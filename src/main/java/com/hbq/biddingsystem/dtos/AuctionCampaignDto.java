package com.hbq.biddingsystem.dtos;

import com.hbq.biddingsystem.entities.BiddingInformation;
import com.hbq.biddingsystem.entities.BiddingStatus;
import com.hbq.biddingsystem.entities.Product;
import com.hbq.biddingsystem.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AuctionCampaignDto extends BaseDto{
    private User auctioneer;
    private Product product;
    private BigDecimal startPrice;
    private LocalDateTime startBidDate;
    private LocalDateTime endBidDate;
    private BiddingStatus biddingStatus;
    private List<BiddingInformation> biddingInformations;
}
