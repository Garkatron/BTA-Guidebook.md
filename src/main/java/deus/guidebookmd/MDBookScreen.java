package deus.guidebookmd;

import deus.guidebookmd.components.MDComponent;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Screen;
import net.minecraft.core.sound.SoundCategory;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MDBookScreen extends MarkdownScreen {

	private int x;
	private int y;
	protected int currentPageNumber = 0;
	protected boolean darkMode = true;
	private String pageTexture = "/assets/minecraft/textures/gui/container/guidebook/guidebook.png";

	public MDBookScreen() {
		loadMarkdownPages("/assets/guidebookmd/markdown/test.md","/assets/guidebookmd/markdown/test2.md",
			"/assets/guidebookmd/markdown/test3.md",
			"/assets/guidebookmd/markdown/test4.md",
			"/assets/guidebookmd/markdown/test5.md");

	}

	@Override
	public void init() {
		super.init();
		x = (width - 158) / 2;
		y = (height - 220) / 2;


		currentPage = pages.get(currentPageNumber);

		centered = true;
		xOffset = -150;

		if (darkMode) {
			pageTexture = "/assets/guidebookmd/textures/gui/dark_guidebook.png";
		}
	}

	@Override
	public void render(int mx, int my, float partialTick) {

		currentPage = pages.get(currentPageNumber);

		startY = y + 10;


		GL11.glDisable(GL11.GL_BLEND);

		mc.textureManager.loadTexture(pageTexture).bind();


		drawTexturedModalRect(x - 79, y, 0, 0, 158, 220);
		drawTexturedModalRect(x + 79, y, 0, 0, 158, 220);

		int rightPageIndex = currentPageNumber;
		if (currentPageNumber + 1 < pages.size()) {
			rightPageIndex = currentPageNumber + 1;
			drawPage(pages.get(rightPageIndex), startY, xOffset + 150+8);
		}

		drawPageTurnIndicator(mx,my,0);

		super.render(mx, my, partialTick);
	}

	public void loadMarkdownPage(String path) {
		this.pages.add(MarkdownCompiler.compile(path, getClass()));

	}
	public void loadMarkdownPages(String... paths) {
		for (String path : paths) {
			loadMarkdownPage(path);
		}
	}

	private void drawPageTurnIndicator(int mouseX, int mouseY, int xOffset) {
		int size = 24;
		int top = this.height / 2 - 110;
		int bottom = this.height / 2 + 110;
		int left = this.width / 2 - 158 + xOffset;
		int right = this.width / 2 + 158 + xOffset;
		this.mc.textureManager.bindTexture(this.mc.textureManager.loadTexture(pageTexture));
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//
//			if (mouseX >= left && mouseX <= left + size && mouseY >= top && mouseY <= top + size) {
//				this.drawTexturedModalRect(left, top, 0, 220, 24, 24);
//			}

			if (mouseX >= left && mouseX <= left + size && mouseY >= bottom - size && mouseY <= bottom) {
				this.drawTexturedModalRect(left, bottom - size, 48, 220, 24, 24);
			}

//		if (mouseX >= right - size && mouseX <= right && mouseY >= top && mouseY <= top + size) {
//			this.drawTexturedModalRect(right - size, top, 24, 220, 24, 24);
//		}

		if (mouseX >= right - size && mouseX <= right && mouseY >= bottom - size && mouseY <= bottom) {
			this.drawTexturedModalRect(right - size, bottom - size, 72, 220, 24, 24);
		}

	}

	public void goBack() {
		currentPageNumber -= 2;
		if (currentPageNumber < 0) currentPageNumber = pages.size() - 1;
		currentPage = pages.get(currentPageNumber);
		playPageSound();
	}

	public void goNext() {
		currentPageNumber += 2;
		if (currentPageNumber >= pages.size()) currentPageNumber = 0;
		currentPage = pages.get(currentPageNumber);
		playPageSound();
	}

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

	public void playPageSound() {
		Random r = new Random();
		this.mc.sndManager.playSound("random.page", SoundCategory.GUI_SOUNDS, 0.8F, 0.9F + (r.nextFloat() - r.nextFloat()) * 0.1F);
	}

	@Override
	public void mouseClicked(int mx, int my, int buttonNum) {
		super.mouseClicked(mx, my, buttonNum);
		if (buttonNum == 0) { // Left mouse button
			int size = 24;
			int top = height / 2 - 110;
			int bottom = height / 2 + 110;
			int left = width / 2 - 158;
			int right = width / 2 + 158;

			if (currentPageNumber > 0) { // Has previous page
				if (mx >= left && mx <= left + size && my >= top && my <= top + size) {
					goBack();
					return;
				}
				if (mx >= left && mx <= left + size && my >= bottom - size && my <= bottom) {
					goBack();
					return;
				}
			}

			// Check right page turn indicators
			if (currentPageNumber + 1 < pages.size()) { // Has next page
				if (mx >= right - size && mx <= right && my >= top && my <= top + size) {
					goNext();
					return;
				}
				if (mx >= right - size && mx <= right && my >= bottom - size && my <= bottom) {
					goNext();
					return;
				}
			}
		}
	}
}
