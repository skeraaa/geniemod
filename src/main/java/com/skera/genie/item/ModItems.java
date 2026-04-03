package com.skera.genie.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import com.skera.genie.GenieMod;

public class ModItems {
	public static final LampItem LAMP = new LampItem(new Item.Settings().maxCount(1));
	
	public static void register() {
		register("lamp", LAMP);
	}
	
	private static void register(String name, Item item) {
		Registry.register(Registries.ITEM, new Identifier(GenieMod.MOD_ID, name), item);
	}
}
