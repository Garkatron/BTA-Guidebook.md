package deus.guidebookmd;

import deus.guidebookmd.item.ItemGuidebookmd;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.Item;
import net.minecraft.core.util.collection.NamespaceID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.ModelEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import java.io.IOException;
import java.net.URISyntaxException;


public class Guidebookmd implements ModInitializer, RecipeEntrypoint, GameStartEntrypoint, ModelEntrypoint {
    public static final String MOD_ID = "guidebookmd";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static TomlConfigHandler CFG;
	private static final Toml TOML = new Toml("Nothing to see here");

	static {
		TOML.addCategory("IDs")
			.addEntry("guidebook", 25000);

		CFG = new TomlConfigHandler(MOD_ID, TOML);
	}

	@Override
    public void onInitialize() {
		Items.initialize();
        LOGGER.info("Guidebook.md initialized.");
    }

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}

	@Override
	public void beforeGameStart() {
		try {
			TextureRegistry.initializeAllFiles(MOD_ID, TextureRegistry.guiSpriteAtlas, true);
		} catch (URISyntaxException | IOException e) {
			System.out.println("ERROR");
			throw new RuntimeException(e);
		}
	}

	@Override
	public void afterGameStart() {

	}

	@Override
	public void initBlockModels(BlockModelDispatcher blockModelDispatcher) {

	}

	@Override
	public void initItemModels(ItemModelDispatcher itemModelDispatcher) {
		ModelHelper.setItemModel(Items.MD_ITEM_GUIDE,
			() -> {
				ItemModelStandard model = new ItemModelStandard(Items.MD_ITEM_GUIDE, MOD_ID);
				model.icon = TextureRegistry.getTexture(Items.MD_ITEM_GUIDE.namespaceID);
				return model;
			});
		ModelHelper.setItemModel(Items.MD_ITEM_EDITOR_BOOK,
			() -> {
				ItemModelStandard model = new ItemModelStandard(Items.MD_ITEM_EDITOR_BOOK, MOD_ID);
				model.icon = TextureRegistry.getTexture(Items.MD_ITEM_EDITOR_BOOK.namespaceID);
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
