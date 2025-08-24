package kuzme.kaifcraft.mixin;


import com.mojang.nbt.tags.CompoundTag;
import kuzme.kaifcraft.util.IKaifNbt;
import net.minecraft.core.entity.monster.MobMonster;
import net.minecraft.core.entity.monster.MobSpider;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MobSpider.class, remap = false)
public abstract class MobSpiderMixin extends MobMonster implements IKaifNbt {

	@Unique
	private final CompoundTag kaifData = new CompoundTag();
	public MobSpiderMixin(@Nullable World world) {
		super(world);
	}
	@Override
	public CompoundTag getKaifData() {
		return kaifData;
	}
	@Override
	public void setDisableAITimer(int ticks) {
		kaifData.putInt("DisableAITimer", ticks);
		kaifData.putBoolean("DisableAI", true);
	}

	// сохранение/загрузка тега
	@Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
	private void addKaifData(CompoundTag tag, CallbackInfo ci) {
		tag.put("KaifcraftData", kaifData);
	}

	@Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
	private void readKaifData(CompoundTag tag, CallbackInfo ci) {
		if (tag.containsKey("KaifcraftData")) {
			CompoundTag loaded = tag.getCompound("KaifcraftData");
			this.kaifData.getValue().clear();
			for (String key : loaded.getValue().keySet()) {
				this.kaifData.put(key, loaded.getTag(key));
			}
		}
	}

}
