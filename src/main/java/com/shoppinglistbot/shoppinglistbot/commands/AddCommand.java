package com.shoppinglistbot.shoppinglistbot.commands;

import com.shoppinglistbot.shoppinglistbot.model.Item;
import com.shoppinglistbot.shoppinglistbot.services.ShoppingListService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ShoppingListService shoppingListService;

    public AddCommand(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

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

        String reply = shoppingListService.addItem(channelId, item);

        return e.deferReply().then(e.editReply(reply).then());
    }
}
