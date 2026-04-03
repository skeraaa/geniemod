package com.skera.genie.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import com.skera.genie.network.WishHandler;

public class LampItem extends Item {
	public LampItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		if (!world.isClient) {
			// Открываем GUI на клиенте, но здесь можно отправить пакет
			player.sendMessage(Text.literal("§5Джин слушает тебя... (Открой GUI)"), true);
			// В реальной реализации: PacketSender.send(new OpenGenieScreenPacket(stack));
		} else {
			// Клиентская часть открытия экрана
			// GenieModClient.openScreen(stack);
		}
		return TypedActionResult.success(stack);
	}
	
	public static int getWishesLeft(ItemStack stack) {
		NbtCompound nbt = stack.getNbt();
		if (nbt == null) return 0;
		return nbt.getInt("wishes_left");
	}
	
	public static void setWishesLeft(ItemStack stack, int count) {
		stack.getOrCreateNbt().putInt("wishes_left", count);
	}
	
	public static String getRarity(ItemStack stack) {
		NbtCompound nbt = stack.getNbt();
		if (nbt == null) return "common";
		return nbt.getString("rarity");
	}
}
