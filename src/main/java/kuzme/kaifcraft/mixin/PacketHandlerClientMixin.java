package kuzme.kaifcraft.mixin;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.net.handler.PacketHandlerClient;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.packet.PacketCustomPayload;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

@Mixin(value = PacketHandlerClient.class, remap = false)
public abstract class PacketHandlerClientMixin {
	@Inject(method = "handleCustomPayload", at = @At("TAIL"), remap = false)
	private void kaifcraft$onHandleParticles(PacketCustomPayload packet, CallbackInfo ci) {
		if (!"Kaif|Particles".equals(packet.channel)) return;

		try (ByteArrayInputStream byteInput = new ByteArrayInputStream(packet.data);
			 DataInputStream dataInput = new DataInputStream(byteInput)) {

			double x = dataInput.readDouble();
			double y = dataInput.readDouble();
			double z = dataInput.readDouble();
			String particle = dataInput.readUTF();

			double mx = dataInput.readDouble();
			double my = dataInput.readDouble();
			double mz = dataInput.readDouble();
			int data = (int) dataInput.readDouble();
			double maxDistance = dataInput.readDouble();

			World world = Minecraft.getMinecraft().currentWorld;
			if (world != null) {
				world.spawnParticle(particle, x, y, z, mx, my, mz, data, maxDistance);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Inject(method = "handleCustomPayload", at = @At("TAIL"), remap = false)
	private void kaifcraft$onHandleSound(PacketCustomPayload packet, CallbackInfo ci) {
		if (!"Kaif|Sound".equals(packet.channel)) return;

		try (ByteArrayInputStream byteInput = new ByteArrayInputStream(packet.data);
			 DataInputStream dataInput = new DataInputStream(byteInput)) {

			double sourceX = dataInput.readDouble();
			double sourceY = dataInput.readDouble();
			double sourceZ = dataInput.readDouble();
			String sound = dataInput.readUTF();
			float volume = dataInput.readFloat();
			float pitch = dataInput.readFloat();

			World world = Minecraft.getMinecraft().currentWorld;
			Player player = Minecraft.getMinecraft().thePlayer;
			if (world != null && player != null) {
				Entity dummySource = new Entity(world) {
					@Override
					protected void defineSynchedData() {}
					@Override public void tick() {}
					@Override
					public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {}
					@Override
					public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {}
				};
				dummySource.x = sourceX;
				dummySource.y = sourceY;
				dummySource.z = sourceZ;
				world.playSoundAtEntity(player, dummySource, sound, volume, pitch);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

