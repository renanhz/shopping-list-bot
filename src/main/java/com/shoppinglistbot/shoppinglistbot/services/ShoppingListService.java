package com.shoppinglistbot.shoppinglistbot.services;

import com.shoppinglistbot.shoppinglistbot.model.Item;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Service
public class ShoppingListService {

    private RestClient httpClient;
    private final String baseUrl = "http://localhost:8081/api/v1/";

    public ShoppingListService() {
        this.httpClient = RestClient.create();
    }

    public List<Item> getItems(Long channelId) {
        String finalUrl = baseUrl + "shopping-list/{channelId}";

        Item[] itemArray = httpClient.get()
                .uri(finalUrl, channelId)
                .retrieve()
                .toEntity(Item[].class)
                .getBody();

        return Arrays.asList(itemArray);
    }
}
