package net.yogstation.api.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "erro_ban")
@Data
public class BanEntity {
    @Id
    @GeneratedValue
    private int id;

    private String ckey;
    private String a_ckey;
    @Column(columnDefinition="mediumtext")
    private String reason;
    private LocalDateTime expirationTime;
    private LocalDateTime unbannedDatetime;
    private LocalDateTime bantime;
    private String role;
    private int roundId;
}
