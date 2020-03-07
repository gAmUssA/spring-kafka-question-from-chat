package io.confluent.developer.springcricket;

import com.example.application.domain.command.CreateTicketCommand;


import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
public class SpringCricketApplication {

  @Bean
  NewTopic newTicketTopic() {
    return new NewTopic("ticket", 3, (short) 1);


  }

  @Bean
  public ProducerFactory<String, CreateTicketCommand> producerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
        "localhost:9092");
    configProps.put(
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class);
    configProps.put(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        JsonSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, CreateTicketCommand> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringCricketApplication.class, args);
  }

}

// test only
@RequiredArgsConstructor
@Slf4j
@Component
class Producer {

  private final KafkaTemplate<String, CreateTicketCommand> kafkaTemplate;

  @EventListener(ApplicationStartedEvent.class)
  public void produce() {
    log.info("hi");

    kafkaTemplate.send("ticket", new CreateTicketCommand("boom", 42));

  }
}
