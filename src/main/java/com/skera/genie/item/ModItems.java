package com.skera.genie.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import com.skera.genie.GenieMod;

public class ModItems {
	public static final Item GENIE_LAMP = new LampItem(new Item.Settings());
	public static final Item RITUAL_KNIFE = new Item(new Item.Settings());
	public static final Item VAMPIRE_BLOOD_BOTTLE = new Item(new Item.Settings());
	public static final Item HOLY_WATER_BOTTLE = new Item(new Item.Settings());
	public static final Item PURIFIED_BLOOD = new Item(new Item.Settings());
	public static final Item WHITE_HONEY = new Item(new Item.Settings());
	public static final Item FAKE_DIAMOND_BLOCK = new Item(new Item.Settings().maxCount(64)); // Для логики фейка

	public static void registerItems() {
		register("genie_lamp", GENIE_LAMP);
		register("ritual_knife", RITUAL_KNIFE);
		register("vampire_blood_bottle", VAMPIRE_BLOOD_BOTTLE);
		register("holy_water_bottle", HOLY_WATER_BOTTLE);
		register("purified_blood", PURIFIED_BLOOD);
		register("white_honey", WHITE_HONEY);
		register("fake_diamond_block", FAKE_DIAMOND_BLOCK);
	}

	private static void register(String name, Item item) {
		Registry.register(Registries.ITEM, new Identifier(GenieMod.MOD_ID, name), item);
	}
}
