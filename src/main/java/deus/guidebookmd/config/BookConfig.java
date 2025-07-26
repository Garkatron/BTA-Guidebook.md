package deus.guidebookmd.config;

import com.google.gson.Gson;
import deus.guidebookmd.Guidebookmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class BookConfig {

	public String defaultPageTexture = "/assets/minecraft/textures/gui/container/guidebook/guidebook.png";
	public String frontPage = "/assets/guidebookmd/textures/gui/generic_cover.png";
	public String backPage = "/assets/guidebookmd/textures/gui/generic_back.png";
	public int[] backPageOffsets = {0,0};
	public int[] frontBackPageWH = {158, 220};

	public int pageSkipAmount = 2;
	public int[] pageTexturePositions = {-79, 79};
	public int[] textXPositions = {-153, 166};
	public int[] textYPositions = {0, 0};
	public boolean pairButtons = true;
	public PageConfig defaultPageConfig = new PageConfig();

	public BookConfig(Consumer<BookConfig> config) {
		config.accept(this);
	}

	public BookConfig() {}

	public static BookConfig fromJsonResource(Class<?> c, String path) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(c.getResourceAsStream(path)))) {
			Gson gson = new Gson();
			return gson.fromJson(reader, BookConfig.class);
		} catch (IOException e) {
			Guidebookmd.LOGGER.error("Error reading JSON file: {}, {}", path, e);
		}
		return new BookConfig();
	}

}
