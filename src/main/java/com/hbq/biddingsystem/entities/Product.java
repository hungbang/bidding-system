package com.hbq.biddingsystem.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Entity
@Table(name = "product")
public class Product extends BaseEntity<String> implements Serializable {

    private String title;
    private String name;
    private String description;
    private int quantity;
    private BigDecimal price;


}
