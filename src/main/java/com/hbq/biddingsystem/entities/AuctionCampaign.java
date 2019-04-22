package com.hbq.biddingsystem.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@Entity
@Table(name = "auctionCampaign")
public class AuctionCampaign extends BaseEntity<String> implements Serializable {

    private User auctioneer;
    private Product product;
    private BigDecimal startPrice;
    private LocalDateTime startBidDate;
    private LocalDateTime endBidDate;
    private BiddingStatus biddingStatus;
    @OneToMany(mappedBy = "auctionCampaign")
    private List<BiddingInformation> biddingInformations;
}
