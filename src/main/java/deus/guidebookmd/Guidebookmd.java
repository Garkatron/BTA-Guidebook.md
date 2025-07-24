package deus.guidebookmd;

import deus.guidebookmd.item.ItemPage;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.io.IOException;
import java.net.URISyntaxException;


public class Guidebookmd implements ModInitializer, RecipeEntrypoint, GameStartEntrypoint {
    public static final String MOD_ID = "guidebookmd";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static Item itemPage = null;
	ItemBuilder genericItemBuilder = new ItemBuilder(MOD_ID);

	@Override
    public void onInitialize() {
        LOGGER.info("Guidebook.md initialized.");
		itemPage = genericItemBuilder.build(new ItemPage("itempage","guidebookmd:item/itempage", 25000));
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
}
