package kuzme.kaifcraft.mixin;

import com.mojang.nbt.tags.CompoundTag;
import kuzme.kaifcraft.mixin.accessors.MobCreeperAccessor;
import kuzme.kaifcraft.util.IKaifNbt;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.monster.MobCreeper;
import net.minecraft.core.entity.monster.MobMonster;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MobCreeper.class, remap = false)
public abstract class MobCreeperMixin extends MobMonster implements IKaifNbt {
	public MobCreeperMixin(@Nullable World world) {
		super(world);
	}

	@Unique
	private final CompoundTag kaifData = new CompoundTag();
	private boolean fuseSoundPlayed = false;


	@Inject(method = "attackEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;playSoundAtEntity(Lnet/minecraft/core/entity/Entity;Lnet/minecraft/core/entity/Entity;Ljava/lang/String;FF)V"), cancellable = true)
	private void onPlayFuseSound(Entity target, float distance, CallbackInfo ci) {
		MobCreeper self = (MobCreeper)(Object)this;

		// Если звук уже сыгран, отменяем вызов, чтобы не играть снова
		if (fuseSoundPlayed) {
			ci.cancel(); // отменяем оригинальный вызов playSoundAtEntity
			return;
		}

		// Отмечаем, что звук сыгран
		fuseSoundPlayed = true;
	}

	@Inject(method = "attackEntity", at = @At("TAIL"))
	private void atEndOfAttackEntity(Entity target, float distance, CallbackInfo ci) {
		MobCreeper self = (MobCreeper)(Object)this;
		MobCreeperAccessor accessor = (MobCreeperAccessor) (Object) self;

		int time = accessor.getTimeSinceIgnited();

		// Если крипер перестал зажигаться — сбрасываем флаг звука
		if (time == 0) {
			fuseSoundPlayed = false;
		}
	}

	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void addAdditionalMixin(CompoundTag tag, CallbackInfo ci) {
		tag.put("KaifcraftData", kaifData);
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void readAdditionalMixin(CompoundTag tag, CallbackInfo ci) {
		if (tag.containsKey("KaifcraftData")) {
			CompoundTag loaded = tag.getCompound("KaifcraftData");
			this.kaifData.getValue().clear();
			for (String key : loaded.getValue().keySet()) {
				this.kaifData.put(key, loaded.getTag(key));
			}
		}
	}

	@Override
	public CompoundTag getKaifData() {
		return kaifData;
	}
	@Override
	public void setDisableAITimer(int ticks) {
		kaifData.putInt("DisableAITimer", ticks);
		kaifData.putBoolean("DisableAI", true);
		System.out.println("[MobCreeperMixin] Set DisableAITimer = " + ticks);
	}
}
