package deus.guidebookmd.gui.elements;

import deus.guidebookmd.gui.MDPage;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import org.lwjgl.opengl.GL11;

public class PageButton extends MDGui {
	public String texture = "";

	public boolean visible = true;
	public boolean disabled = false;
	protected final MDPage page; // Reference to parent MDPage to access screen
	public Runnable onClick = ()->{};
	public int size = 16;

	public PageButton(MDPage page, String texture) {
		this.page = page;
		this.texture = texture;
	}

	public PageButton() {
		this.page = null;
	}

	@Override
	public void render() {
		if (!visible || disabled) return;
		this.mc.textureManager.bindTexture(this.mc.textureManager.loadTexture(texture));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		drawTexturedIcon(this.x, this.y, this.size,this.size, TextureRegistry.getTexture(texture));

	}


	@Override
	public void mouseClick(int mx, int my) {
		if (page == null || page.screen == null || disabled) return;
		super.mouseClick(mx, my);

		int left = x;
		int top = y;

		if (mx >= left && mx <= left + size && my >= top && my <= top + size) {
			onClick.run();
		}
	}


}
