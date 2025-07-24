package deus.guidebookmd;

import deus.guidebookmd.components.MDComponent;
import net.minecraft.client.gui.Screen;

import java.util.ArrayList;
import java.util.List;

public class MarkdownScreen extends Screen {
	protected final List<List<MDComponent>> pages;

	public MarkdownScreen() {
		this.pages = new ArrayList<>();
		this.pages.add(MarkdownCompiler.compile("/assets/guidebookmd/markdown/test.md", getClass()));
	}

	@Override
	public void render(int mx, int my, float partialTick) {
		super.render(mx, my, partialTick);
		int y = 0;
		for (List<MDComponent> page : pages) {
			for (MDComponent mdComponent : page) {
				mdComponent.render(0, y);
				y += mdComponent.height;
			}
		}
	}
}
