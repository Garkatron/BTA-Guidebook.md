package deus.guidebookmd;

import deus.guidebookmd.block.MDBlocks;
import deus.guidebookmd.item.Items;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.util.helper.Side;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;

public class Models implements ModelEntrypoint {
	@Override
	public void initBlockModels(BlockModelDispatcher blockModelDispatcher) {
		ModelHelper.setBlockModel(MDBlocks.BLOCK_PRINTER, () -> new BlockModelStandard<>(MDBlocks.BLOCK_PRINTER)
			.setAllTextures(0, "guidebookmd:block/printer/sides")
			.setTex(0, "guidebookmd:block/printer/top", Side.TOP)
			.setTex(0, "guidebookmd:block/printer/back_0", Side.NORTH)
			.setTex(0, "guidebookmd:block/printer/front", Side.SOUTH)
		);
	}

	@Override
	public void initItemModels(ItemModelDispatcher itemModelDispatcher) {
		ModelHelper.setItemModel(Items.MD_ITEM_GUIDE,
			() -> {
				ItemModelStandard model = new ItemModelStandard(Items.MD_ITEM_GUIDE, Guidebookmd.MOD_ID);
				model.icon = TextureRegistry.getTexture(Items.MD_ITEM_GUIDE.namespaceID);
				return model;
			});
		ModelHelper.setItemModel(Items.MD_ITEM_EDITABLE_BOOK,
			() -> {
				ItemModelStandard model = new ItemModelStandard(Items.MD_ITEM_EDITABLE_BOOK, Guidebookmd.MOD_ID);
				model.icon = TextureRegistry.getTexture(Items.MD_ITEM_EDITABLE_BOOK.namespaceID);
				return model;
			});
		ModelHelper.setItemModel(Items.MD_ITEM_READONLY_BOOK,
			() -> {
				ItemModelStandard model = new ItemModelStandard(Items.MD_ITEM_READONLY_BOOK, Guidebookmd.MOD_ID);
				model.icon = TextureRegistry.getTexture(Items.MD_ITEM_READONLY_BOOK.namespaceID);
				return model;
			});

	}
	@Override
	public void initEntityModels(EntityRenderDispatcher entityRenderDispatcher) {

	}

	@Override
	public void initTileEntityModels(TileEntityRenderDispatcher tileEntityRenderDispatcher) {

	}

	@Override
	public void initBlockColors(BlockColorDispatcher blockColorDispatcher) {

	}
}
