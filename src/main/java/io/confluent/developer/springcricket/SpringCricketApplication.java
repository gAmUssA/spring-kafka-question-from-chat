package io.confluent.developer.springcricket;

import com.example.application.domain.command.CreateTicket;
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
  public ProducerFactory<?, ?> producerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    props.put(JsonSerializer.TYPE_MAPPINGS,
              "createTicket:com.example.application.domain.command.CreateTicket, createTicketCommand:com.example.application.domain.command.CreateTicketCommand");
    return new DefaultKafkaProducerFactory<>(props);
  }

  @Bean
  public KafkaTemplate<?, ?> kafkaTemplate() {
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

  private final KafkaTemplate kafkaTemplate;

  @EventListener(ApplicationStartedEvent.class)
  public void produce() {
    kafkaTemplate.send("ticket", new CreateTicketCommand("boom", 42));
    kafkaTemplate.send("ticket", new CreateTicket("boom", "hello"));
  }
}
