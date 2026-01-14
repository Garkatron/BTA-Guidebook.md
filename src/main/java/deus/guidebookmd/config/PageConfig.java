package deus.guidebookmd.config;

import com.google.gson.Gson;
import deus.guidebookmd.formats.MarkdownColor;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.util.Arrays;
import java.util.Objects;

public class PageConfig implements Cloneable {
	public int argbFontColor;
	public String pageTexture;
	public int[] scissorWH;
	public int maxWidthCharacters;
	public int maxCharactersHigh;
	public boolean centered;
	public boolean centeredMaxWidth;
	public int pageTextureWidth;
	public int pageTextureHeight;

	public PageConfig() {
		this.argbFontColor = MarkdownColor.get('0');
		this.pageTexture = "/assets/minecraft/textures/gui/container/guidebook/guidebook.png";
		this.scissorWH = new int[]{144, 215};
		this.maxWidthCharacters = 24;
		this.maxCharactersHigh = 22;
		this.centered = false;
		this.centeredMaxWidth = false;
		this.pageTextureWidth = 158;
		this.pageTextureHeight = 220;
	}


	public static PageConfig fromJsonString(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, PageConfig.class);
	}

	@Override
	public PageConfig clone() {
		try {
			PageConfig clone = (PageConfig) super.clone();
			clone.scissorWH = this.scissorWH.clone(); // Defensive copy of mutable array
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError("Cloning failed unexpectedly", e);
		}
	}
}
