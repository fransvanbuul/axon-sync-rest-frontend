package com.example.axonsyncrestfrontend.readside;

import lombok.Value;

import java.util.UUID;

/**
 * Query representing a request to find a single customer by their customerId.
 * Both the initialResponse and updates will be of type CustomerRecord.
 */
@Value
public class FindCustomerQuery {

    UUID customerId;

}
