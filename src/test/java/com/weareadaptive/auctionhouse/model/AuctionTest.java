package com.weareadaptive.auctionhouse.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class AuctionTest {
    private static Stream<Arguments> createAuctionArguments() {
        return Stream.of(
                // Null / empty owner
                Arguments.of("owner",
                        (Executable) () -> new Auction(1, null, "pp", 20.5, 5)),
                Arguments.of("owner",
                        (Executable) () -> new Auction(1, "", "pp", 20.5, 5)),
                // Null / empty symbol
                Arguments.of("symbol",
                        (Executable) () -> new Auction(1, "username", null, 20.5, 5)),
                Arguments.of("symbol",
                        (Executable) () -> new Auction(1, "username", "", 20.5, 5)),
                // 0 / negative minimum price
                Arguments.of("minPrice",
                        (Executable) () -> new Auction(1, "username", "pp", 0, 5)),
                Arguments.of("minPrice",
                        (Executable) () -> new Auction(1, "username", "pp", -36, 5)),
                // 0 / negative available quantity
                Arguments.of("availableQty",
                        (Executable) () -> new Auction(1, "username", "pp", 20.5, 0)),
                Arguments.of("availableQty",
                        (Executable) () -> new Auction(1, "username", "pp", 20.5, -6))
        );
    }

    @ParameterizedTest(name = "Create auction should throw exception when {0} is invalid")
    @MethodSource("createAuctionArguments")
    public void createAuctionShouldThrowWhenInvalidProperty(String propertyName, Executable auctionExecutable) {
        var exception = assertThrows(BusinessException.class, auctionExecutable);
        assertTrue(exception.getMessage().contains(propertyName));
    }

    // Bidding tests
    @Test
    @DisplayName("should throw an exception when bidding on a closed auction")
    public void shouldThrowIfAuctionIsClosed() {
        var auction = new Auction(1, "owner", "AAPL", 20.0, 5);
        auction.closeAuction();
        var exception = assertThrows(BusinessException.class, () -> auction.bid("bidder",22, 2));
        assertTrue(exception.getMessage().contains("closed"));
    }

    @Test
    @DisplayName("should throw an exception when the bidder is the auction owner")
    public void shouldThrowIfBidderIsOwner() {
        var auction = new Auction(1, "owner", "AAPL", 20.0, 5);
        var exception = assertThrows(BusinessException.class, () -> auction.bid("owner",22, 2));
        assertTrue(exception.getMessage().contains("Owner"));
    }

    @Test
    @DisplayName("should throw an exception when the bidding price is below the minimum price")
    public void shouldThrowIfPriceIsTooLow() {
        var auction = new Auction(1, "owner", "AAPL", 20.0, 5);
        var exception = assertThrows(BusinessException.class, () -> auction.bid("bidder",10, 2));
        assertTrue(exception.getMessage().contains("price"));
    }

    @Test
    @DisplayName("should throw an exception when the bidding quantity is below 1 or higher than the available quantity")
    public void shouldThrowIfQuantityIsInvalid() {
        var auction = new Auction(1, "owner", "AAPL", 20.0, 5);

        var lowException = assertThrows(BusinessException.class, () -> auction.bid("bidder",22, 0));
        assertTrue(lowException.getMessage().contains("low"));

        var highException = assertThrows(BusinessException.class, () -> auction.bid("bidder",22, 10));
        assertTrue(highException.getMessage().contains("exceeds"));
    }

    @Test
    @DisplayName("should be able to add a bid")
    public void shouldAddABid() {
        var auction = new Auction(1, "owner", "AAPL", 20.0, 5);

        auction.bid("bidder1",22, 3);
        auction.bid("bidder2",23.2, 2);
        auction.bid("bidder3",23.5, 2);

        var currentBids = auction.getBidList();

        assertEquals(currentBids.get(0).owner(), "bidder1");
        assertEquals(currentBids.get(0).price(),22);

        assertEquals(currentBids.get(1).owner(), "bidder2");
        assertEquals(currentBids.get(1).quantity(), 2);

        assertEquals(currentBids.get(2).owner(), "bidder3");
        assertEquals(currentBids.get(2).quantity(), 2);
    }

    @Test
    @DisplayName("orderBidList should sort the existing bids by descending price order")
    public void shouldOrderBidListByPrice() {
        var auction = new Auction(1, "username", "AAPL", 20.5, 5);
        auction.bid("username2",22, 2);
        auction.bid("username3",24, 3);
        auction.bid("username4",21, 3);
        auction.bid("username5",26, 3);

        var orderedList = auction.orderBidList();

        assertEquals(orderedList.get(0).owner(), "username5");
    }

    @Test
    @DisplayName("orderBidList should sort the existing bids by earliest execution date when price is matched")
    public void shouldOrderBidListByDate() {
        var auction = new Auction(1, "username", "AAPL", 20.5, 5);
        auction.bid("username2",22, 2);
        auction.bid("username3",26, 3);
        auction.bid("username4",21, 3);
        auction.bid("username5",26, 3);

        var orderedList = auction.orderBidList();

        assertEquals(orderedList.get(0).owner(), "username3");
    }

    // Close auction tests
    @Test
    @DisplayName("should throw an exception when trying to close an already closed auction")
    public void shouldThrowWhenClosingClosedAuction() {
        var auction = new Auction(1, "owner", "AAPL", 20.0, 5);
        auction.closeAuction();

        var exception = assertThrows(BusinessException.class, auction::closeAuction);
        assertTrue(exception.getMessage().contains("already closed"));
    }

    @Test
    @DisplayName("should calculate who are the winners when closing")
    public void shouldCalculateTheWinnersOnClose() {
        var auction = new Auction(1, "owner", "AAPL", 20.0, 5);

        auction.bid("bidder1",22, 3); // Should win 1
        auction.bid("bidder2",23.5, 2); // Should win all
        auction.bid("bidder3",23.2, 2); // Should win all
        auction.bid("bidder4",21, 2); // Should lose

        auction.closeAuction();

        var summary = auction.getAuctionSummary();

        assertEquals(summary.soldQty(), 5); // Sold all of them
        assertEquals(summary.remainingQty(), 0); // No remaining lots
        assertEquals(summary.totalRevenue(), BigDecimal.valueOf((23.5 * 2) + (23.2 * 2) + 22));

        var winningBids = summary.winningBids();

        assertEquals(winningBids.toArray().length, 3);
        // First bid closed
        assertEquals(winningBids.get(0).bidder(), "bidder2");
        // Second bid closed
        assertEquals(winningBids.get(1).bidder(), "bidder3");
    }

    @Test
    @DisplayName("should close an auction partially")
    public void shouldCloseAuctionPartially() {
        var auction = new Auction(1, "owner", "AAPL", 20.0, 5);

        auction.bid("bidder1",22, 1);
        auction.bid("bidder2",23.5, 2);

        auction.closeAuction();

        var summary = auction.getAuctionSummary();

        assertEquals(summary.soldQty(), 3); // Sold all of them
        assertEquals(summary.remainingQty(), 2); // No remaining lots
        assertEquals(summary.totalRevenue(), BigDecimal.valueOf((23.5 * 2) + 22));
    }

    @Test
    @DisplayName("should show correct lost bids")
    public void shouldShowLostBids() {
        var auction = new Auction(1, "owner", "AAPL", 20.0, 2);

        auction.bid("bidder1",21, 2); // Lose
        auction.bid("bidder1",22, 1); // Win
        auction.bid("bidder2",23.5, 1); // Win

        auction.closeAuction();

        var lostBidsBidder1 = auction.getLostBids("bidder1").toList();
        var lostBidsBidder2 = auction.getLostBids("bidder2").toList();

        assertEquals(lostBidsBidder2.toArray().length, 0); // No lost bids - won all of them

        assertEquals(lostBidsBidder1.toArray().length, 1); // One lost bid
        assertEquals(lostBidsBidder1.get(0).AuctionId(), auction.getId());
        assertEquals(lostBidsBidder1.get(0).pricePerLot(), 21);
        assertEquals(lostBidsBidder1.get(0).quantityLeft(), 2);
    }
}
