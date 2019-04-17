package com.hbq.biddingsystem.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Entity
@Table(name = "biddingInformation")
public class BiddingInformation extends BaseEntity<String> implements Serializable {
    private User bidder;
    private AuctionCampaign auctionCampaign;
    private double bidingPrice;
    @Enumerated(EnumType.STRING)
    private BiddingStatus biddingStatus;
}
