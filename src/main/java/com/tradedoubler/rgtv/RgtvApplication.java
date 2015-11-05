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
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by wen on 9/18/15.
 */
@SpringBootApplication
@EnableWebSocketMessageBroker
@EnableCaching
@EnableScheduling
public class RgtvApplication extends AbstractWebSocketMessageBrokerConfigurer implements SchedulingConfigurer {

    public static void main(String args[]) throws Throwable {
        SpringApplication.run(RgtvApplication.class, args);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp").withSockJS();
    }

    @SuppressWarnings("UnusedDeclaration")
    @Bean
    public EmbeddedServletContainerFactory containerFactory() {
        int port = 9170;
        String context = "/rgtv";
        return new TomcatEmbeddedServletContainerFactory(context, port);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // use the /topic prefix for outgoing WebSocket communication
        config.enableSimpleBroker("/topic");
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        return restTemplate;
    }

    @Bean
    public CacheManager cacheManager() {
        return new EhCacheCacheManager();
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(200);
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

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }
}
