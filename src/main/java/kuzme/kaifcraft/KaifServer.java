package kuzme.kaifcraft;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.sound.SoundTypes;

import static kuzme.kaifcraft.KaifCraftMod.MOD_ID;
import static org.apache.log4j.builders.appender.SocketAppenderBuilder.LOGGER;


@Environment(EnvType.SERVER)
public class KaifServer implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		LOGGER.info("KaifServer init");
	}
}
