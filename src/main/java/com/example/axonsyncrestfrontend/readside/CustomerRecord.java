package com.example.axonsyncrestfrontend.readside;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.time.Instant;
import java.util.UUID;

@Entity
@NamedQueries({
        @NamedQuery(name = "CustomerRecord.findAll",
                query = "SELECT c FROM CustomerRecord c ORDER BY c.lastName, c.firstName")
})
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CustomerRecord {

    @Id @EqualsAndHashCode.Include private UUID id;
    private Instant timeOfRegistration;
    private String firstName;
    private String lastName;

}
