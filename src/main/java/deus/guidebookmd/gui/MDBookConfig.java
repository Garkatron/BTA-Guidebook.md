package deus.guidebookmd.gui;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class MDBookConfig {
	public int[] scissorWH = {144, 215};
	public int maxWidthCharacters = 26;
	public int maxCharactersHigh = 22;
	public boolean centered = true;
	public boolean centeredMaxWidth = false;
	public String defaultPageTexture = "/assets/minecraft/textures/gui/container/guidebook/guidebook.png";
	public int pageTextureWidth = 158;
	public int pageTextureHeight = 220;
	public int pageSkipAmount = 2;
	public int[] pageTexturePositions = {-79, 79};
	public int[] textXPositions = {-73, 86};
	public int[] textXOffsets = {-80, 80};
	public MDBookConfig(Consumer<MDBookConfig> config) {
		config.accept(this);
	}

	public static MDBookConfig fromJsonResource(Class<?> c, String path) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(c.getResourceAsStream(path)))) {
			Gson gson = new Gson();
			return gson.fromJson(reader, MDBookConfig.class);
		} catch (IOException e) {
			throw new RuntimeException("Error reading JSON file: " + path, e);
		}
	}

}
