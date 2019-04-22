package com.hbq.biddingsystem.entities;

import lombok.*;

import javax.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "fk_bidder")
    private User bidder;
    @ManyToOne
    @JoinColumn(name = "fk_auctionCampaign")
    private AuctionCampaign auctionCampaign;
    private BigDecimal biddingPrice;
    @Enumerated(EnumType.STRING)
    private BiddingStatus biddingStatus;
}
