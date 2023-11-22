package com.weareadaptive.auctionhouse.model;

import static com.weareadaptive.auctionhouse.TestData.ADMIN;
import static com.weareadaptive.auctionhouse.TestData.ORG_1;
import static com.weareadaptive.auctionhouse.TestData.ORG_2;
import static com.weareadaptive.auctionhouse.TestData.USER1;
import static com.weareadaptive.auctionhouse.TestData.USER2;
import static com.weareadaptive.auctionhouse.TestData.USER3;
import static com.weareadaptive.auctionhouse.TestData.USER4;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserStateTest {
  private UserState state;

  @BeforeEach
  public void initState() {
    state = new UserState();
    Stream.of(
        ADMIN,
        USER1,
        USER2,
        USER3,
        USER4
    ).forEach(u -> state.add(u));
    state.setNextId(USER4.getId());
  }

  // TODO: Add tests
  // TODO: Add auction / auction state test

  @Test
  @DisplayName("findUserByUsernameAndPassword should return the correct user")
  public void shouldFindUserByUsernameAndPassword() {
    var username = ADMIN.getUsername();
    var password = "admin";

    // Returns correct username
    var user = state.findUserByUsernameAndPassword(username, password);
    assertNotNull(user);
    assertEquals(user.hashCode(), ADMIN.hashCode());

    // Returns empty optional for non-existing username
    var nonExistingUser = state.findUserByUsernameAndPassword("nonExistingUser", password);
    assert(nonExistingUser).isEmpty();

    // Returns empty optional for username with the wrong password
    var wrongPassword = state.findUserByUsernameAndPassword(username, "wrongPassword");
    assert(wrongPassword).isEmpty();
  }

  @Test
  @DisplayName("doesUsernameExist should find the right user")
  public void shouldReturnIfUsernameExists() {
    // Returns true when searching for existing username
    var existingUsername = state.doesUsernameExist(ADMIN.getUsername());
    assertTrue(existingUsername);

    // Returns false when searching for non-existing user
    var nonExistingUser = state.doesUsernameExist("nonExistingUser");
    assertFalse(nonExistingUser);

    // Returns true when searching for existing username (case-insensitive)
    var caseInsensitive = state.doesUsernameExist(ADMIN.getUsername().toUpperCase());
    assertTrue(caseInsensitive);
  }

}
