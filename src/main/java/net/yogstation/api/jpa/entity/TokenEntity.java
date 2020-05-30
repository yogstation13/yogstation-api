package net.yogstation.api.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "yg_token")
@Data
public class TokenEntity {

    @Id
    private String token;

    private LocalDateTime expires;
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity user;
}
