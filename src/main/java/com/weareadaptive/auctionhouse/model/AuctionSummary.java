package com.weareadaptive.auctionhouse.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AuctionSummary(BigDecimal totalRevenue, int soldQty, int remainingQty, LocalDateTime closingTime, List<WonBid> winningBids) { }
