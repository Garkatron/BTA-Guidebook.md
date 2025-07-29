package deus.guidebookmd.gui;

import deus.guidebookmd.MarkdownCompiler;
import deus.guidebookmd.components.MDComponent;
import deus.guidebookmd.config.PageConfig;
import deus.guidebookmd.gui.elements.PageButton;
import deus.guidebookmd.gui.elements.TextArea;

import java.util.ArrayList;
import java.util.List;

public class MDEditablePage extends MDPage {

	public boolean editable = true;
	public TextArea textArea = new TextArea();
	private PageButton deleteButton = null;
	private PageButton lockButton = null;
	private PageButton addButton = null;
	private PageButton moveLeftButton = null;
	private PageButton moveRightButton = null;

	private final PageButton[] buttons = {
		addButton = new PageButton(this, "guidebookmd:gui/hud/add"),
		lockButton = new PageButton(this, "guidebookmd:gui/hud/lock"),
		deleteButton = new PageButton(this, "guidebookmd:gui/hud/delete"),
		moveLeftButton = new PageButton(this, "guidebookmd:gui/hud/moveleft"),
		moveRightButton = new PageButton(this, "guidebookmd:gui/hud/moveright"),
	};

	public MDEditablePage(PageConfig config, List<MDComponent> mdComponents) {
		super(config, mdComponents);

		moveLeftButton.onClick = () -> {
			if (screen instanceof MarkdownGuidebook) {
				MarkdownGuidebook<MDEditablePage> p = (MarkdownGuidebook<MDEditablePage>) screen;
				if (number <= 0) return;

				MDEditablePage a = p.pages.get(number);
				MDEditablePage b = p.pages.get(number - 1);
				p.pages.set(number, b);
				p.pages.set(number - 1, a);
			}
		};

		moveRightButton.onClick = () -> {
			if (screen instanceof MarkdownGuidebook) {
				MarkdownGuidebook<MDEditablePage> p = (MarkdownGuidebook<MDEditablePage>) screen;

				int index = p.currentPageNumber;
				if (index >= p.pages.size() - 1) return;

				MDEditablePage a = p.pages.get(index);
				MDEditablePage b = p.pages.get(index + 1);
				p.pages.set(index, b);
				p.pages.set(index + 1, a);
			}
		};


		deleteButton.onClick = () -> {
			if (screen instanceof MarkdownGuidebook) {
				MarkdownGuidebook<MDEditablePage> p = (MarkdownGuidebook<MDEditablePage>) screen;
				if (p.pages.size() > 1) {
					int index = p.pages.indexOf(this);
					p.pages.remove(this);
					p.goTo(p.pages.size() - 1);

				}
			}
		};
		lockButton.onClick = () -> {
			editable = !editable;
			if (editable) {
				lockButton.texture = "guidebookmd:gui/hud/lock";
				deleteButton.texture = "guidebookmd:gui/hud/delete";

			} else {
				lockButton.texture = "guidebookmd:gui/hud/unlock";
				deleteButton.texture = "guidebookmd:gui/hud/cant_delete";
			}
		};

		addButton.onClick = () -> {
			if (screen instanceof MarkdownGuidebook) {
				MarkdownGuidebook<MDEditablePage> p = (MarkdownGuidebook<MDEditablePage>) screen;
				MDEditablePage page = new MDEditablePage(new PageConfig(), new ArrayList<>());
				page.setScreen(screen);
				p.pages.add(page);
				p.goTo(p.pages.size() - 1);
			}
		};

		textArea.drawBackground = false;
		textArea.autoWrap = false;
		textArea.maxTextLength = 22;
		textArea.maxLines = 22;
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
			mdComponents = MarkdownCompiler.compile(textArea.getLines()).mdComponents;
			config = MarkdownCompiler.compile(textArea.getLines()).config;
		}

		int buttonY = this.y + textYPos - 18;
		int startX = this.x + textXPos + pageConfig.pageTextureWidth - 128;
		int base = 16;

		addButton.x = startX;
		lockButton.x = startX + base;
		deleteButton.x = startX + base * 2;
		moveLeftButton.x = startX + base * 3;
		moveRightButton.x = startX + base * 4;

		addButton.y = lockButton.y = deleteButton.y = moveRightButton.y = moveLeftButton.y = buttonY;

		for (PageButton button : buttons) {

			button.updateMousePos(mx, my);
			button.render();
		}

	}

	@Override
	public void update() {
		super.update();
		if (editable) {
			textArea.updateMousePos(mx, my);
			textArea.update();
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

	@Override
	public void mouseClick(int mx, int my) {
		super.mouseClick(mx, my);
		for (PageButton button : buttons) {

			button.mouseClick(mx, my);
		}


	}
}
