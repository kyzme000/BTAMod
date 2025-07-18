package kuzme.kaifcraft.models;

import kuzme.kaifcraft.items.KaifItems;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;

public class KaifModels implements ModelEntrypoint {
	@Override
	public void initBlockModels(BlockModelDispatcher dispatcher){
	}
	@Override
	public void initItemModels(ItemModelDispatcher dispatcher) {

		ModelHelper.setItemModel(KaifItems.BLUNT, () -> new ItemModelStandard(KaifItems.BLUNT, "kaifcraft"));
	}
	@Override
	public void initEntityModels(EntityRenderDispatcher dispatcher) {
	}
	@Override
	public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
	}
	@Override
	public void initBlockColors(BlockColorDispatcher dispatcher) {
	}
}
