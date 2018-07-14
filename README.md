# axon-sync-rest-frontend
This is a small example illustrating how to create a synchronous REST front-end on an asynchronous CQRS+ES back-end using
Axon's subscription queries.

Tech stack used:
* Axon Framework 3.3.1
* Java 8
* Spring Boot 2
* Reactor Core 3
* Swagger 2
* Lombok
* H2 / JPA

No external dependencies, run using mvn spring-boot:run

Test via http://localhost:8080/swagger-ui.html

Trace-level logging enabled by default for illustration of the process.