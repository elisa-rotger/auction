package com.weareadaptive.auctionhouse.console;

import com.weareadaptive.auctionhouse.model.Auction;
import com.weareadaptive.auctionhouse.model.Bid;
import com.weareadaptive.auctionhouse.model.BusinessException;

import java.util.ArrayList;
import java.util.Arrays;

public class AuctionMenu extends ConsoleMenu {
    @Override
    public void display(MenuContext context) {
        createMenu(
                context,
                // TODO: Auction functionality
                option("Create new auction", this::createAuction),
                option("Close auction", this::closeAuction),
                option("View my auctions", this::listOwnAuctions),
                option("Bid on an auction", this::createBid),
                option("View bids won", this::testFn),
                option("View bids lost", this::testFn),
                leave("Go Back")
        );
    }

    private void testFn(MenuContext context) {
        context.getOut().println("Test function");
    }

    private void createAuction(MenuContext context) {
        var out = context.getOut();
        var scanner = context.getScanner();
        var state = context.getState();

        try {
            var owner = context.getCurrentUser().getUsername();

            out.println("Enter the instrument symbol:");
            final var symbol = scanner.nextLine();

            out.println("Enter the available quantity:");
            final var availableQty = Integer.parseInt(scanner.nextLine());

            out.println("Enter the minimum price:");
            final var minPrice = Double.parseDouble(scanner.nextLine());

            var newAuction = new Auction(state.auctionState().nextId(), owner, symbol, minPrice, availableQty);

            state.auctionState().add(newAuction);

            out.printf("Auction with id %s has been added. Symbol: %s %n", newAuction.getId(), newAuction.getSymbol());
            pressEnter(context);
        } catch(BusinessException businessException) {
            out.println("Cannot create auction.");
            out.println(businessException.getMessage());
        }

    }

    private void listOwnAuctions(MenuContext context) {
        var out = context.getOut();
        var state = context.getState();

        var owner = context.getCurrentUser().getUsername();
        var auctionList = state.auctionState().findAuctionsByOwner(owner);
        var hasNoAuctions = auctionList.length == 0;

        if (hasNoAuctions) {
            out.println("User has no auctions, open or closed.");
            pressEnter(context);
            return;
        }

        Arrays.stream(auctionList)
                .forEach(a -> {
                            out.printf(
                                    "Symbol: %s, Minimum price: %s, Available quantity: %s || %s %n",
                                    a.getSymbol(),
                                    a.getMinPrice(),
                                    a.getAvailableQty(),
                                    a.getIsOpen() ? "(Open)" : "(Closed)");

                            var bidList = a.getBidList();
                            out.println("List of bids: ---------->");
                            bidList.forEach(b -> {
                                out.printf(
                                        "Quantity: %s, Price: %s, Bidder: %s %n",
                                        b.getQuantity(),
                                        b.getPrice(),
                                        b.getOwner());
                            });
                        }
                );

        pressEnter(context);
    }

    private void createBid(MenuContext context) {
        var out = context.getOut();
        var state = context.getState();
        var scanner = context.getScanner();

        var bidder = context.getCurrentUser().getUsername();
        var auctionList = state.auctionState().findOtherAuctions(bidder);
        var noAuctions = auctionList.length == 0;

        if (noAuctions) {
            out.println("No available open auctions to bid on.");
            pressEnter(context);
            return;
        }

        var auctionOptions = Arrays.stream(auctionList)
                .map(auction ->
                        option(
                                "Symbol: " + auction.getSymbol() +
                                        ", Minimum price: " + auction.getMinPrice() +
                                        ", Available quantity: " + auction.getAvailableQty()
                                , () -> {
                                    try {
                                        out.println("Input price (per lot) to bid:");
                                        final var price = Double.parseDouble(scanner.nextLine());

                                        out.println("Enter the bid quantity:");
                                        final var quantity = Integer.parseInt(scanner.nextLine());

                                        auction.bid(bidder, price, quantity);

                                        out.println("Bid created!");
                                        pressEnter(context);
                                    } catch(BusinessException businessException) {
                                        out.println("Cannot create bid.");
                                        out.println(businessException.getMessage());
                                    }
                                }))
                .toArray(MenuOption[]::new);

        var allOptions = append(auctionOptions, leave("Go Back"));

        createMenu(
                context,
                allOptions
        );
    }

    private void closeAuction(MenuContext context) {
        // TODO: Extract display menu of auctions to separate method
        var out = context.getOut();
        var state = context.getState();
        var scanner = context.getScanner();

        var owner = context.getCurrentUser().getUsername();
        var auctionList = state.auctionState().findAuctionsByOwner(owner);
        var hasNoAuctions = auctionList.length == 0;

        if (hasNoAuctions) {
            out.println("User has no auctions, open or closed.");
            pressEnter(context);
            return;
        }

        var auctionOptions = Arrays.stream(auctionList)
                .map(auction ->
                        option(
                                "Symbol: " + auction.getSymbol() +
                                        ", Minimum price: " + auction.getMinPrice() +
                                        ", Available quantity: " + auction.getAvailableQty()
                                , () -> {
                                    out.println("Closing auction...");
                                    auction.closeAuction();
                                }))
                .toArray(MenuOption[]::new);

        var allOptions = append(auctionOptions, leave("Go Back"));

        createMenu(context, allOptions);

        // Bids with best prices first
        // Can be partially closed if the bid exceeds the quantity left in the auction
        // In case of a price tie - select the bid that was done first
    }
}
