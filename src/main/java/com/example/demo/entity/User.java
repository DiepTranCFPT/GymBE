package com.example.demo.entity;


import com.example.demo.enums.UserRole;
import com.example.demo.utils.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class User extends BaseEntity  {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long id;

   private String name;

   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
   private String password;

   @Column(unique = true)
   private String phone;

   @Enumerated(EnumType.STRING)
   private UserRole role;

   @Column(unique = true)
   private String email;

   private boolean enable;

   private String verificationCode;

   @OneToMany
   @JoinColumn(name = "calendar_idU", referencedColumnName = "id", insertable = false, updatable = false)
   private List<Calendar> calendar;

}