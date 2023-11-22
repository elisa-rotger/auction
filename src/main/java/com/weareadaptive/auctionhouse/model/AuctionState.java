package com.weareadaptive.auctionhouse.model;

import java.util.HashMap;
import java.util.Map;

public class AuctionState extends State<Auction> {

    public AuctionState() {
        Map<String, Auction> auctionIndex = new HashMap<>();
    }

    public Auction[] findAuctionsByOwner(String owner) {
        return stream().filter(auction -> auction.getOwner().equals(owner)).toArray(Auction[]::new);
    }

    public Auction[] findOtherAuctions(String owner) {
        return stream().filter(auction ->
                !auction.getOwner().equals(owner) && auction.getIsOpen()
        ).toArray(Auction[]::new);
    }
}
