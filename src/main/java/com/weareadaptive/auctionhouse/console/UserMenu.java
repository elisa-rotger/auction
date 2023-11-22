package com.weareadaptive.auctionhouse.console;

import com.weareadaptive.auctionhouse.StringUtil;
import com.weareadaptive.auctionhouse.model.Auction;
import com.weareadaptive.auctionhouse.model.BusinessException;
import com.weareadaptive.auctionhouse.model.User;

import java.sql.Array;

public class UserMenu extends ConsoleMenu {

    @Override
    public void display(MenuContext context) {
        createMenu(
                context,
                option("Create new user", this::createUser),
                option("Get all users", this::getAllUsers),
                option("Get all organisations", this::getAllOrganisations),
                option("Get user details", this::getUserDetails),
                option("Block / unblock user accounts", this::blockUser),
                leave("Go Back")
                );
    }

    private void createUser(MenuContext context) {
        var out = context.getOut();
        var scanner = context.getScanner();
        var state = context.getState();

        // Why does it not blow up if I wrap the whole thing with the try / catch, but it does if I only wrap the add?
        try {
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

        // TODO: How should this be formatted?
        // It kinda looks like rxjs, that's fun
        context.getState()
                .userState()
                .stream()
                .forEach(user ->
                        out.printf("Username: %s, Firstname: %s, Lastname: %s, Organisation: %s%n",
                                user.getUsername(),
                                user.getFirstName(),
                                user.getLastName(),
                                user.getOrganisation()
                        )
                );

        pressEnter(context);
    }

    private void getAllOrganisations(MenuContext context) {
        var out = context.getOut();
        out.println("Organisations ---------->");

        context.getState()
                .userState()
                .stream()
                .map(User::getOrganisation)
                // Remove duplicate organisations
                .distinct()
                .forEach(out::println);

        pressEnter(context);
    }

    private void getUserDetails(MenuContext context) {
        var out = context.getOut();

        var userOptions = context.getState()
                .userState()
                .stream()
                .map(u ->
                        option(u.getUsername(), () -> out.printf(
                                "Username: %s, %nFirst Name: %s, %nLast Name: %s, %nOrganisation: %s %n",
                                u.getUsername(),
                                u.getFirstName(),
                                u.getLastName(),
                                u.getOrganisation()
                        )))
                .toArray(MenuOption[]::new);

        var allOptions = append(userOptions, leave("Go Back"));

        createMenu(
                context,
                allOptions
        );

        pressEnter(context);
    }

    // TODO: Block / unblock user
    private void blockUser (MenuContext context) {
        var out = context.getOut();
        out.println("Test function");
    }
}
