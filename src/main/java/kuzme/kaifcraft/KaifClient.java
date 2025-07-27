package kuzme.kaifcraft;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.minecraft.core.sound.SoundTypes;
import turniplabs.halplibe.util.ClientStartEntrypoint;

import static kuzme.kaifcraft.KaifCraftMod.MOD_ID;
import static org.apache.log4j.builders.appender.SocketAppenderBuilder.LOGGER;

@Environment(EnvType.CLIENT)
public class KaifClient implements ClientModInitializer, ClientStartEntrypoint {
	@Override
	public void onInitializeClient() {
	LOGGER.info("KaifClient init");
	SoundTypes.loadSoundsJson(MOD_ID);
	}

	@Override
	public void beforeClientStart() {

	}

	@Override
	public void afterClientStart() {

	}
}
