package com.skera.genie.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
		// Загрузка желаний из JSON файлов в resources/data/genie/wishes/
		// В реальной реализации нужно использовать ResourceManager
	}
	
	public static Wish getWish(String id) {
		return WISHES.get(id);
	}
	
	public static void registerWish(String id, Wish wish) {
		WISHES.put(id, wish);
	}
	
	public static class Wish {
		public String id;
		public String category;
		public String displayName;
		public float baseRisk;
		public EffectData effect;
		public EffectData trickyEffect;
		
		public Wish(String id, String category, String displayName, float baseRisk, EffectData effect, EffectData trickyEffect) {
			this.id = id;
			this.category = category;
			this.displayName = displayName;
			this.baseRisk = baseRisk;
			this.effect = effect;
			this.trickyEffect = trickyEffect;
		}
	}
	
	public static class EffectData {
		public String type; // "item", "effect", "teleport", "spawn_mob"
		public String itemId;
		public int count;
		public int effectId;
		public int duration;
		public int amplifier;
		public double x, y, z;
		public String mobId;
		
		public static EffectData item(String itemId, int count) {
			EffectData data = new EffectData();
			data.type = "item";
			data.itemId = itemId;
			data.count = count;
			return data;
		}
		
		public static EffectData effect(int effectId, int duration, int amplifier) {
			EffectData data = new EffectData();
			data.type = "effect";
			data.effectId = effectId;
			data.duration = duration;
			data.amplifier = amplifier;
			return data;
		}
		
		public static EffectData teleport(double x, double y, double z) {
			EffectData data = new EffectData();
			data.type = "teleport";
			data.x = x;
			data.y = y;
			data.z = z;
			return data;
		}
		
		public static EffectData spawnMob(String mobId) {
			EffectData data = new EffectData();
			data.type = "spawn_mob";
			data.mobId = mobId;
			return data;
		}
	}
}
