package com.hbq.biddingsystem.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

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
    private BigDecimal biddingPrice;
    @Enumerated(EnumType.STRING)
    private BiddingStatus biddingStatus;
}
