package com.weareadaptive.auctionhouse.model;

import static com.weareadaptive.auctionhouse.IntUtil.isValidQty;
import static com.weareadaptive.auctionhouse.StringUtil.isNullOrEmpty;

public class Bid{
    private final double price;
    private final int quantity;
    private final String owner;
    private final long submissionTime;

    public Bid(String owner, double price, int quantity, long submissionTime) {
        if (isNullOrEmpty(owner)) {
            throw new BusinessException("owner cannot be null or empty");
        }

        if (!isValidQty(price)) {
            throw new BusinessException("price needs to be higher than 0");
        }

        if (!isValidQty(quantity)) {
            throw new BusinessException("quantity needs to be higher than 0");
        }

        this.price = price;
        this.quantity = quantity;
        this.owner = owner;
        this.submissionTime = submissionTime;
    }

    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public long getSubmissionTime() { return submissionTime; }
    public String getOwner() { return owner; }
}
