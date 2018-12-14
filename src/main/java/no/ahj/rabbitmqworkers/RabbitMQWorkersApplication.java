package no.ahj.rabbitmqworkers;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@PropertySources({
    @PropertySource("classpath:rabbit-workers.properties"),
    @PropertySource(value = "file://${user.home}/.rabbit-workers.properties", ignoreResourceNotFound = true),
    @PropertySource(value = "file:///etc/rabbit-workers.properties", ignoreResourceNotFound = true)
})
@ComponentScan()
@SpringBootApplication
public class RabbitMQWorkersApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(RabbitMQWorkersApplication.class, args);
    }

    /**
     * Receives messages exclusively from the prioritized queue.
     *
     * @param data
     * @return
     * @throws InterruptedException
     */
    @RabbitListener(
            bindings = {
                @QueueBinding(
                        value = @Queue(value = "${mq.priority-queue}", durable = "true"),
                        exchange = @Exchange(value = "${mq.exchange}"),
                        key = "${mq.priority-queue-key}")},
            concurrency = "${mq.priority-queue-concurrency}")
    public String receivePrioritized(Message data) throws InterruptedException
    {
        // Do work..
        return "Return work as reply";
    }

    /**
     * Receives messages from both prioritized and regular queues.
     *
     * @param data
     * @return
     * @throws InterruptedException
     */
    @RabbitListener(
            bindings = {
                @QueueBinding(
                        value = @Queue(value = "${mq.priority-queue}", durable = "true"),
                        exchange = @Exchange(value = "${mq.exchange}"),
                        key = "${mq.priority-key}"),
                @QueueBinding(
                        value = @Queue(value = "${mq.queue}", durable = "true"),
                        exchange = @Exchange(value = "${mq.exchange}"))},
            concurrency = "${mq.queue-concurrency}")
    public String receive(Message data) throws InterruptedException
    {
        // Do work..
        return "Return work as reply";
    }

}
