package com.hbq.biddingsystem.dtos;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class BidCriteria {
    private String bidderId;
    private String email;
}
