package deus.guidebookmd.components;

import org.lwjgl.opengl.GL11;

public class MDImage extends MDComponent {
	protected String path = "";

	public MDImage(String text, String path, int height, int width) {
		this.path = path;
		this.height = height;
		this.width = width;
	}

	@Override
	public void render(int x, int y) {
		super.render(x, y);
		GL11.glDisable(GL11.GL_BLEND);

		mc.textureManager.loadTexture(path).bind();
		drawTexturedModalRect(x, y, 0, 0, width, height);
	}
}
