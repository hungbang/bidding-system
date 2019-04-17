package com.hbq.biddingsystem.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class BiddingItemParam {
    private ProductDto productDto;
    private LocalDateTime startBidDate;
    private LocalDateTime endBidDate;

}
