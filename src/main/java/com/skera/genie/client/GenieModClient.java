package com.skera.genie.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import com.skera.genie.GenieMod;
import com.skera.genie.screen.GenieScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.skera.genie.network.ModPackets;

@Environment(EnvType.CLIENT)
public class GenieModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		GenieMod.LOGGER.info("Initializing Genie Mod Client!");
		
		// Register screen handler
		ClientPlayNetworking.registerGlobalReceiver(ModPackets.OPEN_GENIE_SCREEN, (client, handler, buf, responseSender) -> {
			client.execute(() -> {
				var screen = new GenieScreen();
				client.setScreen(screen);
			});
		});
	}
}
