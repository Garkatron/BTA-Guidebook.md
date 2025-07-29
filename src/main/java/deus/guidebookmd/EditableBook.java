package deus.guidebookmd;

import deus.guidebookmd.config.BookConfig;
import deus.guidebookmd.gui.MDEditablePage;
import deus.guidebookmd.gui.MDPage;
import deus.guidebookmd.gui.MarkdownGuidebook;
import net.minecraft.client.gui.ButtonElement;

import java.util.ArrayList;

public class EditableBook extends MarkdownGuidebook<MDEditablePage> {

	public EditableBook() {
		String path = "/assets/guidebookmd/markdown/mdbook/";
		config = BookConfig.fromJsonResource(getClass(), path + "config.json");
		addPage();
	}

	public void addPage() {
		pages.add(new MDEditablePage(null, new ArrayList<>()));
		shareReferenceToComponents();
	}

	public void subPage() {
		pages.forEach((p)->{
			if (p.textArea.isFocused()) pages.remove(p);
		});
	}

	public void lock() {
		pages.forEach((p)->{
			if (p.textArea.isFocused()) p.editable = !p.editable;
		});
	}

	@Override
	public void render(int mx, int my, float partialTick) {
		super.render(mx, my, partialTick);
		if (currentPageNumber != -1) {
			for (ButtonElement buttonElement : this.buttons) {
				((ButtonElement) buttonElement).drawButton(this.mc, mx, my);
			}
		}
	}

	@Override
	public void init() {
		super.init();

	}

	@Override
	protected void buttonClicked(ButtonElement button) {


	}
}
