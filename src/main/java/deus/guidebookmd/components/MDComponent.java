package deus.guidebookmd.components;

import deus.guidebookmd.gui.MarkdownBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public abstract class MDComponent extends Gui {

	public int height = 0;
	public int width = 0;
	public int x = 0;
	public int y = 0;
	public MarkdownBook screen;
	protected Minecraft mc = Minecraft.getMinecraft();
	public void render(int x, int y, int mx, int my) {

	}

	public void setScreen(MarkdownBook screen) {
		this.screen = screen;
	}

}
