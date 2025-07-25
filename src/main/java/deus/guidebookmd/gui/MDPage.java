package deus.guidebookmd.gui;

import deus.guidebookmd.components.MDComponent;
import deus.guidebookmd.gui.elements.MDBookConfig;
import deus.guidebookmd.gui.elements.MDGui;
import deus.guidebookmd.gui.elements.MDPageConfig;
import deus.guidebookmd.gui.elements.MDPageTurnIndicator;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Objects;

import static deus.guidebookmd.gui.Utils.applyScissor;

public class MDPage extends MDGui {
	public MDPageConfig config;
	public int x = 0;
	public int y = 0;
	public MDBookScreen screen;
	List<MDComponent> mdComponents;
	private final MDPageTurnIndicator button0 = new MDPageTurnIndicator(0, this);
	private final MDPageTurnIndicator button1 = new MDPageTurnIndicator(1, this);

	public MDPage(MDPageConfig config, List<MDComponent> mdComponents) {
		this.config = config;
		this.mdComponents = mdComponents;
	}

	public static void drawPage(List<MDComponent> page, int x, int y, int mouseX, int mouseY, int width, int yOffset, boolean centered, boolean centeredMaxWidth) {
		if (page == null || page.isEmpty()) {
			return;
		}

		int currentY = y;
		int maxWidth = 0;

		if (centeredMaxWidth) {
			for (MDComponent mdComponent : page) {
				maxWidth = Math.max(maxWidth, mdComponent.width);
			}
		}

		for (MDComponent mdComponent : page) {
			Objects.requireNonNull(mdComponent, "MDComponent cannot be null");
			int renderX = x;

			if (centered) {
				if (centeredMaxWidth) {
					renderX = (width - maxWidth) / 2 + x;
				} else {
					renderX = (width - mdComponent.width) / 2 + x;
				}
			}

			mdComponent.render(renderX, currentY + yOffset, mouseX, mouseY);
			currentY += mdComponent.height;
		}
	}

	public void setScreen(MDBookScreen screen) {
		this.screen = screen;
	}

	public void render(int mx, int my, int x, int y, int textXPos, int textYPos, int pageXTexturePos) {
		MDBookConfig bkConfig = screen.config;
		mc.textureManager.loadTexture(config.pageTexture == null ? bkConfig.defaultPageTexture : config.pageTexture).bind();

		button0.texture = config.pageTexture;
		button1.texture = config.pageTexture;

		drawTexturedModalRect(x + this.x + pageXTexturePos, y + this.y, 0, 0, config.pageTextureWidth, config.pageTextureHeight);

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		applyScissor(mc, x + this.x + textXPos, y + this.y, config.scissorWH[0], config.scissorWH[1], screen.width, screen.height);

		drawPage(mdComponents, x + this.x + textXPos, y + this.y + textYPos, mx, my, screen.width, screen.yOffset, config.centered, screen.centeredMaxWidth);

		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		if (config.hasNextButton) {
			button0.x = x + this.x + pageXTexturePos + 24;
			button0.y = y + this.y + config.pageTextureHeight;
			button0.render();
		}

		if (config.hasPreviousButton) {
			button1.x = x + this.x + pageXTexturePos + (int) (config.pageTextureWidth );
			button1.y = y + this.y + config.pageTextureHeight;

			button1.render();
		}

	}

	@Override
	public void updateMousePos(int mx, int my) {
		button0.updateMousePos(mx, my);
		button1.updateMousePos(mx, my);
	}

	@Override
	public void mouseClick(int mx, int my) {
		button0.mouseClick(mx, my);
		button1.mouseClick(mx, my);
	}
}
