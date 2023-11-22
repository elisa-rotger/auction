package com.weareadaptive.auctionhouse.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import static java.util.Collections.reverseOrder;

import static com.weareadaptive.auctionhouse.StringUtil.isNullOrEmpty;
import static com.weareadaptive.auctionhouse.IntUtil.isValidQty;

public class Auction implements Model {
    private final int id;
    private final String owner;
    private final String symbol;
    private final double minPrice;
    private int availableQty;
    private boolean isOpen;
    private List<Bid> bidList;

    public int getId() {
        return id;
    }

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

    }

    public String getOwner() { return owner; }
    public String getSymbol() { return symbol; }
    public int getAvailableQty() { return availableQty; }
    public double getMinPrice() { return minPrice; }
    public boolean getIsOpen() { return isOpen; }
    public List<Bid> getBidList() { return bidList; }

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
            // Couldn't this option be partially filled?
            throw new BusinessException("Bid quantity exceeds available quantity.");
        }

        bidList.add(new Bid(biddingUser, price, quantity, System.currentTimeMillis()));
    }

    List<Bid> orderBidList(List<Bid> originalBidList) {
        return bidList.stream().sorted(reverseOrder(Comparator.comparing(Bid::getPrice))).toList();
    }
    public void closeAuction() {
        // TODO: Closing bids logic
        this.isOpen = false;

        // Order by descending price
        var orderedBidList = orderBidList(bidList);

        for (Bid bid : orderedBidList) {
            System.out.println(bid.getQuantity());
        }
    }
}
