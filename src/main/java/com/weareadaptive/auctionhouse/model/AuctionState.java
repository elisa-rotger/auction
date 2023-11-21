package com.weareadaptive.auctionhouse.model;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

public class AuctionState extends State<Auction> {

    public AuctionState() {
        Map<String, Auction> auctionIndex = new HashMap<>();
    }

    public BaseStream<Auction, Stream<Auction>> findAuctionsByOwner(String owner) {
        return stream().filter(auction -> auction.getOwner().equals(owner));
    }
}
