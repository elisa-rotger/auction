package com.weareadaptive.auctionhouse.model;

import java.math.BigDecimal;

public record WonBid(int AuctionId, String symbol, int qtyWon, int originalBidQty, BigDecimal pricePerLot, String bidder) {
}
