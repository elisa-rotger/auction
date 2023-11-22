package com.weareadaptive.auctionhouse.console;

import java.util.Scanner;
import java.util.stream.Stream;

import com.weareadaptive.auctionhouse.model.*;


public class ConsoleAuction {
  private final MenuContext menuContext;

  public ConsoleAuction() {
    var state = new ModelState(new UserState(), new AuctionState());
    initData(state);
    var scanner = new Scanner(System.in);
    menuContext = new MenuContext(state, scanner, System.out);
  }

  private void initData(ModelState state) {
    // Initializes the program with some users
    Stream.of(
        new User(state.userState().nextId(), "admin", "admin", "admin", "admin", "admin", true),
        new User(state.userState().nextId(), "jf", "mypassword", "JF", "Legault", "Org 1"),
        new User(state.userState().nextId(), "thedude", "biglebowski", "Walter", "Sobchak", "Org 2")
    ).forEach(u -> state.userState().add(u));

    // Initializes the program with some auctions
    Stream.of(
            new Auction(state.userState().nextId(), "admin", "AAPL", 20.2, 5),
            new Auction(state.userState().nextId(), "jf", "TESLA", 43.6, 10),
            new Auction(state.userState().nextId(), "thedude", "AAPL", 21.0, 2)
    ).forEach(u -> state.auctionState().add(u));
  }

  public void start() {
    LoginMenu loginMenu = new LoginMenu();
    loginMenu.display(menuContext);
  }

}
