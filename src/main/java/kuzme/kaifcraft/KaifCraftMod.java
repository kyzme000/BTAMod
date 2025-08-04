package kuzme.kaifcraft;

import kuzme.kaifcraft.block.KaifBlocks;
import kuzme.kaifcraft.entity.KaifEntities;
import kuzme.kaifcraft.item.KaifItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class KaifCraftMod implements ModInitializer, RecipeEntrypoint, GameStartEntrypoint {
    public static final String MOD_ID = "kaifcraft";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
    public void onInitialize() {

		LOGGER.info("VIRUSNYA");
		new KaifItems().initializeItems();
		new KaifBlocks().initializeBlocks();
		new KaifEntities().initializeEntities();
    }

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}

	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {
	}


}
