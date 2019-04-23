package com.hbq.biddingsystem.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbq.biddingsystem.dtos.BidCriteria;
import com.hbq.biddingsystem.dtos.BiddingInformationDto;
import com.hbq.biddingsystem.entities.BiddingInformation;
import com.hbq.biddingsystem.repository.BiddingInformationRepository;
import com.hbq.biddingsystem.services.BiddingInformationService;
import com.hbq.biddingsystem.utils.OurMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackOn = Throwable.class)
public class BiddingInformationServiceImpl implements BiddingInformationService {

    @Value("${kafka.topic}")
    private String topicName;
    private final KafkaTemplate kafkaTemplate;
    private final ObjectMapper objectMapper;

    private final BiddingInformationRepository biddingInformationRepository;
    private final OurMapper ourMapper;

    public BiddingInformationServiceImpl(KafkaTemplate kafkaTemplate, ObjectMapper objectMapper, BiddingInformationRepository biddingInformationRepository, OurMapper ourMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.biddingInformationRepository = biddingInformationRepository;
        this.ourMapper = ourMapper;
    }

    @Override
    public BiddingInformationDto addBid(BiddingInformationDto biddingInformationDto) throws JsonProcessingException {
        priceCanPerformBid(biddingInformationDto);
        BiddingInformation biddingInformation = biddingInformationRepository.save(ourMapper.toEntity(biddingInformationDto));
        kafkaTemplate.send(topicName, objectMapper.writeValueAsString(ourMapper.toDto(biddingInformation)));
        return ourMapper.toDto(biddingInformation);
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
