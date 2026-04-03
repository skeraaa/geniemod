package com.skera.genie.config;

import com.google.gson.*;
import net.minecraft.util.Identifier;
import com.skera.genie.GenieMod;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class WishConfig {
	private static final Map<String, Wish> WISHES = new HashMap<>();
	private static final Gson GSON = new Gson();

	public static void loadWishes() {
		// Упрощенная загрузка для примера. В продакшене использовать ResourceManager
		try {
			InputStream stream = GenieMod.class.getClassLoader().getResourceAsStream("data/genie/wishes/wishes.json");
			if (stream != null) {
				JsonObject json = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();
				for (String key : json.keySet()) {
					Wish wish = GSON.fromJson(json.get(key), Wish.class);
					wish.id = key;
					WISHES.put(key, wish);
				}
				GenieMod.LOGGER.info("Loaded {} wishes.", WISHES.size());
			}
		} catch (Exception e) {
			GenieMod.LOGGER.error("Failed to load wishes!", e);
		}
	}

	public static Wish getWish(String id) {
		return WISHES.get(id);
	}

	public static class Wish {
		public String category;
		public String displayName;
		public float baseRisk;
		public EffectData effect;
		public EffectData trickyEffect;
	}

	public static class EffectData {
		public String type; // item, effect, vampire, fake_block, custom_item, flight, god_mode
		public String itemId;
		public int count;
		public String effectId; // Используем строковый ID (minecraft:speed)
		public int duration; // в тиках
		public int amplifier;
		public String customName;
		public JsonObject data; // Доп данные для сложных эффектов
	}
}
