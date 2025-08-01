package deus.guidebookmd;

import deus.guidebookmd.gui.MDPage;
import deus.guidebookmd.gui.MarkdownGuidebook;
import deus.guidebookmd.config.BookConfig;

import java.nio.file.Path;
import java.text.DecimalFormat;

public class IntroBook extends MarkdownGuidebook<MDPage> {

	public IntroBook() {
		String path = "/assets/guidebookmd/markdown/mdbook/";
		config = BookConfig.fromJsonResource(getClass(), path + "config.json");

		DecimalFormat formatter = new DecimalFormat("000");
		for (int i = 0; i<11; i++) {
			String fileName = "page_" + formatter.format(i + 1) + ".md";

			loadMarkdownPages(path + fileName);
		}
	}
}
