package com.weareadaptive.auctionhouse.model;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

// TODO: Can I use the same state class to generate an auction state?
// First I'd need an Auction object, like the User
public class State<T extends Model> {
  public static final String ITEM_ALREADY_EXISTS = "Item already exists";
  private final Map<Integer, T> models;
  private int currentId = 0;

  public State() {
    models = new HashMap<>();
  }

  public int nextId() {
    return currentId++;
  }

  protected void onAdd(T model) {

  }

  // Tries to add a value to the model (model = hashmap that contains our state)
  public void add(T model) {
    if (models.containsKey(model.getId())) {
      throw new BusinessException(ITEM_ALREADY_EXISTS);
    }
    onAdd(model);
    models.put(model.getId(), model);
  }

  void setNextId(int id) {
    this.currentId = id;
  }

  // Gets a specific value by its id
  public T get(int id) {
    return models.get(id);
  }

  // Ah it's a method - it returns all the values in the model in the form of a stream
  public Stream<T> stream() {
    return models.values().stream();
  }
}
