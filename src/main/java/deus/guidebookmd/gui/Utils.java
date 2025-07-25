package deus.guidebookmd.gui;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class Utils {
	static void applyScissor(Minecraft mc, int guiX, int guiY, int guiWidth, int guiHeight, int width, int height) {
		float scaleX = (float) mc.gameWindow.getWidthPixels() / (float) width;
		float scaleY = (float) mc.gameWindow.getHeightPixels() / (float) height;

		int scissorX = (int) (guiX * scaleX);
		int scissorY = (int) ((height - guiY - guiHeight) * scaleY);
		int scissorWidth = (int) (guiWidth * scaleX);
		int scissorHeight = (int) (guiHeight * scaleY);

		GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
	}
}
