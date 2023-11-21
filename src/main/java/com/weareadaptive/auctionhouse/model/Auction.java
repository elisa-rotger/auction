package com.weareadaptive.auctionhouse.model;

import static com.weareadaptive.auctionhouse.StringUtil.isNullOrEmpty;

public class Auction implements Model {
    private final int id;
    private final String owner;
    private final String symbol;
    private final double minPrice;
    private final int availableQty;

    // This class should have a modifiable list of bids
    // TODO: Bid class?
    // private array bids;

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

        // TODO: Add validation for minimum price and available quantity
        // Min Price: has to be higher than 0
        // Quantity: non-negative (equal or higher than 0?)

        this.id = id;
        this.owner = owner;
        this.symbol = symbol;
        this.minPrice = minPrice;
        this.availableQty = availableQty;

    }

    public String getOwner() { return owner; }
}
