package com.shoppinglistbot.shoppinglistbot.commands;

import com.shoppinglistbot.shoppinglistbot.model.Item;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
public class AddCommand implements SlashCommand{
    @Override
    public String getName() {
        return "add";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent e) {
        Logger LOGGER = LoggerFactory.getLogger(this.getClass());
        LOGGER.info("Chamada realizada");

        String itemName = e.getOption("item")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .get();

        String itemQuantity = e.getOption("qtd")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse("1");

        Item item = new Item(itemName, itemQuantity);

        Long channelId = e.getInteraction().getChannelId().asLong();

        String reply = RestClient.create().post()
                .uri("http://localhost:8081/api/v1/item/{channelId}", channelId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(item)
                .exchange((request, response) -> {
                    if (response.getStatusCode().is4xxClientError()) {
                        return "É preciso criar uma lista antes de adicionar um item, use o comando /new";
                    } else {
                        return "Item adicionado: " + itemName + ", Quantidade: " + itemQuantity;
                    }
                });


        return e.deferReply().then(e.editReply(reply).then());
    }
}
