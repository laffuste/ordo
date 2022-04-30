package com.laffuste.ordo.domain;

import lombok.Value;

@Value
public class Order {
    String id;
    int quantity;
    double price;
    boolean isBuy; // an enum would be nicer but we save
    OrderType type;
}
