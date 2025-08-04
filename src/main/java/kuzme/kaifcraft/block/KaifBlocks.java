package kuzme.kaifcraft.block;

import kuzme.kaifcraft.KaifConfig;

public final class KaifBlocks {


	public static int blockID =  KaifConfig.blockIDs;

	public static int blockID(String blockName) {
		try {
			return KaifConfig.cfg.getInt(KaifConfig.BlockIDs + "." + blockName);
		} catch (NullPointerException e) {
			KaifConfig.properties.addEntry(KaifConfig.BlockIDs + "." + blockName, blockID);
			return blockID++;
		}
	}




	public void initializeBlocks() {

	}
}
