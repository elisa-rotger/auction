package com.weareadaptive.auctionhouse.model;

import static com.weareadaptive.auctionhouse.StringUtil.isNullOrEmpty;
import static com.weareadaptive.auctionhouse.IntUtil.isValidQty;

public class Auction implements Model {
    private final int id;
    private final String owner;
    private final String symbol;
    private final double minPrice;
    private int availableQty;
    private boolean isOpen;

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

        if (!isValidQty(availableQty)) {
            throw new BusinessException("available quantity needs to be higher than 0");
        }

        if (!isValidQty(minPrice)) {
            throw new BusinessException("minimum price needs to be higher than 0");
        }

        this.id = id;
        this.owner = owner;
        this.symbol = symbol;
        this.minPrice = minPrice;
        this.availableQty = availableQty;
        this.isOpen = true;

    }

    public String getOwner() { return owner; }

    public String getSymbol() { return symbol; }

    public int getAvailableQty() { return availableQty; }

    public double getMinPrice() { return minPrice; }
    public boolean getIsOpen() { return isOpen; }
}
