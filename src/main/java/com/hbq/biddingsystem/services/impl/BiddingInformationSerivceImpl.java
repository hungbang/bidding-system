package com.hbq.biddingsystem.services.impl;

import com.hbq.biddingsystem.dtos.BidCriteria;
import com.hbq.biddingsystem.dtos.BiddingInformationDto;
import com.hbq.biddingsystem.entities.BiddingInformation;
import com.hbq.biddingsystem.repository.BiddingInformationRepository;
import com.hbq.biddingsystem.services.BiddingInformationService;
import com.hbq.biddingsystem.utils.OurMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackOn = Throwable.class)
public class BiddingInformationSerivceImpl implements BiddingInformationService {

    private final static int LOWER_OPERATION = -1;
    private final BiddingInformationRepository biddingInformationRepository;
    private final OurMapper ourMapper;

    public BiddingInformationSerivceImpl(BiddingInformationRepository biddingInformationRepository, OurMapper ourMapper) {
        this.biddingInformationRepository = biddingInformationRepository;
        this.ourMapper = ourMapper;
    }

    @Override
    public BiddingInformationDto addBid(BiddingInformationDto biddingInformationDto) {
        //TODO: checking if the new price is lower than already price or not.
        priceCanPerformBid(biddingInformationDto);
        return ourMapper.toDto(biddingInformationRepository.save(ourMapper.toEntity(biddingInformationDto)));
    }

    private void priceCanPerformBid(BiddingInformationDto biddingInformationDto) {
        List<BiddingInformation> biddingInformations =
                biddingInformationRepository.findAllByAuctionCampaign_IdOrderByBidingPriceDesc(biddingInformationDto.getAuctionCampaign().getId());
        if(!biddingInformations.isEmpty()){
            if(biddingInformationDto.getBiddingPrice().compareTo(biddingInformations.get(0).getBiddingPrice()) < 0){
                throw new IllegalArgumentException("Can not bid a lower price");
            }
        }
    }

    @Override
    public List<BiddingInformationDto> findByCriteria(BidCriteria bidCriteria) {
        return biddingInformationRepository
                .findAllByBidder_EmailOrderByCreationDateDesc(bidCriteria.getEmail())
                .stream()
                .map(ourMapper::toDto)
                .collect(Collectors.toList());
    }
}
