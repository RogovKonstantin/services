package com.example.demo.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    public static final String createQueueName = "createQueue";
    public static final String updateQueueName = "updateQueue";
    public static final String deleteQueueName = "deleteQueue";
    public static final String validateQueueName = "validateQueue";
    public static final String validationResponseQueueName = "validationResponseQueue";
    public static final String exchangeName = "listings.exchange";
    public static final String exchangeNameValidation = "validation.exchange";





    @Bean
    public Queue createQueue() {
        return new Queue(createQueueName, false);
    }

    @Bean
    public Queue updateQueue() {
        return new Queue(updateQueueName, false);
    }

    @Bean
    public Queue deleteQueue() {
        return new Queue(deleteQueueName, false);
    }

    @Bean
    public Queue validateQueue() {
        return new Queue(validateQueueName, false);
    }
    @Bean
    public Queue validationResponseQueue() {
        return new Queue(validationResponseQueueName, false);
    }

    @Bean
    public TopicExchange listingExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding createBinding(Queue createQueue, TopicExchange listingExchange) {
        return BindingBuilder.bind(createQueue).to(listingExchange).with("listings.create");
    }

    @Bean
    public Binding updateBinding(Queue updateQueue, TopicExchange listingExchange) {
        return BindingBuilder.bind(updateQueue).to(listingExchange).with("listings.update");
    }

    @Bean
    public Binding deleteBinding(Queue deleteQueue, TopicExchange listingExchange) {
        return BindingBuilder.bind(deleteQueue).to(listingExchange).with("listings.delete");
    }

    @Bean
    public Binding validationBinding(Queue validateQueue,TopicExchange listingExchange){
        return BindingBuilder.bind(validateQueue).to(listingExchange).with("listings.validate");
    }

    @Bean
    public Binding validationResponseBinding(Queue validationResponseQueue  ,TopicExchange listingExchange){
        return BindingBuilder.bind(validationResponseQueue).to(listingExchange).with("listings.validate.response");
    }


}
