package deus.guidebookmd.components;

import org.lwjgl.opengl.GL11;

public class MDTitle extends MDText {

	protected float scale = 1f;

	public MDTitle(String text, float scale) {
		super(text);
		this.scale = scale;
		this.height = (int) Math.ceil((mc.font.fontHeight * scale) + 9);
	}

	@Override
	public void render(int x, int y, int mx, int my) {
		GL11.glPushMatrix();
		GL11.glScalef(scale, scale, 1.0f);
		super.render((int) (x / scale), (int) (y / scale), mx, my);
		GL11.glPopMatrix();
	}

}
