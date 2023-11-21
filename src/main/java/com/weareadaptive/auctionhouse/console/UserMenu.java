package com.weareadaptive.auctionhouse.console;

import com.weareadaptive.auctionhouse.StringUtil;
import com.weareadaptive.auctionhouse.model.BusinessException;
import com.weareadaptive.auctionhouse.model.User;

// TODO: Add user management options
public class UserMenu extends ConsoleMenu {

    @Override
    public void display(MenuContext context) {
        createMenu(
                context,
                option("Create new user", this::createUser),
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
    private void createUser(MenuContext context) {
        var out = context.getOut();
        var scanner = context.getScanner();
        var state = context.getState();
        // The username must be an alphanumerical string without spaces or special characters.

        out.println("Enter the username:");
        final var username = scanner.nextLine();

        var usernameExists = state.userState().doesUsernameExist(username);

        if (usernameExists) {
            out.println("Username already exists");
            // TODO: Investigate if I can make the user just go back to the username input
            return;
        }

        if (!StringUtil.isAlphanumeric(username)){
            out.printf("Invalid username format: %s. %nCan only contain alphanumeric characters and no spaces. %n", username);
            // TODO: Investigate if I can make the user just go back to the username input
            return;
        }

        out.println("Enter the password:");
        var password = readPassword(scanner);

        // Enter password a second time
        out.println("Repeat the password:");
        var secondPassword = readPassword(scanner);

        if (!password.equals(secondPassword)) {
            out.println("Password has to be the same");
            // TODO: Investigate if I can make the user just go back to the password input
            return;
        }

        out.println("Enter the first name:");
        var firstName = scanner.nextLine();

        out.println("Enter the last name:");
        var lastName = scanner.nextLine();

        out.println("Enter the organization:");
        var organization = scanner.nextLine();

        var newUser = new User(
                state.userState().nextId(),
                username,
                password,
                firstName,
                lastName,
                organization
        );

        // How to catch if something goes wrong in the creation?
        // It's breaking right now

        try {
            state.userState().add(newUser);
        } catch(BusinessException businessException) {
            out.println("Cannot create user.");
            out.println(businessException.getMessage());
        }
    }

    private void getAllUsers(MenuContext context) {
        var out = context.getOut();
        var scanner = context.getScanner();
        out.println("All users ---------->");

        // How to print multiple things for each user - printf? (print stream?)
        // How should this be formatted
        // It kinda looks like rxjs
        context.getState()
                .userState()
                .stream()
                .forEach(user ->
                        out.printf("Username: %s, Firstname: %s, Lastname: %s, Organisation: %s%n",
                                user.getUsername(),
                                user.getFirstName(),
                                user.getLastName(),
                                user.getOrganisation()));

        out.println("Press enter to continue...");
        scanner.nextLine();
    }

    // TODO: Get all organisations
    // TODO: Get user details
    // TODO: Block / unblock user
}
