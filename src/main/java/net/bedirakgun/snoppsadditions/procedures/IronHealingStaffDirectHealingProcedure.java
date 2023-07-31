package net.bedirakgun.snoppsadditions.procedures;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;

import net.bedirakgun.snoppsadditions.SnoppsAdditionsMod;

import java.util.Map;

public class IronHealingStaffDirectHealingProcedure {

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				SnoppsAdditionsMod.LOGGER.warn("Failed to load dependency entity for procedure IronHealingStaffDirectHealing!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		if (entity instanceof LivingEntity)
			((LivingEntity) entity).setHealth((float) (((entity instanceof LivingEntity) ? ((LivingEntity) entity).getHealth() : -1) + 8));
		{
			Entity _ent = entity;
			if (!_ent.world.isRemote && _ent.world.getServer() != null) {
				_ent.world.getServer().getCommandManager().handleCommand(_ent.getCommandSource().withFeedbackDisabled().withPermissionLevel(4),
						"kill @e[type=snopps_additions:projectile_Iron_healing_staff,distance=0..3]");
			}
		}
	}
}
