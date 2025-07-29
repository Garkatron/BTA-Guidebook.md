package deus.guidebookmd.gui;

import deus.guidebookmd.config.BookConfig;

public abstract class MarkdownBook extends MDScreen {
	protected BookConfig config = new BookConfig(c -> {
	});



	public int xOffset;
	public int yOffset;

	public void goTo(int pageNumber) {
	}

	public void goNext() {

	}

	public void goBack() {

	}
}
