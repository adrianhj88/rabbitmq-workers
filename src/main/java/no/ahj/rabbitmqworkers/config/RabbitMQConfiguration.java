package no.ahj.rabbitmqworkers.config;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfiguration {

    @Bean
    public ConnectionFactory connectionFactory(
            @Value("${mq.hostname}") String mqHostname,
            @Value("${mq.port}") int mqPort,
            @Value("${mq.username}") String mqUsername,
            @Value("${mq.password}") String mqPassword)
    {
        final CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(mqHostname);
        connectionFactory.setPort(mqPort);
        connectionFactory.setUsername(mqUsername);
        connectionFactory.setPassword(mqPassword);
        return connectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory)
    {
        final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // Set prefetch count to zero as we only fetch when consumers are ready for work.
        factory.setPrefetchCount(0);
        return factory;
    }
}
