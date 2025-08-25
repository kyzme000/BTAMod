package kuzme.kaifcraft.util;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.net.packet.PacketCustomPayload;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class KaifNet {

	public static void sendParticleToNearby(World world, double x, double y, double z,
											String particle,
											double mx, double my, double mz,
											int data, double maxDistance) {
		if (EnvironmentHelper.isClientWorld()) {
			return;
		}

		if (EnvironmentHelper.isServerEnvironment()) {
			double radius = 20;

			for (Entity e : world.loadedEntityList) {
				if (!(e instanceof PlayerServer)) continue;

				PlayerServer player = (PlayerServer) e;
				if (player.distanceTo(x, y, z) > radius) continue;

				try (ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
					 DataOutputStream dataOutput = new DataOutputStream(byteOutput)) {

					dataOutput.writeDouble(x);
					dataOutput.writeDouble(y);
					dataOutput.writeDouble(z);
					dataOutput.writeUTF(particle);

					dataOutput.writeDouble(mx);
					dataOutput.writeDouble(my);
					dataOutput.writeDouble(mz);
					dataOutput.writeDouble(data);
					dataOutput.writeDouble(maxDistance);

					PacketCustomPayload packet = new PacketCustomPayload("Kaif|Particles", byteOutput.toByteArray());
					player.playerNetServerHandler.sendPacket(packet);

				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}

	}
	public static void sendSoundToNearby(World world, double sourceX, double sourceY, double sourceZ, String sound, float volume, float pitch) {
		if (EnvironmentHelper.isClientWorld()) return;

		if (EnvironmentHelper.isServerEnvironment()) {
			double radius = 20;

			for (Entity e : world.loadedEntityList) {
				if (!(e instanceof PlayerServer)) continue;

				PlayerServer player = (PlayerServer) e;
				if (player.distanceTo(sourceX, sourceY, sourceZ) > radius) continue;

				try (ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
					 DataOutputStream dataOutput = new DataOutputStream(byteOutput)) {

					dataOutput.writeDouble(sourceX);
					dataOutput.writeDouble(sourceY);
					dataOutput.writeDouble(sourceZ);
					dataOutput.writeUTF(sound);
					dataOutput.writeFloat(volume);
					dataOutput.writeFloat(pitch);

					PacketCustomPayload packet = new PacketCustomPayload("Kaif|Sound", byteOutput.toByteArray());
					player.playerNetServerHandler.sendPacket(packet);

				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

}
