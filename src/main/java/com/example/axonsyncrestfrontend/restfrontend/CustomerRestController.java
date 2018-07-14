package com.example.axonsyncrestfrontend.restfrontend;

import com.example.axonsyncrestfrontend.commandside.CreateCustomerCommand;
import com.example.axonsyncrestfrontend.readside.CustomerRecord;
import com.example.axonsyncrestfrontend.readside.FindAllCustomersQuery;
import com.example.axonsyncrestfrontend.readside.FindCustomerQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.XSlf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.axonframework.queryhandling.responsetypes.ResponseTypes;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@XSlf4j
public class CustomerRestController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    /**
     * This is a synchronous POST handler that will create a new customer record based on the
     * request data, and return the new record created in the read model.
     *
     * (Although fully asynchronous designs may be preferable for a number of reasons, it is
     * a common scenario that back-end teams are forced to provide a synchronous REST API like
     * this on their asynchronous CQRS+ES back-ends.)
     */
    @PostMapping
    public CustomerRecord handlePost(@RequestBody CreateCustomerRequest request) {
        log.entry(request);
        /* Generating a unique identifier for the new aggregate. */
        UUID customerId = UUID.randomUUID();
        /* We query for customer records with this id. The initial result of this query will be empty,
         * so we'll ignore it, but we are interested in the updates. Note that updates will be collected
         * from the moment we do this call, regardless of when we decide to subscribe to them.
         */
        SubscriptionQueryResult<CustomerRecord, CustomerRecord> queryResult = queryGateway.subscriptionQuery(
                new FindCustomerQuery(customerId),
                ResponseTypes.instanceOf(CustomerRecord.class),
                ResponseTypes.instanceOf(CustomerRecord.class));
        try {
            /* Sending the command to create the customer. */
            commandGateway.sendAndWait(new CreateCustomerCommand(
                    customerId, request.getFirstName(), request.getLastName()));
            /* Returning the first update send to our find customer query. */
            return log.exit(queryResult.updates().blockFirst());
        } finally {
            /* Closing the subscription query. */
            queryResult.close();
        }
    }

    /**
     * For illustration purposes, also method doing a non-subscription query.
     */
    @GetMapping
    public List<CustomerRecord> handleGet() {
        log.entry();
        CompletableFuture<List<CustomerRecord>> result = queryGateway.query(
                new FindAllCustomersQuery(), ResponseTypes.multipleInstancesOf(CustomerRecord.class));
        return log.exit(result.join());
    }

}
