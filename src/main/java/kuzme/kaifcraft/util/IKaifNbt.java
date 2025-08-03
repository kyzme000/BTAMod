package kuzme.kaifcraft.util;

import com.mojang.nbt.tags.CompoundTag;

public interface IKaifNbt {
	CompoundTag getKaifData();
	void setDisableAITimer(int ticks);
}
