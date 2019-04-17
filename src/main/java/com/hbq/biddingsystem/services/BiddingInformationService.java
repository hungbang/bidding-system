package com.hbq.biddingsystem.services;

import com.hbq.biddingsystem.dtos.BidCriteria;
import com.hbq.biddingsystem.dtos.BiddingInformationDto;

import java.util.List;

public interface BiddingInformationService {

    BiddingInformationDto addBid(BiddingInformationDto biddingInformationDto);
    List<BiddingInformationDto> findByCriteria(BidCriteria bidCriteria);

}
