package deus.guidebookmd.gui.elements;

import deus.guidebookmd.gui.elements.MDGui;
import deus.guidebookmd.utils.Signal;
import net.minecraft.client.gui.text.ITextField;
import net.minecraft.client.render.tessellator.Tessellator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TextArea extends MDGui {

	private boolean wasClicked = false;
	private boolean wasClickedOut = false;

	public List<String> text = new ArrayList<>();
	protected int cursorPosition = 0;
	protected long lastCursorToggle = 0;
	protected boolean drawCursor = true;
	protected boolean drawBackground = true;

	protected int currentIndex = 0;

	public final Signal<String> textChangedSignal = new Signal<>();
	private boolean focused = false;

	protected int maxTextLength = 20;

	public boolean isHovered() {
		return mx >= x && my >= y && mx < x + width && my < y + height;
	}



	@Override
	public void render() {
		int focusBackgroundColor = 0xFF000000;
		int focusTextColor = 0xFFE9C46A;
		int focusBorderColor = 0xFFE9C46A;

		int defaultBackgroundColor = 0xFF000000;
		int defaultTextColor = 0xFFFFFFFF;
		int defaultBorderColor = 0xFFFFFFFF;

		int cursorBlinkInterval = 500;

		String cursorCharacter = "_";


		int backgroundColor = focused ? focusBackgroundColor : defaultBackgroundColor;
		int textColor = focused ? focusTextColor : defaultTextColor;
		int borderColor = focused ? focusBorderColor : defaultBorderColor;

		this.drawRect(this.x - 1, this.y - 1, this.x + width + 1, this.y + height + 1, borderColor);
		this.drawRect(this.x, this.y, this.x + width, this.y + height, backgroundColor);
		//if (drawBackground) {

		//}

		int lineHeight = this.mc.font.fontHeight;
		int textStartY = this.y + 4;

		for (int i = 0; i < text.size(); i++) {
			String line = text.get(i);
			this.drawString(this.mc.font, line, this.x + 4, textStartY + (lineHeight * i), textColor);
		}

		if (focused) {
			long currentTime = System.currentTimeMillis();
			if (currentTime - lastCursorToggle > cursorBlinkInterval) {
				drawCursor = !drawCursor;
				lastCursorToggle = currentTime;
			}

			if (drawCursor && currentIndex < text.size()) {
				int cursorX = this.x + 4 + this.mc.font.getStringWidth(text.get(currentIndex).substring(0, cursorPosition));
				int cursorY = textStartY + (lineHeight * currentIndex);
				this.drawString(this.mc.font, cursorCharacter, cursorX, cursorY, textColor);
			}
		}
	}



	@Override
	public void updateMousePos(int mx, int my) {
		super.updateMousePos(mx, my);

		boolean hovered = isHovered();
		boolean buttonDown = Mouse.isButtonDown(0);

		if (hovered) {
			if (buttonDown) {
				if (!wasClicked) {
					onPush();
					wasClicked = true;
				}

				whilePressed();
				wasClickedOut = false;

			} else {
				if (wasClicked) {
					onRelease();
					wasClicked = false;
				}

				if (buttonDown) {
					if (!wasClickedOut) {
						onPushOut();
						wasClickedOut = true;
					}
				} else {
					wasClickedOut = false;
				}
			}
		} else {
			if (buttonDown) {
				if (!wasClickedOut) {
					onPushOut();
					wasClickedOut = true;
				}
			} else {
				wasClicked = false;
				wasClickedOut = false;
			}
		}
	}
	public void whilePressed() {
	}

	public void onPush() {
		focused = true;
	}

	public void onPushOut() {
		focused = false;
	}

	public void onRelease() {
	}


	@Override
	public void update() {

		if (focused) {
			while (Keyboard.next()) {
				if (Keyboard.getEventKeyState()) {
					int key = Keyboard.getEventKey();
					char character = Keyboard.getEventCharacter();

					if (key == Keyboard.KEY_BACK) {
						deleteCharacter();


					} else if (key == Keyboard.KEY_ESCAPE) {
						focused = false;


					} else if (key == Keyboard.KEY_RETURN) {
						currentIndex++;
						if (currentIndex >= text.size()) {
							text.add("");
						}
						cursorPosition = 0;
						height += 10;


					} else if (key == Keyboard.KEY_LEFT) {
						if (cursorPosition > 0) {
							cursorPosition -= 1;
						} else if (currentIndex > 0) {
							currentIndex--;
							cursorPosition = text.get(currentIndex).length();
						}

					} else if (key == Keyboard.KEY_RIGHT) {
						if (cursorPosition < text.get(currentIndex).length()) {
							cursorPosition += 1;
						} else if (currentIndex < text.size() - 1) {
							currentIndex++;
							cursorPosition = 0;
						}

						//&& isCharacterAllowed(character)
					} else if (character != 0 ) {
						addCharacter(character);
					}
				}
			}
		}
	}

	private void deleteCharacter() {
		if (focused) {
			if (cursorPosition > 0) {
				text.set(currentIndex, text.get(currentIndex).substring(0, cursorPosition - 1) + text.get(currentIndex).substring(cursorPosition));
				cursorPosition = Math.max(0, cursorPosition - 1);
				textChangedSignal.emit(text.get(currentIndex));
			} else if (currentIndex > 0) {
				String previousLine = text.get(currentIndex - 1);
				String currentLine = text.get(currentIndex);
				text.set(currentIndex - 1, previousLine + currentLine);
				text.remove(currentIndex);
				height -=10;
				currentIndex--;
				cursorPosition = previousLine.length();
				textChangedSignal.emit(text.get(currentIndex));
			}
		}
	}

	private void addCharacter(char character) {
		if (text.get(currentIndex).length() < maxTextLength) {
			text.set(currentIndex, text.get(currentIndex).substring(0, cursorPosition) + character + text.get(currentIndex).substring(cursorPosition));
			cursorPosition++;
			textChangedSignal.emit(text.get(currentIndex));
		}
	}

	public void setText(String newText) {
		if (!this.text.equals(newText)) {
			text.set(currentIndex, newText);
			cursorPosition = text.get(currentIndex).length();
			textChangedSignal.emit(text.get(currentIndex));
		}
	}

}
