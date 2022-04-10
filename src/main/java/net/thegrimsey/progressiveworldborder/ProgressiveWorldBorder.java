package net.thegrimsey.progressiveworldborder;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.server.command.WorldBorderCommand;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.level.ServerWorldProperties;
import org.jetbrains.annotations.Debug;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProgressiveWorldBorder implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("progressiveworldborder");

	public static ProgressiveWorldBorderConfig CONFIG;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		CONFIG = new ProgressiveWorldBorderConfig();

		LOGGER.info("Hello Fabric world!");

		ServerWorldEvents.LOAD.register((server, world) -> {
			LOGGER.info("World Bordering...");
			long day = world.getTimeOfDay() / 24000L;

			LOGGER.info("Day: " + day);
			UpdateWorldBorder(world, day, true);
		});
	}

	public static double getBorderForDay(long day) {
		return CONFIG.baseSize + CONFIG.expansionPerDay * day;
	}

	public static void UpdateWorldBorder(ServerWorld world, long day, boolean skipTime) {
		BlockPos worldSpawn = world.getSpawnPos();
		WorldBorder worldBorder = world.getWorldBorder();
		worldBorder.setCenter(worldSpawn.getX(), worldSpawn.getZ());
		if(skipTime)
			worldBorder.setSize(getBorderForDay(day));
		else
			worldBorder.interpolateSize(worldBorder.getSize(), getBorderForDay(day), CONFIG.expansionTimeSeconds * 1000L);

		((ServerWorldProperties)world.getLevelProperties()).setWorldBorder(worldBorder.write());
	}
}
