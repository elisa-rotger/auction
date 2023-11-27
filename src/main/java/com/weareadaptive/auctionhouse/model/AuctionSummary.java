package com.weareadaptive.auctionhouse.model;

import java.math.BigDecimal;
import java.util.List;

// TODO: Add auction closing time
// Investigate best way to represent time (not Date, not long)
public record AuctionSummary(BigDecimal totalRevenue, int soldQty, int remainingQty, List<WonBid> winningBids) { }
