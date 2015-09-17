package com.tradedoubler.rgtv;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.support.Function;
import org.springframework.integration.websocket.ServerWebSocketContainer;
import org.springframework.integration.websocket.outbound.WebSocketOutboundMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Created by wen on 9/16/15.
 */

@SpringBootApplication
@EnableWebSocket
@EnableCaching
public class RgtvWebSocketsApplication {

    public static void main(String args[]) throws Throwable {
        SpringApplication.run(RgtvWebSocketsApplication.class, args);
    }

    @SuppressWarnings("UnusedDeclaration")
    @Bean
    public EmbeddedServletContainerFactory containerFactory() {
        int port = 9170;
        String context = "/rgtv";
        return new TomcatEmbeddedServletContainerFactory(context, port);
    }

    @Bean
    ServerWebSocketContainer serverWebSocketContainer() {
        return new ServerWebSocketContainer("/messages").withSockJs();
    }

    @Bean
    MessageHandler webSocketOutboundAdapter() {
        return new WebSocketOutboundMessageHandler(serverWebSocketContainer());
    }

    @Bean(name = "webSocketFlow.input")
    MessageChannel requestChannel() {
        return new DirectChannel();
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        return restTemplate;
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        CloseableHttpClient httpClient =
                HttpClients.custom()
                        .setSSLHostnameVerifier(new NoopHostnameVerifier())
                        .build();
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return requestFactory;
    }

    @Bean
    IntegrationFlow webSocketFlow() {
        return f -> {
            Function<Message, Object> splitter = m -> serverWebSocketContainer()
                    .getSessions()
                    .keySet()
                    .stream()
                    .map(s -> MessageBuilder.fromMessage(m)
                            .setHeader(SimpMessageHeaderAccessor.SESSION_ID_HEADER, s)
                            .build())
                    .collect(Collectors.toList());
            f.split(Message.class, splitter)
                    .channel(c -> c.executor(Executors.newCachedThreadPool()))
                    .handle(webSocketOutboundAdapter());
        };
    }
}
