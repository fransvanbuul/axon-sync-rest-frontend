package com.example.axonsyncrestfrontend.commandside;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.XSlf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor
@XSlf4j
public class CustomerAggregate {

    @AggregateIdentifier
    private UUID id;

    @CommandHandler
    public CustomerAggregate(CreateCustomerCommand cmd) {
        log.entry(cmd);
        apply(new CustomerCreatedEvent(cmd.getCustomerId(), cmd.getFirstName(), cmd.getLastName()));
    }

    @EventSourcingHandler
    public void on(CustomerCreatedEvent evt) {
        log.entry(evt);
        this.id = evt.getCustomerId();
    }

}
