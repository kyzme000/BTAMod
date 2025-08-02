package kuzme.kaifcraft.mixin;

import com.mojang.nbt.tags.CompoundTag;
import kuzme.kaifcraft.util.IKaifNbt;
import net.minecraft.core.entity.MobPathfinder;
import net.minecraft.core.entity.monster.Enemy;
import net.minecraft.core.entity.monster.MobMonster;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MobMonster.class, remap = false)
public abstract class MobMonsterMixin extends MobPathfinder implements Enemy {
	public MobMonsterMixin(@Nullable World world) {
		super(world);
	}
	@Inject(method = "updateAI", at = @At("HEAD"), cancellable = true)
	private void disableAIWhenBlocked(CallbackInfo ci) {
		if (this instanceof IKaifNbt) {
			CompoundTag tag = ((IKaifNbt)this).getKaifData();
			if (tag.getBoolean("DisableAI")) {
				ci.cancel();
			}
		}
	}
}
