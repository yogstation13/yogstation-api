package net.yogstation.api.jpa.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "erro_library")
@Data
public class BookEntity {
    @Id
    @GeneratedValue
    private int id;

    private String author;
    private String title;
    private String content;
    private String category;
    private String ckey;
    private LocalDateTime datetime;
    private Integer deleted;
    private Integer roundIdCreated;
}
