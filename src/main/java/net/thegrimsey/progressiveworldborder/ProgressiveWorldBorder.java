package net.thegrimsey.progressiveworldborder;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.level.ServerWorldProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProgressiveWorldBorder implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("progressiveworldborder");

	public static ProgressiveWorldBorderConfig CONFIG;

	@Override
	public void onInitialize() {
		// Register config file.
		AutoConfig.register(ProgressiveWorldBorderConfig.class, JanksonConfigSerializer::new);
		// Get config.
		CONFIG = AutoConfig.getConfigHolder(ProgressiveWorldBorderConfig.class).getConfig();

		ServerWorldEvents.LOAD.register((server, world) -> {
			LOGGER.info("World Bordering...");
			long day = world.getTimeOfDay() / 24000L;
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
			worldBorder.interpolateSize(worldBorder.getSize(), getBorderForDay(day), (long) (CONFIG.expansionTimeSeconds * 1000L));

		((ServerWorldProperties)world.getLevelProperties()).setWorldBorder(worldBorder.write());
	}
}
