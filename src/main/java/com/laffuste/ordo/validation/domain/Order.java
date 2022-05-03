package com.laffuste.ordo.validation.domain;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    public static final String TYPE_MARKET = "MARKET";
    public static final String TYPE_LIMIT = "LIMIT";

    String id;
    int quantity;
    double price;
    boolean isBuy; // an enum would be nicer but this is cheaper
    String type;

    public double calculateNotional() {
        return quantity * price;
    }

    public boolean isMarketOrder() {
        return TYPE_MARKET.equals(type);
    }

    public boolean isLimitOrder() {
        return TYPE_LIMIT.equals(type);
    }

}
