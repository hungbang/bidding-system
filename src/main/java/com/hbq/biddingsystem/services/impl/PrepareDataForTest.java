package com.hbq.biddingsystem.services.impl;

import com.google.common.collect.Lists;
import com.hbq.biddingsystem.entities.*;
import com.hbq.biddingsystem.repository.AuctionCampaignRepository;
import com.hbq.biddingsystem.repository.ProductRepository;
import com.hbq.biddingsystem.repository.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class PrepareDataForTest {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AuctionCampaignRepository auctionCampaignRepository;

    public PrepareDataForTest(ProductRepository productRepository, UserRepository userRepository, AuctionCampaignRepository auctionCampaignRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.auctionCampaignRepository = auctionCampaignRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void run(){
        this.createProducts();
        this.createUsers();
    }

    public List<Product> createProducts() {
        Product product = Product.builder().title("Iphone 6")
                .description("a product from Apple")
                .name("Iphone 6")
                .quantity(1)
                .price(BigDecimal.valueOf(1000))
                .build();
        Product product1 = Product.builder().title("Iphone 7")
                .description("a product from Apple")
                .name("Iphone 7")
                .quantity(1)
                .price(BigDecimal.valueOf(1000))
                .build();
        Product product2 = Product.builder().title("Iphone 8")
                .description("a product from Apple")
                .name("Iphone 8")
                .quantity(1)
                .price(BigDecimal.valueOf(1000))
                .build();
        Product product3 = Product.builder().title("Iphone 9")
                .description("a product from Apple")
                .name("Iphone 9")
                .quantity(1)
                .price(BigDecimal.valueOf(1000))
                .build();
        return productRepository.saveAll(Lists.newArrayList(product, product1, product2, product3));

    }

    public List<User> createUsers(){
        final User user = User.builder()
                .email("quanhungbang@gmail.com")
                .firstName("Hung")
                .lastName("Bang")
                .type(UserType.AUCNEER)
                .build();
        final User bidder = User.builder()
                .email("bidder@gmail.com")
                .firstName("Hung")
                .lastName("Bang")
                .type(UserType.BIDDER)
                .build();
       return userRepository.saveAll(Lists.newArrayList(user, bidder));
    }


    public AuctionCampaign createAuctionCampaigns(Product product, User user){
        final AuctionCampaign auctionCampaign = AuctionCampaign.builder()
                .product(product)
                .auctioneer(user)
                .startBidDate(LocalDateTime.now())
                .endBidDate(LocalDateTime.now().plusDays(7))
                .startPrice(BigDecimal.ONE)
                .biddingStatus(BiddingStatus.IN_PROGRESS)
                .build();
       return auctionCampaignRepository.save(auctionCampaign);
    }
}
