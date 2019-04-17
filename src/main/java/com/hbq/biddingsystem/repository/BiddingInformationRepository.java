package com.hbq.biddingsystem.repository;

import com.hbq.biddingsystem.entities.BiddingInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BiddingInformationRepository extends JpaRepository<BiddingInformation, String> {

    List<BiddingInformation> findAllByBidder_EmailOrBidder_IdOrderByCreationDate(String email, String id);
}
