package net.bedirakgun.snoppsadditions.procedures;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;

import net.bedirakgun.snoppsadditions.SnoppsAdditionsMod;

import java.util.Map;

public class IronStaffCooldownProcedure {

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				SnoppsAdditionsMod.LOGGER.warn("Failed to load dependency entity for procedure IronStaffCooldown!");
			return;
		}
		if (dependencies.get("itemstack") == null) {
			if (!dependencies.containsKey("itemstack"))
				SnoppsAdditionsMod.LOGGER.warn("Failed to load dependency itemstack for procedure IronStaffCooldown!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		ItemStack itemstack = (ItemStack) dependencies.get("itemstack");
		if (entity instanceof PlayerEntity)
			((PlayerEntity) entity).getCooldownTracker().setCooldown(itemstack.getItem(), (int) 140);
	}
}
