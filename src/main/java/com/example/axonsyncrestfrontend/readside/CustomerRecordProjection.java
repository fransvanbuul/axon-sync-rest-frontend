package com.example.axonsyncrestfrontend.readside;

import com.example.axonsyncrestfrontend.commandside.CustomerCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.XSlf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.Timestamp;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@XSlf4j
public class CustomerRecordProjection {

    private final EntityManager entityManager;
    private final QueryUpdateEmitter queryUpdateEmitter;

    @EventHandler
    public void on(CustomerCreatedEvent evt, @Timestamp Instant timestamp) {
        log.entry(evt, timestamp);
        CustomerRecord record = new CustomerRecord(evt.getCustomerId(), timestamp, evt.getFirstName(), evt.getLastName());
        /* saving the record in our read model. */
        entityManager.persist(record);
        /* sending it to subscription queries of type FindCustomerQuery, but only if the customer id matches. */
        queryUpdateEmitter.emit(FindCustomerQuery.class,
                query -> query.getCustomerId().equals(evt.getCustomerId()),
                record);
        /* sending it to subscription queries of type FindAllCustomers. */
        queryUpdateEmitter.emit(FindAllCustomersQuery.class,
                query -> true,
                record);
        log.exit();
    }

    @QueryHandler
    public CustomerRecord handle(FindCustomerQuery query) {
        log.entry(query);
        return log.exit(entityManager.find(CustomerRecord.class, query.getCustomerId()));
    }

    @QueryHandler
    public List<CustomerRecord> handle(FindAllCustomersQuery query) {
        log.entry(query);
        TypedQuery<CustomerRecord> jpaQuery = entityManager.createNamedQuery("CustomerRecord.findAll", CustomerRecord.class);
        return log.exit(jpaQuery.getResultList());
    }

}
