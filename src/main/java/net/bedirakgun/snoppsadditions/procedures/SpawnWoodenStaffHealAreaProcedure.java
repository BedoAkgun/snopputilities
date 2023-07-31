package net.bedirakgun.snoppsadditions.procedures;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.command.ICommandSource;
import net.minecraft.command.CommandSource;

import net.bedirakgun.snoppsadditions.SnoppsAdditionsMod;

import java.util.Map;

public class SpawnWoodenStaffHealAreaProcedure {

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				SnoppsAdditionsMod.LOGGER.warn("Failed to load dependency world for procedure SpawnWoodenStaffHealArea!");
			return;
		}
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				SnoppsAdditionsMod.LOGGER.warn("Failed to load dependency x for procedure SpawnWoodenStaffHealArea!");
			return;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				SnoppsAdditionsMod.LOGGER.warn("Failed to load dependency y for procedure SpawnWoodenStaffHealArea!");
			return;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				SnoppsAdditionsMod.LOGGER.warn("Failed to load dependency z for procedure SpawnWoodenStaffHealArea!");
			return;
		}
		IWorld world = (IWorld) dependencies.get("world");
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		if (world instanceof ServerWorld) {
			((World) world).getServer().getCommandManager().handleCommand(
					new CommandSource(ICommandSource.DUMMY, new Vector3d(x, y, z), Vector2f.ZERO, (ServerWorld) world, 4, "",
							new StringTextComponent(""), ((World) world).getServer(), null).withFeedbackDisabled(),
					"/summon area_effect_cloud ~ ~.9 ~ {Radius:2f,Duration:55,ReapplicationDelay:25,Effects:[{Duration:52,Id:10b,Amplifier:1b}]}");
		}
	}
}
