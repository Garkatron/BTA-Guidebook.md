package deus.guidebookmd.components;

import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import org.lwjgl.opengl.GL11;

public class MDImage extends MDComponent {
	protected String path = "";
	protected String type = "default";


	public MDImage(String text, String path, int height, int width, String type) {
		this.path = path;
		this.height = height;
		this.width = width;
		this.type = type;
	}

	@Override
	public void render(int x, int y) {
		super.render(x, y);
		GL11.glDisable(GL11.GL_BLEND);

		mc.textureManager.loadTexture(path).bind();
		if (type.equals("default")) {
			drawTexturedModalRect(x, y, 0, 0, width, height);
		} else if (type.equals("icon")) {
			drawTexturedIcon(x,y,width, height, TextureRegistry.getTexture(path));
		}
	}
}
