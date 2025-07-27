package kuzme.kaifcraft.items;

import kuzme.kaifcraft.KaifConfig;
import net.minecraft.core.item.Item;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.util.ItemInitEntrypoint;


import java.util.HashMap;

import static kuzme.kaifcraft.KaifCraftMod.MOD_ID;

public class KaifItems implements ItemInitEntrypoint {

	public static int itemID = KaifConfig.itemIDs;


	public static int itemID(String itemName) {
		try {
			return KaifConfig.cfg.getInt(KaifConfig.itemIDs + "." + itemID);
		} catch (NullPointerException e) {
			KaifConfig.properties.addEntry(KaifConfig.itemIDs + "." + itemName, itemID);
			return itemID++;
		}
	}

	public static Item BLUNT;

	public static String itemKey(String string) {
		return MOD_ID + ":item/" + string;
	}

	public void initializeItems() {

		BLUNT = new ItemBuilder(MOD_ID)
			.build(new ItemBlunt("blunt", itemKey("blunt"), itemID("BLUNT")));
	}

	@Override
	public void afterItemInit() {

	}
}



