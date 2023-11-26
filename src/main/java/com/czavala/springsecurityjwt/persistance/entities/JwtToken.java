package com.czavala.springsecurityjwt.persistance.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 3000) // token contiene muchos caracteres, DB por defecto asigna 256, se debe aumentar
    private String token;

    // fecha de expiracion del token
    private Date expirationDate;

    // indica si token es valido
    private boolean isValid;

    @ManyToOne // un user puede tener muchos tokens, un token se asigna a un solo user
    @JoinColumn(name = "user_id")
    private User user;
}
