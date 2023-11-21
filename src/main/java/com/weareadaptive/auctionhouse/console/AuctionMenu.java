package com.weareadaptive.auctionhouse.console;

public class AuctionMenu extends ConsoleMenu {
    @Override
    public void display(MenuContext context) {
        createMenu(
                context,
                // TODO: Auction functionality
                option("Create new auction", this::testFn),
                option("Close auction", this::testFn),
                option("View my auctions", this::testFn),
                option("Bid on an auction", this::testFn),
                option("View bids won", this::testFn),
                option("View bids lost", this::testFn),
                leave("Go Back")
        );
    }

    private void testFn(MenuContext context) {
        context.getOut().println("Test function");
    }
}
