package com.hbq.biddingsystem.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Entity
@Table(name = "transaction")
public class Transaction extends BaseEntity<String> implements Serializable {

    private BigDecimal amount;
    private String description;

    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;
}
