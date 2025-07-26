package deus.guidebookmd.config;

import com.google.gson.Gson;
import deus.guidebookmd.formats.MarkdownColor;

public class PageConfig {
	public int argbFontColor = MarkdownColor.get('0');
	public String pageTexture = "/assets/minecraft/textures/gui/container/guidebook/guidebook.png";

	public int[] scissorWH = {144, 215};
	public int maxWidthCharacters = 26;
	public int maxCharactersHigh = 22;
	public boolean centered = false;
	public boolean centeredMaxWidth = false;
	public int pageTextureWidth = 158;
	public int pageTextureHeight = 220;


	public static PageConfig fromJsonString(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, PageConfig.class);
	}
}
