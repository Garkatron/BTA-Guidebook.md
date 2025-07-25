package deus.guidebookmd.gui.elements;

import deus.guidebookmd.gui.MDPage;
import org.lwjgl.opengl.GL11;

public class MDPageTurnIndicator extends MDGui {
	public String texture = "";
	public int type = 0;
	private final MDPage page; // Reference to parent MDPage to access screen

	public MDPageTurnIndicator(int type, MDPage page) {
		this.type = type;
		this.page = page;
	}

	public MDPageTurnIndicator() {
		this.page = null;
	}

	@Override
	public void render() {
		int size = 24;

		this.mc.textureManager.bindTexture(this.mc.textureManager.loadTexture(texture));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		int left = x - size;
		int top = y - size;


		if (mx >= left && mx <= left + size && my >= top && my <= top + size) {
			switch (type) {
				case 0:
					this.drawTexturedModalRect(left, top, 48, 220, size, size);
					break;
				case 1:
					this.drawTexturedModalRect(left, top, 72, 220, size, size);
					break;
			}
		}
	}


	@Override
	public void mouseClick(int mx, int my) {
		if (page == null || page.screen == null) return;

		int size = 24;
		int left = x - size;
		int top = y - size;

		if (mx >= left && mx <= left + size && my >= top && my <= top + size) {
			if (type ==1) {
				page.screen.goNext();
			} else if (type == 0) {
				page.screen.goBack();
			}
		}
	}
}
