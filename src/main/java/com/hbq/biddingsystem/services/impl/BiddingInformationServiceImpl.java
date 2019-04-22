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
public class BiddingInformationServiceImpl implements BiddingInformationService {

    private final BiddingInformationRepository biddingInformationRepository;
    private final OurMapper ourMapper;

    public BiddingInformationServiceImpl(BiddingInformationRepository biddingInformationRepository, OurMapper ourMapper) {
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
        BiddingInformation biddingInformation =
                biddingInformationRepository.findTopByAuctionCampaign_IdOrderByBiddingPriceDesc(biddingInformationDto.getAuctionCampaign().getId());
        if (biddingInformation != null) {
            if (biddingInformationDto.getBiddingPrice().compareTo(biddingInformation.getBiddingPrice()) < 0) {
                throw new IllegalArgumentException("Can not bid a lower price");
            }
        }

    }

    @Override
    public List<BiddingInformationDto> findByCriteria(BidCriteria bidCriteria) {
        return biddingInformationRepository
                .findByBidder_EmailOrderByCreationDateDesc(bidCriteria.getEmail())
                .stream()
                .map(ourMapper::toDto)
                .collect(Collectors.toList());
    }
}
