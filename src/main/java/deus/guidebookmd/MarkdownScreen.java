package deus.guidebookmd;

import deus.guidebookmd.components.MDComponent;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Screen;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MarkdownScreen extends Screen {
	protected final List<List<MDComponent>> pages = new ArrayList<>();
	protected List<MDComponent> currentPage = new ArrayList<>();

	protected boolean centered = false;
	protected boolean centeredMaxWidth = false;
	protected int xOffset = 0;
	protected int yOffset = 0;
	protected int startY = 0;

	@Override
	public void render(int mx, int my, float partialTick) {
		super.render(mx, my, partialTick);
		drawPage(currentPage, startY, xOffset, mx, my);
	}

	protected void drawPage(List<MDComponent> page, int startY, int xOffset, int mouseX, int mouseY) {
		if (page == null || page.isEmpty()) {
			return;
		}

		int currentY = startY;
		int maxWidth = 0;

		if (centeredMaxWidth) {
			for (MDComponent mdComponent : page) {
				maxWidth = Math.max(maxWidth, mdComponent.width);
			}
		}

		for (MDComponent mdComponent : page) {
			Objects.requireNonNull(mdComponent, "MDComponent cannot be null");
			int renderX = xOffset;

			if (centered) {
				if (centeredMaxWidth) {
					renderX = (width - maxWidth) / 2 + xOffset;
				} else {
					renderX = (width - mdComponent.width) / 2 + xOffset;
				}
			}

			mdComponent.render(renderX, currentY + yOffset, mouseX, mouseY);
			currentY += mdComponent.height;
		}
	}

	protected void applyScissor(int guiX, int guiY, int guiWidth, int guiHeight) {
		float scaleX = (float) mc.gameWindow.getWidthPixels() / (float) width;
		float scaleY = (float) mc.gameWindow.getHeightPixels() / (float) height;

		int scissorX = (int) (guiX * scaleX);
		int scissorY = (int) ((height - guiY - guiHeight) * scaleY);
		int scissorWidth = (int) (guiWidth * scaleX);
		int scissorHeight = (int) (guiHeight * scaleY);

		GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
	}


}
