package net.bedirakgun.snoppsadditions.procedures;

import net.minecraft.potion.Effects;
import net.minecraft.potion.EffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;

import net.bedirakgun.snoppsadditions.SnoppsAdditionsMod;

import java.util.Random;
import java.util.Map;

public class HasteRightclickedProcedure {

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				SnoppsAdditionsMod.LOGGER.warn("Failed to load dependency entity for procedure HasteRightclicked!");
			return;
		}
		if (dependencies.get("itemstack") == null) {
			if (!dependencies.containsKey("itemstack"))
				SnoppsAdditionsMod.LOGGER.warn("Failed to load dependency itemstack for procedure HasteRightclicked!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		ItemStack itemstack = (ItemStack) dependencies.get("itemstack");
		if (entity instanceof LivingEntity)
			((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.HASTE, (int) 160, (int) 3));
		{
			ItemStack _ist = itemstack;
			if (_ist.attemptDamageItem((int) 1, new Random(), null)) {
				_ist.shrink(1);
				_ist.setDamage(0);
			}
		}
		if ((itemstack).getDamage() == 0) {
			(itemstack).setCount((int) 0);
		}
		if (entity instanceof PlayerEntity)
			((PlayerEntity) entity).getCooldownTracker().setCooldown(itemstack.getItem(), (int) 900);
	}
}
