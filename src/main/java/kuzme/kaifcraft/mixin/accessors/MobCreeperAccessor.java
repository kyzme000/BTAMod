package kuzme.kaifcraft.mixin.accessors;

import net.minecraft.core.entity.monster.MobCreeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = MobCreeper.class, remap = false)
public interface MobCreeperAccessor {
	@Accessor("timeSinceIgnited")
	int getTimeSinceIgnited();

	@Accessor("timeSinceIgnited")
	void setTimeSinceIgnited(int ticks);

	@Invoker("setCreeperState")
	void invokeSetCreeperState(int i);

	@Invoker("getCreeperState")
	int invokeGetCreeperState();
}
