package deus.guidebookmd.gui.elements;

import deus.guidebookmd.gui.MDPage;
import net.minecraft.client.gui.paged.Page;
import org.lwjgl.opengl.GL11;

public class PageTurnIndicator extends PageButton {

	public int type = 0;
	public PageTurnIndicator(int type, MDPage page) {
		super(page, "");
		this.type = type;
		size=24;
	}




	@Override
	public void render() {
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
		super.mouseClick(mx, my);

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
