package com.weareadaptive.auctionhouse.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.min;
import static java.util.Collections.reverseOrder;

import static com.weareadaptive.auctionhouse.StringUtil.isNullOrEmpty;
import static com.weareadaptive.auctionhouse.IntUtil.isValidQty;

public class Auction implements Model {
    private final int id;
    private final String owner;
    private final String symbol;
    private final double minPrice;
    private final int availableQty;
    private boolean isOpen;
    private final List<Bid> bidList;
    private final List<WinningBid> winningBidList;
    private AuctionSummary auctionSummary;
    public Auction(int id, String owner, String symbol, double minPrice, int availableQty) {

        if (isNullOrEmpty(owner)) {
            throw new BusinessException("owner cannot be null or empty");
        }

        if (isNullOrEmpty(symbol)) {
            throw new BusinessException("symbol cannot be null or empty");
        }

        if (!isValidQty(availableQty)) {
            throw new BusinessException("availableQty needs to be higher than 0");
        }

        if (!isValidQty(minPrice)) {
            throw new BusinessException("minPrice needs to be higher than 0");
        }

        this.id = id;
        this.owner = owner;
        this.symbol = symbol;
        this.minPrice = minPrice;
        this.availableQty = availableQty;
        this.isOpen = true;
        this.bidList = new ArrayList<Bid>();
        this.winningBidList = new ArrayList<WinningBid>();
    }

    public int getId() { return id; }
    public String getOwner() { return owner; }
    public String getSymbol() { return symbol; }
    public int getAvailableQty() { return availableQty; }
    public double getMinPrice() { return minPrice; }
    public boolean getIsOpen() { return isOpen; }
    public List<Bid> getBidList() { return bidList; }
    public List<WinningBid> getWinningBidList(String bidder) {
        return winningBidList.stream()
                .filter(winningBid -> Objects.equals(winningBid.originalBid().owner(), bidder))
                .toList();
    }
    public AuctionSummary getAuctionSummary() { return auctionSummary; }

    public void bid(String biddingUser, double price, int quantity) {
        if (!this.isOpen) {
            throw new BusinessException("Cannot bid on a closed auction.");
        }

        if (this.owner.equals(biddingUser)) {
            throw new BusinessException("Owner cannot bid on their own auction.");
        }

        if (price < this.minPrice) {
            throw new BusinessException("Price is inferior to minimum price set.");
        }

        if (quantity > availableQty) {
            throw new BusinessException("Bid quantity exceeds available quantity.");
        }

        if (quantity < 1) {
            throw new BusinessException("Bid quantity is too low.");
        }

        // submissionTime might not be needed -> list is sorted by addition date anyway
        // Could be good to generate a timestamp in the summary though, so leaving it in for now
        bidList.add(new Bid(biddingUser, price, quantity, System.currentTimeMillis()));
    }

    List<Bid> orderBidList(List<Bid> originalBidList) {
        return bidList.stream().sorted(reverseOrder(Comparator.comparing(Bid::price))).toList();
    }
    public void closeAuction() {
        if (!isOpen) throw new BusinessException("Cannot close an already closed auction.");

        this.isOpen = false;
        // Order by descending price & execution date
        var orderedBidList = orderBidList(bidList);

        var quantityLeft = availableQty;
        double profit = 0.0;
        // Close them in that order until there is no more quantity left
        for (Bid bid : orderedBidList) {
            if (quantityLeft > 0) {
                // Quantity -> either the quantity left in the auction, or the bidding quantity, whichever is lower
                var quantityToClose = min(quantityLeft, bid.quantity());
                // Add money won by bid to total profit
                profit = profit + (quantityToClose * bid.price());
                // Decrease quantity left on the auction
                quantityLeft = quantityLeft - quantityToClose;

                // Add executed bid to the winning bid list
                winningBidList.add(new WinningBid(quantityToClose, bid));
            }
        }
        // Loop is done -> we have: total money won (profit), quantity left without selling in the auction (quantityLeft)
        var totalSoldQty = availableQty - quantityLeft;
        this.auctionSummary = new AuctionSummary(profit, totalSoldQty, quantityLeft, winningBidList);
    }
}
