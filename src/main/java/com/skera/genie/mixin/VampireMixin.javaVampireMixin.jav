package com.skera.genie.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.effect.StatusEffects;

@Mixin(PlayerEntity.class)
public abstract class VampireMixin extends LivingEntity {
	protected VampireMixin(EntityType<? extends LivingEntity> entityType, World world) { super(entityType, world); }

	@Inject(at = @At("HEAD"), method = "tick")
	public void tick(CallbackInfo ci) {
		PlayerEntity player = (PlayerEntity) (Object) this;
		if (player.getPersistentData().getBoolean("isVampire")) {
			// Проверка солнца
			if (player.getWorld().isDay() && player.getWorld().isSkyVisible(player.getBlockPos())) {
				// Проверка кожаной брони (упрощенно)
				boolean hasLeather = player.getInventory().getArmorStack(3).getName().getString().contains("Leather") && 
									 player.getInventory().getArmorStack(2).getName().getString().contains("Leather");
				if (!hasLeather) {
					player.setOnFireFor(1); // Горим на солнце
				}
			}
			// Проверка жителей рядом (Голод)
			// ... логика поиска сущностей в радиусе 50 блоков
		}
	}
}
