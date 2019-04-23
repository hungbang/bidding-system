package com.hbq.biddingsystem.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hbq.biddingsystem.dtos.BidCriteria;
import com.hbq.biddingsystem.dtos.BiddingInformationDto;
import com.hbq.biddingsystem.services.BiddingInformationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/biddingInformations")
public class BiddingInformationController {


    private final BiddingInformationService biddingInformationService;

    public BiddingInformationController(BiddingInformationService biddingInformationService) {
        this.biddingInformationService = biddingInformationService;
    }

    @PostMapping
    public ResponseEntity<BiddingInformationDto> newBid(@RequestBody BiddingInformationDto biddingInformationDto) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(biddingInformationService.addBid(biddingInformationDto));
    }


    @GetMapping
    public ResponseEntity<List<BiddingInformationDto>> findBiddingInformationByCriteria(BidCriteria bidCriteria){
        return ResponseEntity.ok(biddingInformationService.findByCriteria(bidCriteria));
    }

}
