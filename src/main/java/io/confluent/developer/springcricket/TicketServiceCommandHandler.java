package io.confluent.developer.springcricket;

import com.example.application.domain.command.CreateTicket;
import com.example.application.domain.command.CreateTicketCommand;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@KafkaListener(topics = "ticket", containerFactory = "createTicketListenerContainerFactory")
@Service
@Slf4j
public class TicketServiceCommandHandler {

  @KafkaHandler
  private void createTicket(CreateTicket command) {
    log.info("Received command {}", command.toString());
  }

  @KafkaHandler
  private void createTicket(CreateTicketCommand command) {
    log.info("Received command {}", command.toString());
  }

  /*
  @KafkaHandler
  private void createTicket(CreateTicketTest3 command) {
              ....
  }*/
}