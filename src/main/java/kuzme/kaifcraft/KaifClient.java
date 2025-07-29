package kuzme.kaifcraft;

import kuzme.kaifcraft.particle.ParticleBigSmoke;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.sound.SoundRepository;
import turniplabs.halplibe.helper.ParticleHelper;
import turniplabs.halplibe.util.ClientStartEntrypoint;

import java.io.IOException;
import java.net.URISyntaxException;

import static kuzme.kaifcraft.KaifCraftMod.MOD_ID;
import static org.apache.log4j.builders.appender.SocketAppenderBuilder.LOGGER;

@Environment(EnvType.CLIENT)
public class KaifClient implements ClientModInitializer, ClientStartEntrypoint {

	@Override
	public void onInitializeClient() {
	LOGGER.info("KaifClient init");

	ParticleHelper.createParticle("bigsmoke", (world, x, y, z, xa, ya, za, id) -> new ParticleBigSmoke(world, x, y, z, xa, ya, za));
	SoundRepository.registerNamespace(MOD_ID);

		try {
			TextureRegistry.initializeAllFiles(MOD_ID, TextureRegistry.particleAtlas, false);
		} catch (URISyntaxException | IOException e) {
			LOGGER.error("Failed to initialize textures!");
		}
	}

	@Override
	public void beforeClientStart() {

	}

	@Override
	public void afterClientStart() {

	}
}
