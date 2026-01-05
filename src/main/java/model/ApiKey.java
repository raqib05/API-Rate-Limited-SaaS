package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
public class ApiKey {
    @Id
    UUID id;
    String keyHash;

    @ManyToOne
    Tenant tenant;

    boolean revoked;
    Instant createdAt;
    Instant expiresAt;
}
