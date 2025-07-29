package deus.guidebookmd.gui;

import deus.guidebookmd.components.MDComponent;
import deus.guidebookmd.config.BookConfig;
import deus.guidebookmd.gui.elements.MDGui;
import deus.guidebookmd.config.PageConfig;
import deus.guidebookmd.gui.elements.PageButton;
import deus.guidebookmd.gui.elements.PageTurnIndicator;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Objects;

import static deus.guidebookmd.utils.RenderUtils.applyScissor;

public class MDPage extends MDGui {
	public PageConfig config;

	public int number = 0;
	public boolean hasNextButton = true;
	public boolean hasPreviousButton = true;
	public MarkdownBook screen;
	public boolean disableScissor = false;
	List<MDComponent> mdComponents;
	private final PageTurnIndicator button0 = new PageTurnIndicator(0, this);
	private final PageTurnIndicator button1 = new PageTurnIndicator(1, this);

	public MDPage(PageConfig config, List<MDComponent> mdComponents) {
		this.config = config;
		this.mdComponents = mdComponents;
	}


	public static void renderMarkdownComponents(List<MDComponent> page, int x, int y, int mouseX, int mouseY, int width, int yOffset, boolean centered, boolean centeredMaxWidth) {
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

	public void setScreen(MarkdownBook screen) {
		this.screen = screen;
	}

	public void render(int textXPos, int textYPos, int pageXTexturePos) {
		// ? Book config
		BookConfig bkConfig = screen.config;

		// ? Page config
		PageConfig pageConfig = config == null ? bkConfig.defaultPageConfig : config;

		mc.textureManager.loadTexture(pageConfig.pageTexture).bind();

		// ? Get button texture
		button0.texture = pageConfig.pageTexture;
		button1.texture = pageConfig.pageTexture;

		// ? Draw background
		drawTexturedModalRect(this.x + pageXTexturePos, this.y, 0, 0, pageConfig.pageTextureWidth, pageConfig.pageTextureHeight);

		// ? Draw page number
		drawString(mc.font, number + "", this.x + textXPos, this.y - 9, 0xffffff);

		// ? Apply mask to avoid text overflow
		if (!disableScissor) {
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			applyScissor(mc, this.x + textXPos, this.y, pageConfig.scissorWH[0], pageConfig.scissorWH[1], screen.width, screen.height);
		}

		// ? Draw markdown text
		drawMarkdown(this.x + textXPos, textYPos, pageConfig.centered, screen.centeredMaxWidth);

		if (!disableScissor) {
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		}

		// ? Avoid draw 2 buttons
		if (hasNextButton) {
			button0.x = this.x + pageXTexturePos + 24;
			button0.y = this.y + pageConfig.pageTextureHeight;
			button0.render();
		}

		if (hasPreviousButton) {
			button1.x = this.x + pageXTexturePos + pageConfig.pageTextureWidth;
			button1.y = this.y + pageConfig.pageTextureHeight;

			button1.render();
		}
	}

	protected void drawMarkdown(int textXPos, int textYPos, boolean centered, boolean centeredMaxWidth) {
		renderMarkdownComponents(mdComponents, textXPos, textYPos, mx, my, screen.width, screen.yOffset, centered, centeredMaxWidth);
	}

	@Override
	public void updateMousePos(int mx, int my) {
		super.updateMousePos(mx, my);
		button0.updateMousePos(mx, my);
		button1.updateMousePos(mx, my);
	}

	@Override
	public void mouseClick(int mx, int my) {
		super.mouseClick(mx, my);
		button0.mouseClick(mx, my);
		button1.mouseClick(mx, my);
	}
}
