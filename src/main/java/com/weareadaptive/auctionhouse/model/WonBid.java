package com.weareadaptive.auctionhouse.model;

public record WonBid(int AuctionId, String symbol, int qtyWon, int originalBidQty, double pricePerLot) {
}
