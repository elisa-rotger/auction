package com.weareadaptive.auctionhouse.model;

// Java record: shorthand to have a class with immutable values (auctionState and userState) and getters
// As well as overrides to the class methods toString, equals and hashCode
public record ModelState(UserState userState, AuctionState auctionState) { }
