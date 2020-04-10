package net.yogstation.api.jpa.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "yg_permission")
@Data
public class PermissionEntity {
    @Id
    @GeneratedValue
    private int id;
    private int userGroup;
    private int user;

    @NotNull
    @NotBlank
    private String permission;
}
