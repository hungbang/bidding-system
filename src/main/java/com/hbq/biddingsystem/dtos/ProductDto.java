package com.hbq.biddingsystem.dtos;

import com.hbq.biddingsystem.entities.BiddingStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductDto extends BaseDto{

    private String title;
    private String name;
    private String description;
    private String quantity;
    private double startPrice;
    private BiddingStatus biddingStatus;
}
