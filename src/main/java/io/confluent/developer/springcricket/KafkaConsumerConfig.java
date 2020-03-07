package io.confluent.developer.springcricket;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

  public ConsumerFactory consumerFactory() {
    Map<String, Object> props = new HashMap<>();
    String bootstrapServers = "localhost:9092";
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    String groupId = "ticket-cricket";
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    String autoOffsetReset = "latest";
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
    props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
    props.put(JsonDeserializer.TYPE_MAPPINGS,
              "createTicket:com.example.application.domain.command.CreateTicket, createTicketCommand:com.example.application.domain.command.CreateTicketCommand");
    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>());
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<?, ?> createTicketListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<?, ?> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }
}