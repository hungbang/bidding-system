package com.hbq.biddingsystem.services;

import com.hbq.biddingsystem.dtos.AuctionCampaignDto;
import com.hbq.biddingsystem.dtos.BiddingItemParam;
import com.hbq.biddingsystem.exception.OperationNotAllowedException;

import java.util.List;

public interface AuctionCampaignService {


    AuctionCampaignDto createAuctionCampaign(BiddingItemParam biddingItemParam);

    AuctionCampaignDto updateAuctionCampaign(AuctionCampaignDto auctionCampaignDto) throws OperationNotAllowedException;

    List<AuctionCampaignDto> findAll();

    AuctionCampaignDto findById(String id);
}
