package com.shoppinglistbot.shoppinglistbot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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

        StringBuilder reply = new StringBuilder();
        reply.append("Item adicionado: ").append(itemName).append(" Quantidade: ");

        Optional<Long> itemQuantity = e.getOption("qtd")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asLong);

        if (itemQuantity.isPresent()) {
            reply.append(itemQuantity.get());
        } else {
            int MIN_ITEM_QUANTITY = 1;
            reply.append(MIN_ITEM_QUANTITY);
        }

        return e.reply().withContent(reply.toString());
    }
}
