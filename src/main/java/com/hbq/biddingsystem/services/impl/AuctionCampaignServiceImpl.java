package com.hbq.biddingsystem.services.impl;

import com.hbq.biddingsystem.dtos.AuctionCampaignDto;
import com.hbq.biddingsystem.dtos.BiddingItemParam;
import com.hbq.biddingsystem.entities.AuctionCampaign;
import com.hbq.biddingsystem.entities.BiddingStatus;
import com.hbq.biddingsystem.entities.Product;
import com.hbq.biddingsystem.exception.OperationNotAllowedException;
import com.hbq.biddingsystem.repository.AuctionCampaignRepository;
import com.hbq.biddingsystem.repository.ProductRepository;
import com.hbq.biddingsystem.services.AuctionCampaignService;
import com.hbq.biddingsystem.utils.OurMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackOn = Throwable.class)
public class AuctionCampaignServiceImpl implements AuctionCampaignService {

    private final OurMapper ourMapper;
    private final ProductRepository productRepository;
    private final AuctionCampaignRepository auctionCampaignRepository;
    public AuctionCampaignServiceImpl(OurMapper ourMapper,
                                      ProductRepository productRepository,
                                      AuctionCampaignRepository auctionCampaignRepository) {
        this.ourMapper = ourMapper;
        this.productRepository = productRepository;
        this.auctionCampaignRepository = auctionCampaignRepository;
    }

    @Override
    public AuctionCampaignDto createAuctionCampaign(BiddingItemParam biddingItemParam) {
        Product product = productRepository.save(ourMapper.toEntity(biddingItemParam.getProductDto()));
        return ourMapper.toDto(auctionCampaignRepository.save(AuctionCampaign.builder()
                //TODO: Uses spring security context holder to fetch the appropriate auctioneer
                .auctioneer(null)
                .product(product)
                .startPrice(product.getPrice())
                .startBidDate(biddingItemParam.getStartBidDate())
                .endBidDate(biddingItemParam.getEndBidDate())
                .build()));
    }

    @Override
    public AuctionCampaignDto updateAuctionCampaign(AuctionCampaignDto auctionCampaignDto) throws OperationNotAllowedException {
        //TODO: auctioneers cannot update/edit the price when bidding is progress
        Optional<AuctionCampaign> optional = auctionCampaignRepository.findById(auctionCampaignDto.getId());
        if(optional.isPresent()){
            AuctionCampaign auctionCampaign = optional.get();
            if(auctionCampaign.getBiddingStatus().equals(BiddingStatus.IN_PROGRESS)
                    && auctionCampaign.getStartPrice().compareTo(auctionCampaignDto.getStartPrice()) != 0){
                throw new OperationNotAllowedException("Cannot edit the price when bidding is in progress");
            }
        }
        return ourMapper.toDto(auctionCampaignRepository.save(ourMapper.toEntity(auctionCampaignDto)));
    }

    @Override
    public List<AuctionCampaignDto> findAll() {
        return auctionCampaignRepository
                .findAll()
                .stream()
                .map(ourMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AuctionCampaignDto findById(String id) {
        Optional<AuctionCampaign> optional = auctionCampaignRepository.findById(id);
        return optional.map(ourMapper::toDto).orElseThrow(NullPointerException::new);
    }
}
