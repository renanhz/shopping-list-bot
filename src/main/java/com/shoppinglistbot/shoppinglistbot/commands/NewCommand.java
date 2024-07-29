package com.shoppinglistbot.shoppinglistbot.commands;

import com.shoppinglistbot.shoppinglistbot.services.ShoppingListService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import reactor.core.publisher.Mono;

@Component
public class NewCommand implements SlashCommand{

    @Autowired
    private ShoppingListService shoppingListService;

    public NewCommand(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @Override
    public String getName() {
        return "new";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent e) {
        Logger LOGGER = LoggerFactory.getLogger(this.getClass());

        Long channelId = e.getInteraction().getChannelId().asLong();

        RestClient httpClient = RestClient.create();

        String reply = shoppingListService.createList(channelId);

        return e.deferReply().then(e.editReply(reply)).then();
    }

}
