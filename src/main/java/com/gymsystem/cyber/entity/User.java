package com.gymsystem.cyber.entity;


import com.gymsystem.cyber.enums.UserRole;
import com.gymsystem.cyber.utils.BaseEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.SuperBuilder;


import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account", indexes = {@Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_Uid", columnList = "firebaseUid"),
        @Index(name = "idx_phone", columnList = "phone")})
@SuperBuilder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String firebaseUid;


    @Column(unique = true)
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid phone number format")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 digits")
    private String phone;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "email", nullable = false, unique = true)
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;


    private boolean enable;

    private String verificationCode;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LONGBLOB")
    private byte[] avata;

    @OneToOne(mappedBy = "user")
    private Members members;
}