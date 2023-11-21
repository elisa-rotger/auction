package com.weareadaptive.auctionhouse.model;

import static java.lang.String.format;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserState extends State<User> {

  private final Map<String, User> usernameIndex;

  public UserState() {
    usernameIndex = new HashMap<>();
  }


  // Find specific user by its username
  public Optional<User> findUserByUsernameAndPassword(String username, String password) {
    return stream()
        .filter(user -> user.getUsername().equalsIgnoreCase(username))
        .filter(user -> user.validatePassword(password))
        .findFirst();
  }

  public boolean doesUsernameExist(String username) {
    return stream()
            .map(User::getUsername)
            // Used to have filter by username -> findFirst -> isPresent but the IDE is smarter than me
            .anyMatch(userUsername -> userUsername.equalsIgnoreCase(username));
  }

  public Optional<User> findUserById(int userId) {
    return stream().filter(user -> user.getId() == userId).findFirst();
  }

}
