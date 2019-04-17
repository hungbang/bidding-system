package com.hbq.biddingsystem.services.impl;

import com.hbq.biddingsystem.dtos.BidCriteria;
import com.hbq.biddingsystem.dtos.BiddingInformationDto;
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

    private final BiddingInformationRepository biddingInformationRepository;
    private final OurMapper ourMapper;

    public BiddingInformationSerivceImpl(BiddingInformationRepository biddingInformationRepository, OurMapper ourMapper) {
        this.biddingInformationRepository = biddingInformationRepository;
        this.ourMapper = ourMapper;
    }

    @Override
    public BiddingInformationDto addBid(BiddingInformationDto biddingInformationDto) {
        return ourMapper.toDto(biddingInformationRepository.save(ourMapper.toEntity(biddingInformationDto)));
    }

    @Override
    public List<BiddingInformationDto> findByCriteria(BidCriteria bidCriteria) {
        return biddingInformationRepository
                .findAllByBidder_EmailOrBidder_IdOrderByCreationDate(bidCriteria.getEmail(), bidCriteria.getBidderId())
                .stream()
                .map(ourMapper::toDto)
                .collect(Collectors.toList());
    }
}
