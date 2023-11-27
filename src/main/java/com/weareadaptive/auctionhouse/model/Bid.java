package com.weareadaptive.auctionhouse.model;

import java.math.BigDecimal;

import static com.weareadaptive.auctionhouse.StringUtil.isNullOrEmpty;

public record Bid(String owner, BigDecimal price, int quantity, long submissionTime) {
    public Bid {
        if (isNullOrEmpty(owner)) {
            throw new BusinessException("owner cannot be null or empty");
        }

    }
}
