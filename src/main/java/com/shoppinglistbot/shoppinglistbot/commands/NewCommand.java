package com.shoppinglistbot.shoppinglistbot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import reactor.core.publisher.Mono;

@Component
public class NewCommand implements SlashCommand{

    @Override
    public String getName() {
        return "new";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent e) {
        Logger LOGGER = LoggerFactory.getLogger(this.getClass());

        Long channelId = e.getInteraction().getChannelId().asLong();

        RestClient httpClient = RestClient.create();

        ResponseEntity<String> result = httpClient.post()
                .uri("http://localhost:8081/api/v1/shopping-list/{channelId}", channelId)
                .retrieve()
                .toEntity(String.class);


        String replyContent = "";
        if (result.getStatusCode().equals(HttpStatus.CREATED)) {
            replyContent = "Lista criada!";
        } else {
            replyContent = "Falha ao criar lista";
            LOGGER.error(result.getStatusCode().toString());
        }

        return e.deferReply().then(e.editReply(replyContent)).then();
    }

}
