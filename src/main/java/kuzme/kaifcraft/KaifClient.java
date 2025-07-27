package kuzme.kaifcraft;

import net.fabricmc.api.ClientModInitializer;
import turniplabs.halplibe.util.ClientStartEntrypoint;

import static org.apache.log4j.builders.appender.SocketAppenderBuilder.LOGGER;

public class KaifClient implements ClientModInitializer, ClientStartEntrypoint {
	@Override
	public void onInitializeClient() {
	LOGGER.info("KaifClient init");
	}

	@Override
	public void beforeClientStart() {

	}

	@Override
	public void afterClientStart() {

	}
}
