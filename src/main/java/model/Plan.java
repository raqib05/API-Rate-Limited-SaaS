package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Plan {

    @Id
    UUID id;

    String name;

    int requestsPerMinute;
    int requestsPerDay;
    long monthlyQuota;

    boolean active;
}

