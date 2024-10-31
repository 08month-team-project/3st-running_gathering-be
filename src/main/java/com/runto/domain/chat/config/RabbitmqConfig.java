package com.runto.domain.chat.config;

import com.rabbitmq.client.ShutdownSignalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;


@Configuration
public class RabbitmqConfig {

    private static final Logger log = LoggerFactory.getLogger(RabbitmqConfig.class);
    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${rabbit.exchange}")
    private String exchange;

    @Value("${rabbit.direct.queue}")
    private String directQueueName;

    @Value("${rabbit.group.queue}")
    private String groupQueueName;

    @Value("${rabbit.direct.routing}")
    private String directRoutingKey;

    @Value("${rabbit.group.routing}")
    private String groupRoutingKey;

    @Value("${rabbit.dl.exchange}")
    private String deadLetterExchange;

    @Value("${rabbit.dl.queue}")
    private String deadLetterQueue;

    @Value("${rabbit.dl.routing}")
    private String deadLetterRoutingKey;


    @Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);

        connectionFactory.setChannelCacheSize(10);
        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.SIMPLE);
        connectionFactory.setPublisherReturns(true);

        connectionFactory.addConnectionListener(new ConnectionListener() {
            @Override
            public void onCreate(Connection connection) {
                log.info("RabbitMQ connection created: {}",connection);
            }

            @Override
            public void onShutDown(ShutdownSignalException signal) {
                log.warn("RabbitMQ connection shut down: {}",signal.getMessage());
            }

            @Override
            public void onFailed(Exception e) {
                log.error("RabbitMQ connection failed: {}",e.getMessage());
            }
        });
        return connectionFactory;
    }

    //메시지를 JSON 타입으로 변환하기 위한 컨버터 등록
    @Bean
    MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    //메시지 송수신을 위한 템플릿
    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(5);

        //전송 실패시 재시도 템플릿
        RetryTemplate retryTemplate = new RetryTemplate();
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(500); //첫번째 재시도 대기 시간
        backOffPolicy.setMultiplier(2.0); // 대기 시간 증가 배율
        backOffPolicy.setMaxInterval(10000);// 최대 대기 시간

        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(simpleRetryPolicy);

        rabbitTemplate.setRetryTemplate(retryTemplate);
        rabbitTemplate.setMessageConverter(messageConverter);

        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(((correlationData, ack, cause) -> {
            //메시지가 브로커에 도달했는지 확인
            if (ack){
                log.info("Message send success to RabbitMQ");
            }else {
                log.error("Message send failed to RabbitMQ = {}",cause);
            }
        }));
        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            //메시지가 라우팅되지 않았을 경우
            log.error("Message Return = {}",returnedMessage.getMessage());
        });

        return rabbitTemplate;
    }

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(exchange);
    }

    @Bean
    public DirectExchange deadLetterExchange(){
        return new DirectExchange(deadLetterExchange);
    }

    @Bean
    public Queue directChatQueue(){
        return QueueBuilder
                .durable(directQueueName)
                .ttl(90000)
                .maxLengthBytes(1024 * 1024 * 5)
                .build();
    }

    @Bean
    public Binding directChatBinding(Queue directChatQueue, DirectExchange directExchange){
        return BindingBuilder.bind(directChatQueue).to(directExchange).with(directRoutingKey);
    }

    @Bean
    public Queue groupChatQueue(){
        return QueueBuilder.durable(groupQueueName)
                .ttl(90000)
                .maxLengthBytes(1024 * 1024 * 5)
                .build();
    }

    @Bean
    public Binding groupChatBinding(Queue groupChatQueue, DirectExchange directExchange){
        return BindingBuilder.bind(groupChatQueue).to(directExchange).with(groupRoutingKey);
    }


    @Bean Queue deadLetterQueue(){
        return QueueBuilder.durable(deadLetterQueue).build();
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange){
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(deadLetterRoutingKey);
    }


}
