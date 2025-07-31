package deus.guidebookmd;

import deus.guidebookmd.item.Items;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.net.command.CommandManager;
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
	public static final String BOOKS_DIRECTORY = FabricLoader.getInstance().getGameDir().toString() + "/guidebookmd/";
	private static final Toml TOML = new Toml("Nothing to see here");
	public static TomlConfigHandler CFG;

	static {
		TOML.addCategory("IDs")
			.addEntry("guidebook", 25000);

		CFG = new TomlConfigHandler(MOD_ID, TOML);
	}

	@Override
	public void onInitialize() {
		Items.initialize();
		CommandManager.registerCommand(new GuideBookCommand());
		LOGGER.info("Guidebook.md initialized.");
	}

	@Override
	public void onRecipesReady() {
		RecipeInitializer.InitRecipes();
	}

	@Override
	public void initNamespaces() {
		RecipeInitializer.InitNameSpaces();
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
