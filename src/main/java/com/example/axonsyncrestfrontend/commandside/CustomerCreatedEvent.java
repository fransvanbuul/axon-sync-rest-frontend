package com.example.axonsyncrestfrontend.commandside;

import lombok.Value;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

import java.util.UUID;

@Value
public class CustomerCreatedEvent {

    UUID customerId;
    String firstName;
    String lastName;

}
