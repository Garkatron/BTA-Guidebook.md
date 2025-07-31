package deus.guidebookmd.gui;

import deus.guidebookmd.formats.MarkdownCompiler;
import deus.guidebookmd.components.MDComponent;
import deus.guidebookmd.utils.RLoading;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.core.sound.SoundCategory;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MarkdownGuidebook<E extends MDPage> extends MarkdownBook {


	protected int currentPageNumber = -1;
	protected final List<E> pages = new ArrayList<>();

	// ? Load markdown
	public MarkdownGuidebook() {

	}

	@Override
	public void init() {
		super.init();
		xOffset = (width - 158) / 2;
		yOffset = (height - 220) / 2;

		shareReferenceToComponents();
	}


	@Override
	public void render(int mx, int my, float partialTick) {
		if (currentPageNumber != -1) currentPage = getCurrentPage().mdComponents;

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPushMatrix();

		// ? Draw cover page
		if (currentPageNumber == -1) {
			mc.textureManager.loadTexture(config.frontPage).bind();
			drawTexturedModalRect(xOffset, yOffset, 0, 0, config.frontBackPageWH[0], config.frontBackPageWH[1]);
		} else {
			mc.textureManager.loadTexture(config.backPage).bind();
			drawTexturedModalRect(xOffset + config.backPageOffsets[0], yOffset + config.backPageOffsets[1], 0, 0, config.frontBackPageWH[0], config.frontBackPageWH[1]);

			// ? Draw pages
			if (pages.isEmpty()) return;

			for (int i = 0; i < config.pageTexturePositions.length; i++) {
				int pageIndex = currentPageNumber + i;
				if (pageIndex < pages.size()) {

					// ? Get configs
					int textX = config.textXPositions.length > i ? config.textXPositions[i] : 0;
					int textY = config.textYPositions.length > i ? config.textYPositions[i] : 0;
					int textureX = config.pageTexturePositions.length > i ? config.pageTexturePositions[i] : 0;

					E page = pages.get(pageIndex);

					// ? Avoid 2 buttons in each page
					if (config.pairButtons && pageIndex % 2 == 0) {
						page.hasNextButton = true;
						page.hasPreviousButton = false;
					} else if (config.pairButtons) {
						page.hasNextButton = false;
						page.hasPreviousButton = true;
					}

					// ? Update mouse pos
					page.updateMousePos(mx, my);
					page.update();
					page.number = pageIndex;
					page.x = xOffset;
					page.y = yOffset;

					// ? Render
					page.render(textX, textY, textureX);
				}
			}
		}

		GL11.glPopMatrix();
	}

	// ? Logic functions
	public void goBack() {
		currentPageNumber -= config.pageSkipAmount;
		if (currentPageNumber < 0) currentPageNumber = -1;
		playPageSound();
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
		super.mouseClicked(mx, my, buttonNum);
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
		this.pages.add((E) MarkdownCompiler.compile(path, getClass()));
	}


	public void loadMarkdownPages(String... paths) {
		for (String path : paths) {
			loadMarkdownPage(path);
		}
	}

	public E getCurrentPage() {
		if (currentPageNumber >= pages.size() || currentPageNumber < 0) {
			return pages.isEmpty() ? null : pages.get(0);
		}
		return pages.get(currentPageNumber);
	}
	public void playPageSound() {
		Random r = new Random();
		this.mc.sndManager.playSound("random.page", SoundCategory.GUI_SOUNDS, 0.8F, 0.9F + (r.nextFloat() - r.nextFloat()) * 0.1F);
	}

	protected void shareReferenceToComponents() {
		for (MDPage page : pages) {
			page.setScreen(this);
			for (MDComponent mdComponent : page.mdComponents) {
				mdComponent.setScreen(this);
			}
		}
	}


}
