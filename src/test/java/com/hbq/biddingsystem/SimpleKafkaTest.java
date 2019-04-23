package com.hbq.biddingsystem;


import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class SimpleKafkaTest {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(SimpleKafkaTest.class);
    private static final String TOPIC = "topic-bid";

    @ClassRule
    public static EmbeddedKafkaRule embeddedKafka =
            new EmbeddedKafkaRule(1, true, TOPIC);

    private Consumer<String, String> consumer;


    @Before
    public void before() {

        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("consumer", "false", embeddedKafka.getEmbeddedKafka()));
        consumer = new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new StringDeserializer()).createConsumer();
        consumer.subscribe(Collections.singleton(TOPIC));
        consumer.poll(0);
    }

    @After
    public void tearDown() {
        consumer.close();
    }

    @Test
    public void kafkaSetup_withTopic_ensureSendMessageIsReceived() {
        // GIVEN
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafka.getEmbeddedKafka()));
        Producer<String, String> producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new StringSerializer()).createProducer();

        // WHEN
        producer.send(new ProducerRecord<>(TOPIC, "my-aggregate-id", "{\"event\":\"Test Event\"}"));
        producer.flush();

        // THEN
        ConsumerRecord<String, String> singleRecord = KafkaTestUtils.getSingleRecord(consumer, TOPIC);
        Assert.assertNotNull(singleRecord);
        Assert.assertEquals("my-aggregate-id", singleRecord.key());
        Assert.assertEquals("{\"event\":\"Test Event\"}", singleRecord.value());
    }
}
