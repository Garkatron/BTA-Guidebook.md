package deus.guidebookmd.gui;

import deus.guidebookmd.components.MDComponent;
import net.minecraft.client.gui.Screen;

import java.util.ArrayList;
import java.util.List;

import static deus.guidebookmd.gui.MDPage.drawPage;

public class MDScreen extends Screen {
	protected List<MDComponent> currentPage = new ArrayList<>();

	protected boolean centered = false;
	protected boolean centeredMaxWidth = false;
	protected int xOffset = 0;
	protected int yOffset = 0;
	protected int startY = 0;

	@Override
	public void render(int mx, int my, float partialTick) {
		super.render(mx, my, partialTick);
		drawPage(currentPage, startY, xOffset, mx, my, width, yOffset, centered, centeredMaxWidth);
	}
}
