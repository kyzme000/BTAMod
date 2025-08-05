package kuzme.kaifcraft.entity;

import com.mojang.nbt.NbtIo;
import com.mojang.nbt.tags.CompoundTag;
import kuzme.kaifcraft.mixin.accessors.MobCreeperAccessor;
import kuzme.kaifcraft.util.IKaifNbt;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.monster.MobCreeper;
import net.minecraft.core.net.packet.PacketCustomPayload;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntitySmoke extends Entity {
	private int lifetime = 60; // тики
	private int age = 0;
	private final Set<Integer> syncedEntities = new HashSet<>();
	public EntitySmoke(@Nullable World world, double x, double y, double z) {
		super(world);
		this.setPos(x, y, z);
	}

	@Override
	protected void defineSynchedData() {

	}

	@Override
	public void tick() {
		System.out.println("SMOKENTITY");
		if (this.age++ >= this.lifetime) {
			this.remove();
		}
		AABB checkArea = AABB.getTemporaryBB(
			this.x - 2.0,
			this.y - 2.0,
			this.z - 2.0,
			this.x + 2.0,
			this.y + 2.0,
			this.z + 2.0
		);

		List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, checkArea);
		for (Entity entity : list) {
			if (!(entity instanceof IKaifNbt)) continue;

			if (syncedEntities.contains(entity.id)) continue;
			syncedEntities.add(entity.id);

			IKaifNbt kaifNbt = (IKaifNbt) entity;
			CompoundTag tagToSend = kaifNbt.getKaifData();

			try {
				ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
				DataOutputStream dataOutput = new DataOutputStream(byteOutput);

				dataOutput.writeInt(entity.id); // ID сущности
				NbtIo.write(tagToSend, dataOutput); // сериализация тега

				byte[] bytes = byteOutput.toByteArray();
				PacketCustomPayload packet = new PacketCustomPayload("Kaif|Creeper", bytes);

				if (this.world.isClientSide) {
					// Только на клиенте — отправляем пакет
					Minecraft.getMinecraft().getSendQueue().addToSendQueue(packet);
				}
				System.out.println("[EntitySmoke] Отправлен Kaif|Creeper пакет с NBT");

			} catch (IOException e) {
				System.err.println("[EntitySmoke] Ошибка при отправке NBT: " + e);
			}

			if (tagToSend.getInteger("DisableAITimer") <= 0) {
				kaifNbt.setDisableAITimer(200);
			}

			if (entity instanceof MobCreeper) {
				MobCreeper creeper = (MobCreeper) entity;
				MobCreeperAccessor accessor = (MobCreeperAccessor) creeper;

				int time = accessor.getTimeSinceIgnited();
				int state = accessor.invokeGetCreeperState();

				if (time > 0 || state > 0) {
					System.out.println("[EntitySmoke] Creeper state: " + state);
					accessor.setTimeSinceIgnited(1);
					accessor.invokeSetCreeperState(1);
					kaifNbt.setDisableAITimer(60);
				}
			}
		}
	}
	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {

	}
}

