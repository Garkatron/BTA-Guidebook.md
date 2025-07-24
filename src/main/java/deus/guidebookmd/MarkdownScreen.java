package deus.guidebookmd;

import deus.guidebookmd.components.MDComponent;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Screen;

import java.util.ArrayList;
import java.util.List;

public class MarkdownScreen extends Screen {
	protected final List<List<MDComponent>> pages = new ArrayList<>();
	protected List<MDComponent> currentPage = new ArrayList<>();

	protected boolean centered = false;
	protected int xOffset = 0;
	protected int yOffset = 0;
	protected int startY = 0;

	@Override
	public void render(int mx, int my, float partialTick) {
		super.render(mx, my, partialTick);
		drawPage(currentPage, startY, xOffset);
	}

	protected void drawPage(List<MDComponent> page, int startY, int xOffset) {
		int currentY = startY;
		for (MDComponent mdComponent : page) {
			int renderX = calculateXPosition(mdComponent, xOffset);
			renderComponent(mdComponent, renderX, currentY + yOffset);
			currentY += mdComponent.height;
		}
	}

	private int calculateXPosition(MDComponent component, int xOffset) {
		if (centered) {
			return (width - component.width) / 2 + xOffset;
		}
		return xOffset;
	}

	private void renderComponent(MDComponent component, int x, int y) {
		component.render(x, y);
	}
}
