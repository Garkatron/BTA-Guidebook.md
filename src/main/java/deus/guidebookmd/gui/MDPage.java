package deus.guidebookmd.gui;

import deus.guidebookmd.components.MDComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Objects;

import static deus.guidebookmd.gui.Utils.applyScissor;

public class MDPage extends Gui {
	public MDPageConfig config;
	List<MDComponent> mdComponents;
	protected MDBookScreen screen;
	protected Minecraft mc;

	public MDPage(MDPageConfig config, List<MDComponent> mdComponents) {
		this.config = config;
		this.mdComponents = mdComponents;
		this.mc = Minecraft.getMinecraft();
	}

	public void setScreen(MDBookScreen screen) {
		this.screen = screen;
	}

	public void render(int mx, int my, int x, int y, int textXPos, int textYPos, int pageXTexturePos) {
		MDBookConfig bkConfig = screen.config;
		mc.textureManager.loadTexture(config.pageTexture==null ? bkConfig.defaultPageTexture : config.pageTexture).bind();

		drawTexturedModalRect(x + pageXTexturePos, y, 0, 0, config.pageTextureWidth, config.pageTextureHeight);

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		applyScissor(mc,x + textXPos, y, config.scissorWH[0], config.scissorWH[1], screen.width, screen.height);

		drawPage(mdComponents,x + textXPos, y +textYPos, mx, my, screen.width, screen.yOffset, false, false);

		GL11.glDisable(GL11.GL_SCISSOR_TEST);
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

}
