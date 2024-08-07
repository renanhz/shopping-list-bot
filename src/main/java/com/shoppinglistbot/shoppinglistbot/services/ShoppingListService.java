package com.shoppinglistbot.shoppinglistbot.services;

import com.shoppinglistbot.shoppinglistbot.model.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Service
public class ShoppingListService {

    private RestClient httpClient;
    private final String baseUrl = "http://localhost:8081/api/v1/";

    public ShoppingListService() {this.httpClient = RestClient.create();}

    public List<Item> getItems(Long channelId) {
        String finalUrl = baseUrl + "shopping-list/{channelId}";

        Item[] itemArray = httpClient.get()
                .uri(finalUrl, channelId)
                .retrieve()
                .toEntity(Item[].class)
                .getBody();

        return Arrays.asList(itemArray);
    }

    public String addItem(Long channelId, Item item) {
        String reply = httpClient.post()
                .uri("http://localhost:8081/api/v1/item/{channelId}", channelId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(item)
                .exchange((request, response) -> {
                    if (response.getStatusCode().is4xxClientError()) {
                        return "É preciso criar uma lista antes de adicionar um item, use o comando /new";
                    } else {
                        return "Item adicionado: " + item.name() + ", Quantidade: " + item.quantity();
                    }
                });

        return reply;
    }

    public String createList(Long channelId) {
        Logger LOGGER = LoggerFactory.getLogger(this.getClass());

        ResponseEntity<String> result = httpClient.post()
                .uri("http://localhost:8081/api/v1/shopping-list/{channelId}", channelId)
                .retrieve()
                .toEntity(String.class);

        String reply = "";
        if (result.getStatusCode().equals(HttpStatus.CREATED)) {
            reply = "Lista criada!";
        } else {
            reply = "Falha ao criar lista";
            LOGGER.error(result.getStatusCode().toString());
        }

        return reply;
    }
}
