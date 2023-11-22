package com.weareadaptive.auctionhouse.model;

import java.util.List;

public record AuctionSummary(double totalRevenue, int soldQty, int remainingQty, List<WinningBid> winningBids) { }
