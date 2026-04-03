package com.skera.genie.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import com.skera.genie.network.ModPackets;
import net.minecraft.server.network.ServerPlayerEntity;

public class LampItem extends Item {
	public LampItem(Settings settings) {
		super(settings);
	}
	
	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		
		if (!world.isClient) {
			// Send packet to open GUI on client
			if (user instanceof ServerPlayerEntity serverPlayer) {
				ModPackets.openGenieScreen(serverPlayer);
			}
		}
		
		return ActionResult.SUCCESS;
	}
	
	public static int getWishesRemaining(ItemStack stack) {
		NbtCompound nbt = stack.getNbt();
		if (nbt == null || !nbt.contains("WishesRemaining")) {
			return 0;
		}
		return nbt.getInt("WishesRemaining");
	}
	
	public static void setWishesRemaining(ItemStack stack, int wishes) {
		NbtCompound nbt = stack.getOrCreateNbt();
		nbt.putInt("WishesRemaining", wishes);
	}
	
	public static String getGenieRarity(ItemStack stack) {
		NbtCompound nbt = stack.getNbt();
		if (nbt == null || !nbt.contains("GenieRarity")) {
			return "common";
		}
		return nbt.getString("GenieRarity");
	}
	
	public static void setGenieRarity(ItemStack stack, String rarity) {
		NbtCompound nbt = stack.getOrCreateNbt();
		nbt.putString("GenieRarity", rarity);
	}
}
