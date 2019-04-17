package com.hbq.biddingsystem.dtos;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class BiddingRequest {
    private String productId;
    private String identifier;
    private double bidPrice;

}
