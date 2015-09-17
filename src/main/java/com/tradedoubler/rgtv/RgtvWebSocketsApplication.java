package com.tradedoubler.rgtv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Created by wen on 9/16/15.
 */

@SpringBootApplication
@EnableWebSocket
@RestController
public class RgtvWebSocketsApplication {

    public static void main(String args[]) throws Throwable {
        SpringApplication.run(RgtvWebSocketsApplication.class, args);
    }

    @SuppressWarnings("UnusedDeclaration")
    @Bean
    public EmbeddedServletContainerFactory containerFactory() {
        int port = 9170;

        return new TomcatEmbeddedServletContainerFactory(port);
    }

    @Bean
    ServerWebSocketContainer serverWebSocketContainer() {
        return new ServerWebSocketContainer("/ips").withSockJs();
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

    @RequestMapping("/clickip/{ip}")
    public void send(@PathVariable String ip) {
        //https://freegeoip.net/json/8.8.8.8
        requestChannel().send(MessageBuilder.withPayload(ip).build());
    }
}
