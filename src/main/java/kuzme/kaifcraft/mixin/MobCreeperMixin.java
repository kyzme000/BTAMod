package kuzme.kaifcraft.mixin;

import kuzme.kaifcraft.particle.ParticleBigSmoke;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.monster.MobCreeper;
import net.minecraft.core.entity.monster.MobMonster;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = MobCreeper.class, remap = false)
public abstract class MobCreeperMixin extends MobMonster {


	public MobCreeperMixin(@Nullable World world) {
		super(world);
	}
	@Shadow private int timeSinceIgnited;
	@Shadow public abstract void setCreeperState(int state);

	@Inject(
		method = "attackEntity(Lnet/minecraft/core/entity/Entity;F)V",
		at = @At("HEAD"),
		cancellable = true
	)
	private void attackEntity(Entity entity, float distance, CallbackInfo ci) {
		System.out.println("Mixin injected!");
		if (isSmoked()) {
			this.setCreeperState(-1);
			this.timeSinceIgnited = 0;
			ci.cancel();  // отменяем вызов createExplosion и дальнейшее выполнение
		}
	}

	private boolean isSmoked() {

		AABB aabb = AABB.getPermanentBB(
			this.x - 2.0,
			this.y - 2.0,
			this.z - 2.0,
			this.x + 2.0,
			this.y + 2.0,
			this.z + 2.0
		);
		List<Entity> nearby = this.world.getEntitiesWithinAABBExcludingEntity(this, aabb);
		for (Entity e : nearby) {
			if (e instanceof ParticleBigSmoke) {
				System.out.println("smoked!");
				return true;
			}
		}
		return false;
	}
}
