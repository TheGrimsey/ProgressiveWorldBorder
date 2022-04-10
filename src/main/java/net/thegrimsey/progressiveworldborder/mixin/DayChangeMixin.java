package net.thegrimsey.progressiveworldborder.mixin;

import net.minecraft.server.world.ServerWorld;
import net.thegrimsey.progressiveworldborder.ProgressiveWorldBorder;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class DayChangeMixin {
	@Inject(at = @At("HEAD"), method = "setTimeOfDay(J)V")
	private void setTimeOfDay(long timeOfDay, CallbackInfo info) {
		ServerWorld world = (ServerWorld)(Object)(this);
		long oldDay = world.getTimeOfDay() / 24000L;
		long newDay = timeOfDay / 24000L;

		if(oldDay != newDay) {
			ProgressiveWorldBorder.UpdateWorldBorder(world, newDay, false);
		}
	}
}
