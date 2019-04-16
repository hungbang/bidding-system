package com.hbq.biddingsystem.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Entity
@Table(name = "user")
public class User extends BaseEntity<String> implements Serializable {

    @Enumerated(EnumType.STRING)
    private UserType type;

    @OneToOne(fetch = FetchType.LAZY)
    private Wallet wallet;

    private String email;
    private String password;
    private String firstName;
    private String lastName;

    //** Custom code is wrote after this line **//

}
