package com.skera.genie.network;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import com.skera.genie.config.WishConfig;
import com.skera.genie.item.ModItems;
import com.skera.genie.item.LampItem;

public class WishHandler {

	public static void registerPackets() {
		// Регистрация рецепта приема пакетов (Fabric API)
		// ServerPlayNetworking.registerGlobalReceiver(WISH_PACKET_ID, (server, player, handler, buf, responseSender) -> { ... });
	}

	public static void executeWish(PlayerEntity player, String wishId, boolean isTricky) {
		WishConfig.Wish wish = WishConfig.getWish(wishId);
		if (wish == null) return;

		WishConfig.EffectData effectData = isTricky ? wish.trickyEffect : wish.effect;
		if (effectData == null) return;

		player.sendMessage(Text.literal("§6Джин исполняет желание... " + (isTricky ? "§c(С подковыркой!)" : "§a(Чисто!)")), true);

		switch (effectData.type) {
			case "item":
				giveItem(player, effectData.itemId, effectData.count, effectData.customName);
				break;
			case "effect":
				applyEffect(player, effectData.effectId, effectData.duration, effectData.amplifier);
				break;
			case "vampire":
				applyVampirism(player, effectData.data);
				break;
			case "fake_block":
				giveFakeBlock(player, effectData.itemId, effectData.count);
				break;
			case "flight":
				player.getAbilities().allowFlying = true;
				player.getAbilities().flySpeed = 0.1f;
				player.sendAbilitiesUpdate();
				player.sendMessage(Text.literal("§bТеперь ты можешь летать!"), true);
				break;
			case "god_mode":
				player.sendMessage(Text.literal("§d«Отныне вы – божество!.. Но увы...»"), true);
				giveItem(player, "genie:genie_lamp", 1, "Золотая Лампа (Божественная)");
				break;
		}
	}

	private static void giveItem(PlayerEntity player, String itemId, int count, String customName) {
		Identifier id = new Identifier(itemId);
		net.minecraft.item.Item item = Registries.ITEM.get(id);
		ItemStack stack;
		
		if (item == Items.AIR) {
			stack = new ItemStack(Items.STICK); // Заглушка
			if (customName != null) stack.setCustomName(Text.literal(customName));
		} else {
			stack = new ItemStack(item, count);
			if (customName != null) stack.setCustomName(Text.literal(customName));
		}
		player.giveItemStack(stack);
	}
	
	private static void giveFakeBlock(PlayerEntity player, String itemId, int count) {
		// Даем палку с именем "Фейковый блок" или специальный предмет
		ItemStack stack = new ItemStack(ModItems.FAKE_DIAMOND_BLOCK, count);
		stack.setCustomName(Text.literal("§cФейковый " + itemId.split(":")[1]));
		player.giveItemStack(stack);
	}

	private static void applyEffect(PlayerEntity player, String effectId, int duration, int amplifier) {
		Identifier id = new Identifier(effectId);
		var effect = Registries.STATUS_EFFECT.get(id);
		if (effect != null && effect != StatusEffects.EMPTY) {
			player.addStatusEffect(new StatusEffectInstance(effect, duration, amplifier));
		}
	}

	private static void applyVampirism(PlayerEntity player, com.google.gson.JsonObject data) {
		// Логика превращения в вампира
		NbtCompound nbt = player.getPersistentData();
		nbt.putBoolean("isVampire", true);
		nbt.putInt("vampireRank", 1); // Низший
		nbt.putLong("bloodLevel", 0);
		
		player.sendMessage(Text.literal("§4Теперь ты — НИЗШИЙ ВАМПИР! Избегай солнца!"), true);
		// Снимаем старые эффекты, добавляем базовые
		player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 999999, 0));
		player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 999999, 0));
		player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 999999, 0));
	}
}
