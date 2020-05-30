package net.yogstation.api.jpa.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "yg_user")
@Data
public class UserEntity {
    @Id
    private int id;
}
