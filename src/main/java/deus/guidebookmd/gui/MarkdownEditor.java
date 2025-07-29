package deus.guidebookmd.gui;

import deus.guidebookmd.MarkdownCompiler;
import deus.guidebookmd.components.MDComponent;
import deus.guidebookmd.config.PageConfig;
import deus.guidebookmd.gui.elements.TextArea;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class MarkdownEditor extends MarkdownBook {


	// List<MDComponent> mdComponents = new ArrayList<>();
	MDPage page = new MDPage(new PageConfig(), new ArrayList<>());
	TextArea textArea = new TextArea();

	@Override
	public void init() {
		super.init();


	}

	public MarkdownEditor() {
		textArea.x = 0;
		textArea.y = 0;
		textArea.drawBackground = false;
		textArea.autoWrap = false;
		textArea.maxTextLength = 26;

	}

	@Override
	public void render(int mx, int my, float partialTick) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		textArea.width = width/2;
		textArea.height = height;
		page = MarkdownCompiler.compile(textArea.getLines());

		textArea.updateMousePos(mx,my);
		textArea.update();
		textArea.render();

		page.setScreen(this);
		for (MDComponent mdComponent : page.mdComponents) {
			mdComponent.setScreen(this);
		}
		page.updateMousePos(mx, my);
		page.x = xOffset;
		page.y = yOffset;

		page.render(xOffset, yOffset, this.textArea.width);
		//renderMarkdownCOmponents(mdComponents, textArea.width + 20, 0, mx,my,(int)width/2, 0, false, false);

	}

	public void mouseClicked(int mx, int my, int buttonNum) {
		page.mouseClick(mx, my);
	}

	@Override
	public void keyPressed(char eventCharacter, int eventKey, int mx, int my) {

	}


}

