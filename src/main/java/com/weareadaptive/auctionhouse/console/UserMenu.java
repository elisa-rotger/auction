package com.weareadaptive.auctionhouse.console;

import com.weareadaptive.auctionhouse.StringUtil;
import com.weareadaptive.auctionhouse.model.BusinessException;
import com.weareadaptive.auctionhouse.model.User;

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

        try {
            out.println("Enter the username:");
            final var username = scanner.nextLine();

            var usernameExists = context.getState().userState().doesUsernameExist(username);

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
                    context.getState().userState().nextId(),
                    username,
                    password,
                    firstName,
                    lastName,
                    organization
            );
            context.getState().userState().add(newUser);
        } catch(BusinessException businessException) {
            out.println("Cannot create user.");
            out.println(businessException.getMessage());
        }
    }
    private void getAllUsers(MenuContext context) {
        context.getOut().println("All users ---------->");
        context.getState()
                .userState()
                .stream()
                .forEach(user ->
                        context.getOut().printf("Username: %s, Firstname: %s, Lastname: %s, Organisation: %s, Blocked: %s%n",
                                user.getUsername(),
                                user.getFirstName(),
                                user.getLastName(),
                                user.getOrganisation(),
                                user.getIsBlocked() ? "Yes" : "No"
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
                                "Username: %s, %nFirst Name: %s, %nLast Name: %s, %nOrganisation: %s, Blocked: %s%n",
                                u.getUsername(),
                                u.getFirstName(),
                                u.getLastName(),
                                u.getOrganisation(),
                                u.getIsBlocked() ? "Yes" : "No"
                        )))
                .toArray(MenuOption[]::new);
        createMenu(
                context,
                append(userOptions, leave("Go Back"))
        );
        pressEnter(context);
    }
    private void blockUser(MenuContext context) {
        context.getOut().println("Chose user to block or unblock ---------->");

        var userOptions = context.getState()
                .userState()
                .stream()
                .filter(u -> !u.equals(context.getCurrentUser()))
                .map(user -> option(user.getUsername() + (user.getIsBlocked() ? " (Blocked)" : ""), () -> {
                    user.toggleBlocked();
                    context.getOut().printf("User blocked: %s %n", user.getUsername());
                    pressEnter(context);
                }))
                .toArray(MenuOption[]::new);

        // TODO: Blocking a user and rendering the menu again doesn't update the 'blocked' status right away
        createMenu(
                context,
                append(userOptions, leave("Go Back"))
        );
    }
}
