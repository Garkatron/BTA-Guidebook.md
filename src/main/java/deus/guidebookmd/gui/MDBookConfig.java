package deus.guidebookmd.gui;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class MDBookConfig {

	public String defaultPageTexture = "/assets/minecraft/textures/gui/container/guidebook/guidebook.png";

	public int pageSkipAmount = 2;
	public int[] pageTexturePositions = {-79, 79};
	public int[] textXPositions = {-153, 166};
	public int[] textYPositions = {0, 0};
	public MDPageConfig defaultPageConfig = new MDPageConfig();

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
