package kuzme.kaifcraft;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.core.sound.SoundTypes;

import static kuzme.kaifcraft.KaifCraftMod.MOD_ID;
import static org.apache.log4j.builders.appender.SocketAppenderBuilder.LOGGER;


public class KaifServer implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		SoundTypes.loadSoundsJson(MOD_ID);

		LOGGER.info("KaifServer init");
	}
}
