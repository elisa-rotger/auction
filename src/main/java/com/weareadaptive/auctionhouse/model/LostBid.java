package com.weareadaptive.auctionhouse.model;

import java.math.BigDecimal;

public record LostBid(int AuctionId, String symbol, int quantityLeft, double pricePerLot, String bidder) {
}
