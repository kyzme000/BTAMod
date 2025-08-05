package kuzme.kaifcraft;

import kuzme.kaifcraft.entity.EmptyRenderer;
import kuzme.kaifcraft.entity.EntitySmoke;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;

import static kuzme.kaifcraft.KaifCraftMod.MOD_ID;
import static kuzme.kaifcraft.item.KaifItems.*;

public class KaifModelEntrypoint implements ModelEntrypoint {
	@Override
	public void initBlockModels(BlockModelDispatcher dispatcher){

	}
	@Override
	public void initItemModels(ItemModelDispatcher dispatcher) {
		ModelHelper.setItemModel(BLUNT, () -> {
			ItemModelStandard im = new ItemModelStandard(BLUNT, MOD_ID);
			im.icon = TextureRegistry.getTexture(NamespaceID.getPermanent(MOD_ID, "item/blunt"));
			return im;
		});
	}

	@Override
	public void initEntityModels(EntityRenderDispatcher dispatcher) {

		ModelHelper.setEntityModel(EntitySmoke.class, EmptyRenderer::new);

	}

	@Override
	public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {

	}

	@Override
	public void initBlockColors(BlockColorDispatcher dispatcher) {

	}
}
