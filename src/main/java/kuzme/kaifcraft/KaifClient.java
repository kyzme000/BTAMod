package kuzme.kaifcraft;

import kuzme.kaifcraft.particle.ParticleBigSmoke;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.sound.SoundRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.ParticleHelper;
import turniplabs.halplibe.util.ClientStartEntrypoint;

import java.io.IOException;
import java.net.URISyntaxException;

import static kuzme.kaifcraft.KaifCraftMod.MOD_ID;

@Environment(EnvType.CLIENT)
public class KaifClient implements ClientModInitializer, ClientStartEntrypoint {

	public static final Logger LOGGER = LoggerFactory.getLogger("KaifClient");

	@Override
	public void onInitializeClient() {
		LOGGER.info("KaifClient init");

		ParticleHelper.createParticle("bigsmoke", (world, x, y, z, xa, ya, za, id) ->
			new ParticleBigSmoke(world, x, y, z, xa, ya, za)
		);
		SoundRepository.registerNamespace(MOD_ID);

		try {
			TextureRegistry.initializeAllFiles(MOD_ID, TextureRegistry.particleAtlas, false);
		} catch (URISyntaxException | IOException e) {
			LOGGER.error("Failed to initialize textures!", e);
		}
	}

	@Override
	public void beforeClientStart() {
		// пусто
	}

	@Override
	public void afterClientStart() {
		// пусто
	}
}
