package net.yogstation.api.jpa.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "erro_library")
@Data
public class BookEntity {
    @Id
    @GeneratedValue
    private int id;

    private String author;
    private String title;
    @Column(columnDefinition="mediumtext")
    private String content;
    private String category;
    private String ckey;
    private LocalDateTime datetime;
    private Integer deleted;
    private Integer roundIdCreated;
}
