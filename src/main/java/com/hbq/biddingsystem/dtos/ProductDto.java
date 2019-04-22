package com.hbq.biddingsystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductDto extends BaseDto{

    private String title;
    private String name;
    private String description;
    private int quantity;
    private BigDecimal price;
}
