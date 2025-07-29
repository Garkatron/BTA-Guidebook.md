package deus.guidebookmd.gui.elements;

import deus.guidebookmd.utils.Signal;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TextArea extends MDGui {

	// ? State
	private boolean wasClicked = false;
	private boolean wasClickedOut = false;
	private boolean focused = false;

	public List<Character> characters = new ArrayList<>();
	protected int currentCharPos = 0;
	protected int currentLine = 1;
	protected int currentLineCharCount = 1;
	protected final List<List<Character>> clipboard = new ArrayList<>();

	// * Selection
	protected boolean isSelecting = false;
	protected int selectStartChar = 0;
	protected int selectLastChar = 0;

	// ? Drawing
	protected boolean drawBackground = true;
	protected boolean drawLineCharCount = true;
	protected boolean drawLineCount = true;
	protected boolean drawExtraCursors = true;

	protected int cursorX = 0;
	protected int cursorY = 0;

	// * Animation
	int cursorBlinkInterval = 500;
	private long lastCursorToggle = 0;
	private boolean drawCursor = true;

	String cursorCharacter = "_";

	protected int maxTextLength = 20;
	protected int textOffsetX = 12;
	protected int minTextOffsetx = 12;

	// * Colors
	int focusBackgroundColor = 0xFF000000;
	int focusTextColor = 0xFFE9C46A;
	int focusBorderColor = 0xFFE9C46A;

	int defaultBackgroundColor = 0xFF000000;
	int defaultTextColor = 0xFFFFFFFF;
	int defaultBorderColor = 0xFFFFFFFF;

	// ? Keys
	protected boolean isCtrl = false;
	protected boolean isShift = false;

	// ? API
	public final Signal<String> textChangedSignal = new Signal<>();

	public TextArea() {
		currentCharPos = characters.size();
	}

	// ? Functions
	protected void paste() {
		if (!clipboard.isEmpty()) {
			characters.addAll(currentCharPos, clipboard.get(clipboard.size()-1));
			currentCharPos = characters.size();
		}
	}

	protected void copy(int start, int end) {
		int from = Math.min(start, end);
		int to = Math.max(start, end);

		clipboard.add(new ArrayList<>(characters.subList(from, to)));
	}

	protected void cut(int start, int end) {
		copy(start, end);
		int from = Math.min(start, end);
		int to = Math.max(start, end);
		for (int i = 0; i < to - from; i++) {
			characters.remove(from);
		}
	}

	private void deleteWord() {
		while (!isAtEnd() && !isSpace(peek()) && !wordDeleteIgnore(peek())) {
			deleteCharacter();
		}
	}

	// ? Drawing functions
	protected void drawBackground() {
		int backgroundColor = focused ? focusBackgroundColor : defaultBackgroundColor;
		int borderColor = focused ? focusBorderColor : defaultBorderColor;

		this.drawRect(this.x - 1, this.y - 1, this.x + width + 1 + textOffsetX, this.y + height + 1, borderColor);
		this.drawRect(this.x, this.y, this.x + width + textOffsetX, this.y + height, backgroundColor);
	}

	protected void drawText() {
		int textColor = focused ? focusTextColor : defaultTextColor;
		int lineHeight = this.mc.font.fontHeight;
		int textStartY = this.y + 4;

		StringBuilder lineBuffer = new StringBuilder();
		int drawY = textStartY;

		int lineCharCount = 0;
		int cursorLine = 0;
		int pixelX = 0;

		int tempCharPos = 0;

		cursorX = 4;
		cursorY = 4;

		for (int i = 0; i < characters.size(); i++) {
			char c = characters.get(i);

			if (tempCharPos == currentCharPos) {
				cursorX = 4 + pixelX;
				cursorY = 4 + (cursorLine * lineHeight);
			}

			if (c == '\n' || lineCharCount >= maxTextLength) {
				this.drawString(this.mc.font, lineBuffer.toString(), this.x + textOffsetX, drawY, textColor);
				drawY += lineHeight;
				lineBuffer.setLength(0);
				lineCharCount = 0;
				pixelX = 0;
				cursorLine++;

				if (c != '\n') {
					lineBuffer.append(c);
					pixelX += this.mc.font.getCharWidth(c);
					lineCharCount++;
				}
			} else {
				lineBuffer.append(c);
				pixelX += this.mc.font.getCharWidth(c);
				lineCharCount++;

			}
			tempCharPos++;
		}

		if (tempCharPos == currentCharPos) {
			cursorX = textOffsetX + pixelX;
			cursorY = 4 + (cursorLine * lineHeight);
		}

		currentLineCharCount = lineCharCount;
		currentLine = cursorLine;


		if (drawLineCount) {
			for (int i = 0; i < cursorLine; i++) {
				this.drawString(this.mc.font, i+"", this.x, this.y + 4 + (i * mc.font.fontHeight), 0x252525);
			}
		}

		if (lineBuffer.length() > 0) {
			this.drawString(this.mc.font, lineBuffer.toString(), this.x + textOffsetX, drawY, textColor);
		}
	}

	protected void drawCursor() {
		this.drawString(this.mc.font, cursorCharacter, this.x + cursorX, this.y + cursorY, 0xff0000);
	}

	protected void drawAlternativeCursors() {
		this.drawString(this.mc.font, "|", this.x + width-1+textOffsetX, this.y + cursorY, 0xff0000);
		this.drawString(this.mc.font, cursorCharacter, this.x + cursorX, this.y-7, 0xff0000);
	}

	protected void drawLineCharCount() {
		String line = currentLine + "-";
		String lineCharCount = String.valueOf(currentLineCharCount);

		this.drawString(this.mc.font, line, this.x, this.y + cursorY + mc.font.fontHeight*2, 0xb2b3b3);
		this.drawString(this.mc.font, lineCharCount, this.x + mc.font.getStringWidth(line), this.y + cursorY + mc.font.fontHeight*2, 0xb2b3b3);
	}

	@Override
	public void render() {
		if (drawBackground) drawBackground();
		if (drawLineCharCount) drawLineCharCount();
		drawText();
		if (drawCursor) drawCursor();
		if (drawExtraCursors) drawAlternativeCursors();
	}

	// ? Logic Functions
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

	@Override
	public void update() {
		maxTextLength = (width/6)-1;

		isCtrl = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		isShift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

		textOffsetX = Math.max(minTextOffsetx, mc.font.getStringWidth(currentLine+"")+1);

		if (!focused) return;

		long currentTime = System.currentTimeMillis();
		if (currentTime - lastCursorToggle > cursorBlinkInterval) {
			drawCursor = !drawCursor;
			lastCursorToggle = currentTime;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_BACK)) {
			deleteWord();
			return;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_DELETE)) {
			deleteWord();

			return;
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				int key = Keyboard.getEventKey();
				char character = Keyboard.getEventCharacter();

				if (isShift && key == Keyboard.KEY_LEFT) {
					if (!isSelecting) {
						selectStartChar = currentCharPos;
						isSelecting = true;
					}
					if (currentCharPos > 0) {
						currentCharPos--;
					}
					selectLastChar = currentCharPos;
					return;
				}

				if (isShift && key == Keyboard.KEY_RIGHT) {
					if (!isSelecting) {
						selectStartChar = currentCharPos;
						isSelecting = true;
					}
					if (currentCharPos < characters.size()) {
						currentCharPos++;
					}
					selectLastChar = currentCharPos;
					return;
				}

				if (key == Keyboard.KEY_C && isCtrl) {
					if (selectStartChar != selectLastChar) {
						int from = Math.min(selectStartChar, selectLastChar);
						int to = Math.max(selectStartChar, selectLastChar);
						copy(from, to);
					}
					return;
				} else if (isCtrl && Keyboard.isKeyDown(Keyboard.KEY_V)) {
					paste();
					isSelecting = false;

				} else if (key == Keyboard.KEY_BACK) {
					deleteCharacter();

				} else if (key == Keyboard.KEY_ESCAPE) {
					focused = false;

				} else if (key == Keyboard.KEY_RETURN) {
					jumpLine();

				} else if (key == Keyboard.KEY_LEFT) {
				if (currentCharPos > 0) {
					currentCharPos--;
				}

				} else if (key == Keyboard.KEY_RIGHT) {
					if (currentCharPos < characters.size()) {
						currentCharPos++;
					}
				}  else if (Character.isDefined(character) && !Character.isISOControl(character)) {
					addCharacter(character);
				}
			}
		}
	}

	// ? Helper
	private boolean isSpace(char c) {
		return c == ' ' || c == '\n';
	}

	private boolean wordDeleteIgnore(char c) {
		return isSpace(c) || c == '.' || c == ',';
	}

	private boolean isAtEnd() {
		return characters.isEmpty();
	}

	private char peek() {
		return characters.get(currentCharPos-1);
	}

	private char peekPrev() {
		if (characters.isEmpty()) return '\0';
		return characters.get(currentCharPos-2);
	}

	public boolean isHovered() {
		return mx >= x && my >= y && mx < x + width && my < y + height;
	}

	// ? Utility
	private void addCharacter(char character) {
		characters.add(character);
		currentCharPos++;
	}

	private void deleteCharacter() {
		if (focused) {
			if (currentCharPos > 0) {
				characters.remove(currentCharPos-1);
				currentCharPos--;
			}
		}
	}

	private void jumpLine() {
		addCharacter('\n');
	}

	public List<String> getLines() {
		List<String> lines = new ArrayList<>();
		StringBuilder lineBuffer = new StringBuilder();
		int lineCharCount = 0;

		for (int i = 0; i < characters.size(); i++) {
			char c = characters.get(i);

			if (c == '\n') {
				lines.add(lineBuffer.toString());
				lineBuffer.setLength(0);
				lineCharCount = 0;
			} else {
				lineBuffer.append(c);
				lineCharCount++;

				if (lineCharCount >= maxTextLength) {
					lines.add(lineBuffer.toString());
					lineBuffer.setLength(0);
					lineCharCount = 0;
				}
			}
		}

		if (lineBuffer.length() > 0) {
			lines.add(lineBuffer.toString());
		}

		return lines;
	}

	// ? API
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

}
