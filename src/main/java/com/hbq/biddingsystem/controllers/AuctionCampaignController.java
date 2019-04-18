package com.hbq.biddingsystem.controllers;

import com.hbq.biddingsystem.dtos.AuctionCampaignDto;
import com.hbq.biddingsystem.dtos.BiddingItemParam;
import com.hbq.biddingsystem.exception.OperationNotAllowedException;
import com.hbq.biddingsystem.services.AuctionCampaignService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/auctions")
public class AuctionCampaignController {


    private final AuctionCampaignService auctionCampaignService;

    public AuctionCampaignController(AuctionCampaignService auctionCampaignService) {
        this.auctionCampaignService = auctionCampaignService;
    }

    /**
     * The current user will be able to create new bidding item
     *
     * @param biddingItemParam
     * @return AuctionCampaignDto
     */
    @PostMapping
    public ResponseEntity<AuctionCampaignDto> createNewBidItem(@RequestBody BiddingItemParam biddingItemParam) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(auctionCampaignService.createAuctionCampaign(biddingItemParam));
    }

    /**
     * The auctioneer will be able to edit the auction item that they created before.
     * @param auctionCampaignDto
     * @return AuctionCampaignDto
     */
    @PutMapping
    public ResponseEntity<AuctionCampaignDto> updateAuctionCampaign(@RequestBody AuctionCampaignDto auctionCampaignDto) throws OperationNotAllowedException {
        return ResponseEntity.ok(auctionCampaignService.updateAuctionCampaign(auctionCampaignDto));
    }

    /**
     * Listing all auction campaign
     * @return
     */
    @GetMapping
    public ResponseEntity<List<AuctionCampaignDto>> findAllAuctionCampaign(){
        return ResponseEntity.ok(auctionCampaignService.findAll());
    }

    /**
     * Getting a detail of auction item
     * @param auctionId
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<AuctionCampaignDto> findById(@PathVariable("id") String auctionId){
        return ResponseEntity.ok(auctionCampaignService.findById(auctionId));
    }

}
