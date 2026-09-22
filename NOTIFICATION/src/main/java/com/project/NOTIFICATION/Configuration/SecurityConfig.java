package com.project.NOTIFICATION.Configuration;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.project.NOTIFICATION.DTO.OrderItemStatusNotification;
import com.project.NOTIFICATION.DTO.OrderStatusNotification;
import com.project.NOTIFICATION.Filters.JwtFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    final JwtFilter jwtFilter;
    final KafkaProperties kafkaProperties;

    public SecurityConfig(JwtFilter jwtFilter,KafkaProperties kafkaProperties){
        this.jwtFilter=jwtFilter;
        this.kafkaProperties=kafkaProperties;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity){
        httpSecurity.csrf(csrf->csrf.disable()).headers(frame->frame.disable()).
                authorizeHttpRequests(req->req.requestMatchers("/h2-console/**","/favicon.ico").permitAll().anyRequest().authenticated()).
                addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean 
    public ConsumerFactory<String,OrderStatusNotification> orderStatusConsumerFactory(){

        JacksonJsonDeserializer<OrderStatusNotification> deserializer=new JacksonJsonDeserializer<>(OrderStatusNotification.class);
        deserializer.setUseTypeHeaders(false);
        return new DefaultKafkaConsumerFactory<>(
            kafkaProperties.buildConsumerProperties(),
            new StringDeserializer(),
            deserializer
    );
    }


    @Bean 
    public ConsumerFactory<String,OrderItemStatusNotification> orderItemStatusConsumerFactory(){

        JacksonJsonDeserializer<OrderItemStatusNotification> deserializer=new JacksonJsonDeserializer<>(OrderItemStatusNotification.class);
        deserializer.setUseTypeHeaders(false);
        return new DefaultKafkaConsumerFactory<>(
            kafkaProperties.buildConsumerProperties(),
            new StringDeserializer(),
            deserializer
    );

    
    }

    @Bean
public ConcurrentKafkaListenerContainerFactory<String, OrderStatusNotification>
orderStatusKafkaListenerContainerFactory() {

    var factory =
            new ConcurrentKafkaListenerContainerFactory<String, OrderStatusNotification>();

    factory.setConsumerFactory(orderStatusConsumerFactory());

    return factory;
}

@Bean
public ConcurrentKafkaListenerContainerFactory<String, OrderItemStatusNotification>
orderItemStatusKafkaListenerContainerFactory() {

    var factory =
            new ConcurrentKafkaListenerContainerFactory<String, OrderItemStatusNotification>();

    factory.setConsumerFactory(orderItemStatusConsumerFactory());

    return factory;
}
    
} 
