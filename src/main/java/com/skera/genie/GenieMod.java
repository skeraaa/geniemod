package com.skera.genie;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenieMod implements ModInitializer {
	public static final String MOD_ID = "genie";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Genie Mod!");
		
		// Register items
		ModItems.register();
		
		// Register network packets
		ModPackets.register();
	}
}
