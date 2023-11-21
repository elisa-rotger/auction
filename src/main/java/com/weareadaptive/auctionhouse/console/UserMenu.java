package com.weareadaptive.auctionhouse.console;

// TODO: Add user management options
public class UserMenu extends ConsoleMenu {

    @Override
    public void display(MenuContext context) {
        createMenu(
                context,
                option("Create new user", this::testFn),
                option("Get all users", this::getAllUsers),
                option("Get all organisations", this::testFn),
                option("Get user details", this::testFn),
                option("Block / unblock user accounts", this::testFn),
                // Allow logout from menu
                leave("Log out")
                );
    }

    private void testFn (MenuContext context) {
        var out = context.getOut();
        out.println("Test function");
    }

    // TODO: Create user

    private void getAllUsers(MenuContext context) {
        var out = context.getOut();
        out.println("All users ---------->");

        // TODO: Improve UI+
        // Investigate - what the hell is "map" and why can't I use that
        context.getState().userState().stream().forEach(user -> out.println(user.getFirstName()));

        out.println("===================================");
        // TODO: Investigate how to make user press a key to continue or something
    }

    // TODO: Get all organisations
    // TODO: Get user details
    // TODO: Block / unblock user
}
