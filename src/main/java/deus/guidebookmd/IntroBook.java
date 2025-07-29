package deus.guidebookmd;

import deus.guidebookmd.gui.MDPage;
import deus.guidebookmd.gui.MarkdownGuidebook;
import deus.guidebookmd.config.BookConfig;

public class IntroBook extends MarkdownGuidebook<MDPage> {

	public IntroBook() {
		String path = "/assets/guidebookmd/markdown/mdbook/";
		config = BookConfig.fromJsonResource(getClass(), path + "config.json");
		String[] pages = {
			"index.md",
			"intro.md",
			"syntax.md",
			"specials.md",
			"formats.md",
			"comingsoon.md",
			"details.md"
		};
		for (String page : pages) {
			loadMarkdownPages(path + page);
		}
	}
}
