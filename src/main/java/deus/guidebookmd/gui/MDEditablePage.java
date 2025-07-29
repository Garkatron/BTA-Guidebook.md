package deus.guidebookmd.gui;

import deus.guidebookmd.MarkdownCompiler;
import deus.guidebookmd.components.MDComponent;
import deus.guidebookmd.config.PageConfig;
import deus.guidebookmd.gui.elements.TextArea;

import java.util.List;

public class MDEditablePage extends MDPage {

	boolean editable = false;
	TextArea textArea = new TextArea();

	public MDEditablePage(PageConfig config, List<MDComponent> mdComponents) {
		super(config, mdComponents);


		textArea.drawBackground = false;
		textArea.autoWrap = false;
		textArea.maxTextLength = 22;
		disableScissor = true;
	}

	@Override
	public void render(int textXPos, int textYPos, int pageXTexturePos) {
		super.render(textXPos, textYPos, pageXTexturePos);
		textArea.x = this.x + textXPos;
		textArea.y = this.y + textYPos;
		PageConfig pageConfig = config == null ? screen.config.defaultPageConfig : config;
		textArea.width = pageConfig.pageTextureWidth - 25;
		textArea.height = pageConfig.pageTextureHeight - 10;


		if (editable) {
			textArea.update();
			mdComponents = MarkdownCompiler.compile(textArea.getLines()).mdComponents;
			config = MarkdownCompiler.compile(textArea.getLines()).config;
		}
	}

	@Override
	public void updateMousePos(int mx, int my) {
		super.updateMousePos(mx, my);
		if (editable) {
			textArea.updateMousePos(mx,my);
		}
	}

	@Override
	protected void drawMarkdown(int textXPos, int textYPos, boolean centered, boolean centeredMaxWidth) {

		if (editable) {
			textArea.render();
		} else {
			super.drawMarkdown(textXPos, textYPos, centered, centeredMaxWidth);


		}
	}
}
