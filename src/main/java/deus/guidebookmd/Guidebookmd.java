package deus.guidebookmd;

import deus.guidebookmd.block.MDBlocks;
import deus.guidebookmd.item.Items;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import java.io.IOException;
import java.net.URISyntaxException;


public class Guidebookmd implements ModInitializer, RecipeEntrypoint, GameStartEntrypoint {
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
		MDBlocks.initialize();
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

}
