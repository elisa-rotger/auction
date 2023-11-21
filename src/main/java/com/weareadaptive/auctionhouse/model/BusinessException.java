package com.weareadaptive.auctionhouse.model;

// I assume this is going to be used for error catching?
public class BusinessException extends RuntimeException {
  public BusinessException(String message) {
    super(message);
  }
}
