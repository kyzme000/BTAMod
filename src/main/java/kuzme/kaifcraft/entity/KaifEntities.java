package kuzme.kaifcraft.entity;

import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.EntityHelper;

import static kuzme.kaifcraft.KaifCraftMod.MOD_ID;

public final class KaifEntities {
	public static boolean hasInit = false;

	public static void init() {
		if(!hasInit){
			hasInit = true;
			initializeEntities();
		}

	}

	public static String entityKey(String string) {
		return MOD_ID + ".entity." + string;
	}

	public static void initializeEntities() {
		EntityHelper.createEntity(EntitySmoke.class, NamespaceID.getPermanent(MOD_ID, "smoke"), entityKey("smoke"));
	}
}
