package deus.guidebookmd.gui;

import com.google.gson.Gson;
import deus.guidebookmd.formats.MarkdownColor;

public class MDPageConfig {
	public int argbFontColor = MarkdownColor.get('0');
	public String pageTexture = "/assets/minecraft/textures/gui/container/guidebook/guidebook.png";

	public static MDPageConfig fromJsonString(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, MDPageConfig.class);
	}
}
