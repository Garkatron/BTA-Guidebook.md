package deus.guidebookmd.gui;

import deus.guidebookmd.components.MDComponent;
import deus.guidebookmd.gui.elements.TextArea;
import net.minecraft.client.gui.Screen;
import org.lwjgl.opengl.GL11;

import static deus.guidebookmd.gui.MDPage.drawPage;

import java.util.ArrayList;
import java.util.List;

public class MarkdownEditor extends Screen {

	List<MDComponent> mdComponents = new ArrayList<>();
	TextArea textArea = new TextArea();

	@Override
	public void init() {
		super.init();
	}

	public MarkdownEditor() {
		textArea.text.add("Aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		textArea.x = 0;
		textArea.y = 0;

	}

	@Override
	public void render(int mx, int my, float partialTick) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		textArea.width = width/2;
		textArea.height = height;

		//drawPage(mdComponents, (int)width/2, 0, mx,my,(int)width/2, 0, false, false);
		textArea.updateMousePos(mx,my);
		textArea.update();
		textArea.render();
		super.render(mx,my,partialTick);
	}
}
