package kuzme.kaifcraft;

import kuzme.kaifcraft.block.KaifBlocks;
import kuzme.kaifcraft.item.KaifItems;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kuzme.kaifcraft.KaifCraftMod.MOD_ID;

public class KaifConfig {
	public static final Toml properties = new Toml("KaifCraft Configs.toml \n[!] Be careful with IDs. Changes can affect your existing worlds.");
	public static TomlConfigHandler cfg;

	public static int blockIDs = 10000;
	public static int itemIDs = 20000;

	public static String BlockIDs = "Block IDs";
	public static String ItemIDs = "Item IDs";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	static void Setup() {
		LOGGER.info("Initializing config..");

		properties.addCategory("General")
			.addEntry("cfgVersion", 6);

		properties.addCategory(BlockIDs);
		properties.addEntry(BlockIDs+".startingFrom", blockIDs);
		List<Field> blockFields = Arrays.stream(KaifBlocks.class.getDeclaredFields()).filter((F)-> Block.class.isAssignableFrom(F.getType())).collect(Collectors.toList());
		for (Field blockField : blockFields) {
			properties.addEntry(BlockIDs + "." + blockField.getName(), blockIDs++);
		}

		properties.addCategory(ItemIDs);
		properties.addEntry(ItemIDs+".startingFrom", itemIDs);
		List<Field> itemFields = Arrays.stream(KaifItems.class.getDeclaredFields()).filter((F)-> Item.class.isAssignableFrom(F.getType())).collect(Collectors.toList());
		for (Field itemField : itemFields) {
			properties.addEntry(ItemIDs+ "." + itemField.getName(), itemIDs++);
		}
		properties.addCategory("Others");

		cfg = new TomlConfigHandler(MOD_ID, properties);

		if (cfg.getConfigFile().exists()) {
			cfg.loadConfig();
		} else {
			try {cfg.getConfigFile().createNewFile();} catch (IOException e) {throw new RuntimeException(e);}
			cfg.writeConfig();
		}

	}
}
