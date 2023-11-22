package com.weareadaptive.auctionhouse.console;

import com.weareadaptive.auctionhouse.model.Auction;
import com.weareadaptive.auctionhouse.model.BusinessException;

import java.util.Arrays;

public class AuctionMenu extends ConsoleMenu {
    @Override
    public void display(MenuContext context) {
        createMenu(
                context,
                // TODO: Auction functionality
                option("Create new auction", this::createAuction),
                option("Close auction", this::testFn),
                option("View my auctions", this::listOwnAuctions),
                option("Bid on an auction", this::testFn),
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
            out.println("Press enter to continue...");
            scanner.nextLine();
        } catch(BusinessException businessException) {
            out.println("Cannot create auction.");
            out.println(businessException.getMessage());
        }

    }

    private void listOwnAuctions(MenuContext context) {
        var out = context.getOut();
        var scanner = context.getScanner();
        var state = context.getState();

        var owner = context.getCurrentUser().getUsername();

        // TODO: Add list of bids, if available
        Arrays.stream(state.auctionState().findAuctionsByOwner(owner))
                .forEach(a -> out.printf(
                    "Symbol: %s, Minimum price: %s, Available quantity: %s || %s %n",
                    a.getSymbol(),
                    a.getMinPrice(),
                    a.getAvailableQty(),
                    a.getIsOpen() ? "(Open)" : "(Closed)")
                );


        // TODO: Add message when no auctions have been created

        out.println("Press enter to continue...");
        scanner.nextLine();
    }
}
