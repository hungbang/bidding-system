package com.hbq.biddingsystem.configures;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {


    @Value("${kafka.topic}")
    private String topicName;

    @Bean
    public NewTopic topicBid() {
        return new NewTopic(topicName, 3, (short) 1);
    }
}
