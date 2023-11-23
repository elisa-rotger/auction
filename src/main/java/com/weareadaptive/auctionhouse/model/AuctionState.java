package com.weareadaptive.auctionhouse.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuctionState extends State<Auction> {

    public AuctionState() {
        Map<String, Auction> auctionIndex = new HashMap<>();
    }

    public List<Auction> findAuctionsByOwner(String owner) {
        return stream().filter(auction -> auction.getOwner().equals(owner)).toList();
    }

    public List<Auction> findOtherAuctions(String owner) {
        return stream()
                .filter(Auction::getIsOpen)
                .filter(auction -> !auction.getOwner().equals(owner))
                .toList();
    }

    public List<WonBid> findWonBids(String owner) {
        return stream()
                .filter(auction -> !auction.getIsOpen())
                .flatMap(auction -> auction.getWinningBidList(owner)
                        .stream()
                        .map(winningBid -> new WonBid(
                                auction.getId(),
                                auction.getSymbol(),
                                winningBid.amount(),
                                winningBid.originalBid().getQuantity(),
                                winningBid.originalBid().getPrice())))
                .toList();
    }
}
