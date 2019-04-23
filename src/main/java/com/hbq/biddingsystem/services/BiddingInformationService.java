package com.hbq.biddingsystem.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hbq.biddingsystem.dtos.BidCriteria;
import com.hbq.biddingsystem.dtos.BiddingInformationDto;

import java.util.List;

public interface BiddingInformationService {

    BiddingInformationDto addBid(BiddingInformationDto biddingInformationDto) throws JsonProcessingException;
    List<BiddingInformationDto> findByCriteria(BidCriteria bidCriteria);

}
