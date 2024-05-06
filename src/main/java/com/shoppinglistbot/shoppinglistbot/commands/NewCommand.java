package com.shoppinglistbot.shoppinglistbot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

public class NewCommand implements SlashCommand{

    @Override
    public String getName() {
        return "new";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent e) {
        return null;
    }
}
