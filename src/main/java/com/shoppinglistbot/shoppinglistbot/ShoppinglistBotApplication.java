package com.shoppinglistbot.shoppinglistbot;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ShoppinglistBotApplication {

	@Value("${token}") String token;

	@Bean
	public GatewayDiscordClient gatewayDiscordClient() {
		return DiscordClient.create(token).login().block();
	}

	public static void main(String[] args) {
		SpringApplication.run(ShoppinglistBotApplication.class, args);
	}

}
