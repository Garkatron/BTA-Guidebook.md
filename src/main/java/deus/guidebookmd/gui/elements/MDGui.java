package deus.guidebookmd.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class MDGui extends Gui {
	protected Minecraft mc = Minecraft.getMinecraft();
	public int x = 0;
	public int y = 0;
	public int mx = 0;
	public int my = 0;
	public int width = 0;
	public int height = 0;



	public void render() {

	}

	public void update() {

	}

	public void updateMousePos(int mx, int my) {
		this.mx = mx;
		this.my = my;
	}

	public void mouseClick(int mx, int my) {

	}

}
