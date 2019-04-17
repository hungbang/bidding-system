package com.hbq.biddingsystem.utils;

import com.hbq.biddingsystem.dtos.AuctionCampaignDto;
import com.hbq.biddingsystem.dtos.BiddingInformationDto;
import com.hbq.biddingsystem.dtos.ProductDto;
import com.hbq.biddingsystem.entities.AuctionCampaign;
import com.hbq.biddingsystem.entities.BiddingInformation;
import com.hbq.biddingsystem.entities.Product;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OurMapper {


    private final ModelMapper modelMapper;

    public OurMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Product mapping
    public ProductDto toDto(Product product){
        return modelMapper.map(product, ProductDto.class);
    }

    public Product toEntity(ProductDto productDto){
        return modelMapper.map(productDto, Product.class);
    }

    // AuctionCampaign mapping
    public AuctionCampaignDto toDto(AuctionCampaign auctionCampaign){
        return modelMapper.map(auctionCampaign, AuctionCampaignDto.class);
    }
    public AuctionCampaign toEntity(AuctionCampaignDto auctionCampaignDto){
        return modelMapper.map(auctionCampaignDto, AuctionCampaign.class);
    }

    // biddingInformation mapping
    public BiddingInformationDto toDto(BiddingInformation biddingInformation){
        return modelMapper.map(biddingInformation, BiddingInformationDto.class);
    }
    public BiddingInformation toEntity(BiddingInformationDto biddingInformationDto){
        return modelMapper.map(biddingInformationDto, BiddingInformation.class);
    }
}
