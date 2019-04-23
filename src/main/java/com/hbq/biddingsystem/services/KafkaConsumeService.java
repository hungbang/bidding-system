package com.hbq.biddingsystem.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumeService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumeService.class);
    private final SimpMessagingTemplate simpMessagingTemplate;

    public KafkaConsumeService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @KafkaListener(topics = "${kafka.topic}")
    public void consume(@Payload String message){
        logger.info("The message is received :", message);
        simpMessagingTemplate.convertAndSend("/topic/updateBid", message);
    }
}
