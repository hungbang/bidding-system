package com.hbq.biddingsystem.entities;

import com.hbq.biddingsystem.dtos.ProductDto;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

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
    private String quantity;
    private double startPrice;
    @Enumerated(EnumType.STRING)
    private BiddingStatus biddingStatus;

    @Transient
    public ProductDto toDto() {
        //TODO: convert product to dto
        return null;
    }
}
