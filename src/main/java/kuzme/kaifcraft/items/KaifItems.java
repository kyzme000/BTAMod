package kuzme.kaifcraft.items;

import kuzme.kaifcraft.KaifConfig;
import net.minecraft.core.item.Item;
import turniplabs.halplibe.helper.ItemBuilder;

import static kuzme.kaifcraft.KaifCraftMod.MOD_ID;

public class KaifItems {

	static int itemID = 17550;

	public static Item BLUNT;

	public void initializeItems() {

		BLUNT = new ItemBuilder(MOD_ID)
			.build(new Item("blunt", "kaifcraft:item/test", itemID++));
	}
}



