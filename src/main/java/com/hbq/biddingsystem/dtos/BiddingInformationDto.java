package com.hbq.biddingsystem.dtos;

import com.hbq.biddingsystem.entities.AuctionCampaign;
import com.hbq.biddingsystem.entities.BiddingStatus;
import com.hbq.biddingsystem.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BiddingInformationDto extends BaseDto{
    private User bidder;
    private AuctionCampaign auctionCampaign;
    private double bidingPrice;
    private BiddingStatus biddingStatus;
}
