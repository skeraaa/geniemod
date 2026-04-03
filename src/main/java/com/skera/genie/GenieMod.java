package com.skera.genie;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.skera.genie.item.ModItems;
import com.skera.genie.network.WishHandler;
import com.skera.genie.config.WishConfig;

public class GenieMod implements ModInitializer {
	public static final String MOD_ID = "genie";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Genie Mod initialized!");
		ModItems.registerItems();
		WishHandler.registerPackets();
		WishConfig.loadWishes(); // Загрузка JSON при старте
	}
}
