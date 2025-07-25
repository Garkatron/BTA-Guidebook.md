package deus.guidebookmd.gui;

import deus.guidebookmd.MarkdownCompiler;
import deus.guidebookmd.components.MDComponent;
import deus.guidebookmd.gui.elements.MDBookConfig;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.core.sound.SoundCategory;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MDBookScreen extends MDScreen {

	protected final List<MDPage> pages = new ArrayList<>();
	public int xOffset;
	public int yOffset;
	protected int currentPageNumber = -1;
	protected MDBookConfig config = new MDBookConfig(c->{});



	// ? Load markdown
	public MDBookScreen() {
		config = MDBookConfig.fromJsonResource(getClass(), "/assets/guidebookmd/markdown/default.json");

		loadMarkdownPages("/assets/guidebookmd/markdown/test.md", "/assets/guidebookmd/markdown/test2.md",
			"/assets/guidebookmd/markdown/test3.md",
			"/assets/guidebookmd/markdown/test4.md",
			"/assets/guidebookmd/markdown/test5.md");
		shareReferenceToComponents();

	}

	@Override
	public void init() {
		super.init();
		xOffset = (width - 158) / 2;
		yOffset = (height - 220) / 2;

		//currentPage = getCurrentPage().mdComponents;

	}

	@Override
	public void render(int mx, int my, float partialTick) {
		if (currentPageNumber != -1) currentPage = getCurrentPage().mdComponents;

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPushMatrix();

		if (currentPageNumber == -1) {
			mc.textureManager.loadTexture(config.frontPage).bind();
			drawTexturedModalRect(xOffset, yOffset, 0, 0, config.frontBackPageWH[0], config.frontBackPageWH[1]);
		} else {
			// ? Draw pages
			for (int i = 0; i < config.pageTexturePositions.length; i++) { // 2 pages max
				int pageIndex = currentPageNumber + i;
				if (pageIndex < pages.size()) {
					// ? Get configs
					int textX = config.textXPositions.length > i ? config.textXPositions[i] : 0;
					int textY = config.textYPositions.length > i ? config.textYPositions[i] : 0;
					int textureX = config.pageTexturePositions.length > i ? config.pageTexturePositions[i] : 0;

					MDPage page = pages.get(pageIndex);

					// ? Avoid 2 buttons in each page
					if (pageIndex % 2 == 0) {
						page.config.hasNextButton = true;
						page.config.hasPreviousButton = false;
					} else {
						page.config.hasNextButton = false;
						page.config.hasPreviousButton = true;
					}

					// ? Update mouse pos
					page.updateMousePos(mx, my);

					// ? Render
					page.render(mx, my, xOffset, yOffset, textX, textY, textureX);
				}
			}
		}

		GL11.glPopMatrix();
	}




	// ? Logic functions
	public void goBack() {
		if (currentPageNumber == -1) {
			currentPageNumber = 0;
		} else {
			currentPageNumber -= config.pageSkipAmount;
			if (currentPageNumber < 0) currentPageNumber = pages.size() - 1;
			playPageSound();
		}

	}

	public void goNext() {
		if (currentPageNumber == -1) {
			currentPageNumber = 0;
		} else {
			currentPageNumber += config.pageSkipAmount;
			if (currentPageNumber >= pages.size()) currentPageNumber = 0;
			playPageSound();
		}
	}

	public void goTo(int pageNumber) {
		currentPageNumber = pageNumber;

		if (currentPageNumber % config.pageSkipAmount != 0) {
			currentPageNumber--;
		}

		if (currentPageNumber < 0) currentPageNumber = 0;
		if (currentPageNumber >= pages.size()) currentPageNumber = 0;

		int diff = currentPageNumber - pageNumber;
		for (int i = 0; i < diff; i++) {
			playPageSound();
		}
	}

	// ? Interaction
	@Override
	protected void buttonReleased(ButtonElement button) {
		super.buttonReleased(button);
		if (button.id == 0 && currentPageNumber > 0) {
			goBack();
		}
		if (button.id == 1 && currentPageNumber + 1 < pages.size()) {
			goNext();
		}
	}

	@Override
	public void mouseClicked(int mx, int my, int buttonNum) {
		if (currentPageNumber == -1) {
			currentPageNumber = 0;
		} else {
			for (int i = 0; i < config.pageTexturePositions.length; i++) {
				int pageIndex = currentPageNumber + i;
				if (pageIndex < pages.size()) {
					MDPage page = pages.get(pageIndex);

					page.mouseClick(mx, my);
				}
			}
		}
	}

	// ? Util functions
	public void loadMarkdownPage(String path) {
		this.pages.add(MarkdownCompiler.compile(path, getClass()));
	}

	public void loadMarkdownPages(String... paths) {
		for (String path : paths) {
			loadMarkdownPage(path);
		}
	}

	public MDPage getCurrentPage() {
		return pages.get(currentPageNumber);
	}

	public void playPageSound() {
		Random r = new Random();
		this.mc.sndManager.playSound("random.page", SoundCategory.GUI_SOUNDS, 0.8F, 0.9F + (r.nextFloat() - r.nextFloat()) * 0.1F);
	}

	private void shareReferenceToComponents() {
		for (MDPage page : pages) {
			page.setScreen(this);
			for (MDComponent mdComponent : page.mdComponents) {
				mdComponent.setScreen(this);
			}
		}
	}
}
