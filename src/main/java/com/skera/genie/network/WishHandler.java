package com.skera.genie.network;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;
import com.skera.genie.item.LampItem;
import com.skera.genie.config.WishConfig;

public class WishHandler {
	
	public static void handleWishSelection(ServerPlayerEntity player, String wishId, int lampSlot) {
		ItemStack lamp = player.getInventory().getStack(lampSlot);
		
		if (!(lamp.getItem() instanceof LampItem)) {
			return;
		}
		
		int wishesRemaining = LampItem.getWishesRemaining(lamp);
		if (wishesRemaining <= 0) {
			player.sendMessage(Text.literal("§cУ этого Джина больше нет желаний!"), true);
			return;
		}
		
		// Load wish config
		WishConfig.Wish wish = WishConfig.getWish(wishId);
		if (wish == null) {
			player.sendMessage(Text.literal("§cНеизвестное желание!"), true);
			return;
		}
		
		// Calculate tricky chance
		float playerLuck = (float) player.getAttributeValue(net.minecraft.entity.attribute.EntityAttributes.GENERIC_LUCK);
		float trickyChance = calculateTrickyChance(wish.baseRisk, playerLuck);
		
		boolean isTricky = player.getWorld().random.nextFloat() < trickyChance;
		
		// Apply main effect
		applyEffect(player, wish.effect);
		
		// Apply tricky effect if triggered
		if (isTricky && wish.trickyEffect != null) {
			applyEffect(player, wish.trickyEffect);
			player.sendMessage(Text.literal("§5§lДжин усмехнулся... Что-то пошло не так!"), true);
		} else {
			player.sendMessage(Text.literal("§a§lЖелание исполнено!"), true);
		}
		
		// Decrement wishes
		LampItem.setWishesRemaining(lamp, wishesRemaining - 1);
		
		// Play sounds and particles
		player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.PLAYERS, 0.5f, 1.0f);
		
		if (wishesRemaining - 1 <= 0) {
			player.sendMessage(Text.literal("§7Этот Джин больше не может исполнять желания..."), true);
		}
	}
	
	private static float calculateTrickyChance(float baseRisk, float playerLuck) {
		float modifier = 2.0f; // Настройка баланса
		float chance = baseRisk - (playerLuck * modifier);
		return Math.max(0.0f, Math.min(1.0f, chance));
	}
	
	private static void applyEffect(ServerPlayerEntity player, WishConfig.EffectData effectData) {
		if (effectData == null) return;
		
		if (effectData.type.equals("item")) {
			ItemStack stack = new ItemStack(Items.AIR);
			switch (effectData.itemId) {
				case "minecraft:gold_ingot":
					stack = new ItemStack(Items.GOLD_INGOT, effectData.count);
					break;
				case "minecraft:diamond":
					stack = new ItemStack(Items.DIAMOND, effectData.count);
					break;
				case "minecraft:emerald":
					stack = new ItemStack(Items.EMERALD, effectData.count);
					break;
				default:
					stack = new ItemStack(Items.AIR, effectData.count);
			}
			
			if (!stack.isEmpty()) {
				player.getInventory().offerOrDrop(stack);
			}
		} else if (effectData.type.equals("effect")) {
			StatusEffect effect = StatusEffects.byRawId(effectData.effectId);
			if (effect != null) {
				player.addStatusEffect(new StatusEffectInstance(effect, effectData.duration * 20, effectData.amplifier));
			}
		} else if (effectData.type.equals("teleport")) {
			double x = player.getX() + effectData.x;
			double y = player.getY() + effectData.y;
			double z = player.getZ() + effectData.z;
			player.teleport(x, y, z);
		} else if (effectData.type.equals("spawn_mob")) {
			// TODO: Реализация спавна мобов
			player.sendMessage(Text.literal("§4Враги появились рядом!"), true);
		}
	}
}
