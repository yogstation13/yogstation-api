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
    private String aCkey;
    @Column(columnDefinition="mediumtext")
    private String reason;
    private LocalDateTime expirationTime;
    private LocalDateTime unbannedDatetime;
    private LocalDateTime bantime;
    private String role;
    private int roundId;
    private int ip;
    private String computerid; // Why is this a string containing a number???
}
