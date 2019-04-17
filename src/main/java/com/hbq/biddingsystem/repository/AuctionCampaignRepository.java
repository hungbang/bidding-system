package com.hbq.biddingsystem.repository;

import com.hbq.biddingsystem.entities.AuctionCampaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionCampaignRepository extends JpaRepository<AuctionCampaign, String> {
}
