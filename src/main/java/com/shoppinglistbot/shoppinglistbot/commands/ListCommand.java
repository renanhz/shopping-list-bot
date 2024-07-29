package com.shoppinglistbot.shoppinglistbot.commands;

import com.shoppinglistbot.shoppinglistbot.model.Item;
import com.shoppinglistbot.shoppinglistbot.services.ShoppingListService;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ListCommand implements SlashCommand{

    @Autowired
    private ShoppingListService shoppingListService;

    public ListCommand(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent e) {
        RestClient httpClient = RestClient.create();
        Long channelId = e.getInteraction().getChannelId().asLong();

        List<Item> items = shoppingListService.getItems(channelId);

        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .color(Color.YELLOW)
                .title("Lista de compras")
                .build();

        if (items.isEmpty()) {
            embed = embed.withDescription("A lista está vazia");
        } else {
            embed = embed.withDescription("Items da Lista: ");

            List<EmbedCreateFields.Field> fields = new ArrayList<>();
            for (Item item:items) {
                fields.add(createField(item));
            }
            embed = embed.withFields(fields);
        }

        return e.deferReply().then(e.editReply().withEmbeds(embed).then());
    }

    public EmbedCreateFields.Field createField(Item item) {
        return new EmbedCreateFields.Field() {
            @Override
            public String name() {
                return item.name();
            }

            @Override
            public String value() {
                return item.quantity();
            }

            @Override
            public boolean inline() {
                return false;
            }
        };
    }
}
