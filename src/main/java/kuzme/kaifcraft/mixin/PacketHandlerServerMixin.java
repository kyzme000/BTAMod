package kuzme.kaifcraft.mixin;

import com.mojang.nbt.tags.CompoundTag;
import kuzme.kaifcraft.item.ItemBlunt;
import kuzme.kaifcraft.util.IKaifNbt;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.monster.MobCreeper;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.PacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.net.handler.PacketHandlerServer;
import net.minecraft.server.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

@Mixin(value = PacketHandlerServer.class, remap = false)
public abstract class PacketHandlerServerMixin {

	@Shadow
	private PlayerServer playerEntity;

	@Inject(
		method = "handleCustomPayload(Lnet/minecraft/core/net/packet/PacketCustomPayload;)V",
		at = @At("TAIL"),
		remap = false
	)
	private void kaifcraft$onHandleCustomPayload(PacketCustomPayload packet, CallbackInfo ci) {
		System.out.println("[Kaifcraft] PacketHandlerServerMixin injected");
		System.out.println("[Kaifcraft] Packet received: " + packet.channel);

		if ("Kaif|Creeper".equals(packet.channel)) {
			// Обработка Kaif|Creeper, твой существующий код
			try (ByteArrayInputStream byteInput = new ByteArrayInputStream(packet.data);
				 DataInputStream dataInputStream = new DataInputStream(byteInput)) {

				int entityId = dataInputStream.readInt();
				int disableTicks = dataInputStream.readInt();

				MinecraftServer server = playerEntity.mcServer;
				WorldServer world = server.getDimensionWorld(playerEntity.dimension);

				Entity entity = null;
				for (Entity e : world.loadedEntityList) {
					if (e.id == entityId) {
						entity = e;
						break;
					}
				}

				if (entity == null) {
					System.err.println("[Kaifcraft] Не удалось найти сущность с ID: " + entityId);
					return;
				}

				boolean disableAI = dataInputStream.readBoolean();
				if (entity instanceof IKaifNbt) {
					CompoundTag tag = ((IKaifNbt) entity).getKaifData();
					tag.putBoolean("DisableAI", disableAI);
					System.out.println("[Kaifcraft] DisableAI установлен в " + disableAI + " для " + entity.getClass().getSimpleName());
				} else {
					System.err.println("[Kaifcraft] Сущность не реализует IKaifNbt");
				}

			} catch (IOException e) {
				System.err.println("[Kaifcraft] Ошибка при чтении пакета Kaif|Creeper: " + e);
			}
		} else if ("KaifBluntHold".equals(packet.channel)) {
			System.out.println("[Kaifcraft] === Обработка KaifBluntHold начата ===");
			try (ByteArrayInputStream byteInput = new ByteArrayInputStream(packet.data);
				 DataInputStream dataInputStream = new DataInputStream(byteInput)) {

				boolean holding = dataInputStream.readBoolean();
				ItemStack held = playerEntity.getHeldItem();
				if (held != null && held.getItem() instanceof ItemBlunt) {
					CompoundTag tag = held.getData();
					tag.putBoolean("ClientHolding", holding);
					System.out.println("[Kaifcraft] ClientHolding = " + holding);
				} else {
					System.err.println("[Kaifcraft] Нет ItemBlunt в руке игрока");
				}

			} catch (IOException e) {
				System.err.println("[Kaifcraft] Ошибка при чтении пакета Kaif|BluntHold: " + e);
			}
		} else {
			System.out.println("[Kaifcraft] Получен неизвестный канал: " + packet.channel);
		}

	}
}
