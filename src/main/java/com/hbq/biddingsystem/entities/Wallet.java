package com.hbq.biddingsystem.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Entity
@Table(name = "wallet")
public class Wallet extends BaseEntity<String> implements Serializable {

    private BigDecimal balance;
    private String currency;

    @OneToOne(mappedBy = "wallet", fetch = FetchType.LAZY)
    private User user;
}
