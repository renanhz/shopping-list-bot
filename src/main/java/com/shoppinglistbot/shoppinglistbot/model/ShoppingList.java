package com.shoppinglistbot.shoppinglistbot.model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingList {

    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public ShoppingList() {
        items = new ArrayList<>();
    }
}
