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

	protected List<Character> characters = new ArrayList<>();
	protected final List<List<Character>> undoStack = new ArrayList<>();
	protected final List<List<Character>> redoStack = new ArrayList<>();
	public int maxUndoHistory = 100;

	protected int currentCharPos = 0;
	protected int currentLine = 1;
	protected int currentLineCharCount = 1;
	protected final List<List<Character>> clipboard = new ArrayList<>();

	// * Selection
	protected boolean isSelecting = false;
	protected int selectStartChar = 0;
	protected int selectLastChar = 0;

	// ? Drawing
	public boolean drawBackground = true;
	public boolean drawLineCharCount = true;
	public boolean drawLineCount = true;
	public boolean drawExtraCursors = true;

	protected int cursorX = 0;
	protected int cursorY = 0;

	// * Animation
	int cursorBlinkInterval = 500;
	private long lastCursorToggle = 0;
	public boolean drawCursor = true;
	public boolean animateCursor = true;

	String cursorCharacter = "|";

	public int maxTextLength = 20;
	protected int textOffsetX = 12;
	public int minTextOffsetx = 12;

	// * Colors
	public int focusBackgroundColor = 0xFF000000;
	public int focusTextColor = 0xFFE9C46A;
	public int focusBorderColor = 0xFFE9C46A;

	public int backgroundColor = 0xFF000000;
	public int textColor = 0xFFFFFFFF;
	public int borderColor = 0xFFFFFFFF;
	public int cursorColor = 0xFFFFFFFF;
	public int lineCountColor = 0xb2b3b3;

	// ? Keys
	protected boolean isCtrl = false;
	protected boolean isShift = false;

	// ? API
	public final Signal<List<Character>> $onTextChanged = new Signal<>();

	public TextArea() {
		currentCharPos = characters.size();
		$onTextChanged.connect(
			(s, chars)->{
				if (!undoStack.isEmpty() && undoStack.get(undoStack.size() - 1).equals(characters)) return;

				undoStack.add(new ArrayList<>(characters));
				if (undoStack.size() > maxUndoHistory) {
					undoStack.remove(0);
				}
				redoStack.clear();
			}
		);
	}

	// ? Functions
	protected void paste() {
		if (!clipboard.isEmpty()) {
			characters.addAll(currentCharPos, clipboard.get(clipboard.size()-1));
			currentCharPos = characters.size();

			$onTextChanged.emit(characters);
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

		$onTextChanged.emit(characters);
	}

	protected void undo() {
		if (!undoStack.isEmpty()) {
			redoStack.add(new ArrayList<>(characters));
			characters = undoStack.remove(undoStack.size() - 1);
			currentCharPos = Math.min(currentCharPos, characters.size());
		}

	}

	protected void redo() {
		if (!redoStack.isEmpty()) {
//			undoStack.add(new ArrayList<>(characters));
//			int index = undoStack.size() - 1;
//			if (index< redoStack.size()) {
//				characters = redoStack.remove(index);
//			}
			// currentCharPos = Math.min(currentCharPos, characters.size());
		}
	}

	protected void deleteSequence(int start, int end) {
		int from = Math.min(start, end);
		int to = Math.max(start, end);
		for (int i = 0; i < to - from; i++) {
			characters.remove(from);
		}

		$onTextChanged.emit(characters);
	}

	private void deleteWord() {
		while (!isAtEnd() && !isSpace(peek()) && !wordDeleteIgnore(peek())) {
			deleteCharacter();
		}
		$onTextChanged.emit(characters);
	}

	// ? Drawing functions
	protected void drawBackground() {
		int backgroundColor = focused ? focusBackgroundColor : this.backgroundColor;
		int borderColor = focused ? focusBorderColor : this.borderColor;

		this.drawRect(this.x - 1, this.y - 1, this.x + width + 1 + textOffsetX, this.y + height + 1, borderColor);
		this.drawRect(this.x, this.y, this.x + width + textOffsetX, this.y + height, backgroundColor);
	}

	protected void drawText() {
		int textColor = focused ? focusTextColor : this.textColor;
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
			if (tempCharPos == currentCharPos) {
				cursorX = textOffsetX + pixelX;
				cursorY = 4 + (cursorLine * lineHeight);
			}

			char c = characters.get(i);

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


		if (currentCharPos == characters.size()) {
			cursorX = textOffsetX + pixelX;
			cursorY = 4 + (cursorLine * lineHeight);
		}


		currentLineCharCount = lineCharCount;
		currentLine = cursorLine;


		if (drawLineCount) {
			for (int i = 0; i < cursorLine + 1; i++) {
				this.drawString(this.mc.font, i+"", this.x, this.y + 4 + (i * mc.font.fontHeight), lineCountColor);
			}
		}

		if (lineBuffer.length() > 0) {
			this.drawString(this.mc.font, lineBuffer.toString(), this.x + textOffsetX, drawY, textColor);
		}
	}

	protected void drawCursor() {
		this.drawString(this.mc.font, cursorCharacter, this.x + cursorX, this.y + cursorY, cursorColor);
	}

	protected void drawAlternativeCursors() {
		this.drawString(this.mc.font, "|", this.x + width-1+textOffsetX, this.y + cursorY, 0xff0000);
		this.drawString(this.mc.font, cursorCharacter, this.x + cursorX, this.y-7, 0xff0000);
	}

	protected void drawLineCharCount() {
		// String line = currentLine + "-";
		String lineCharCount = String.valueOf(currentLineCharCount);

		// this.drawString(this.mc.font, line, this.x, this.y + cursorY - mc.font.fontHeight*2, 0xb2b3b3);
		this.drawString(this.mc.font, lineCharCount, this.x + cursorX, this.y + cursorY + mc.font.fontHeight, lineCountColor);
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

		if (animateCursor) {
			long currentTime = System.currentTimeMillis();
			if (currentTime - lastCursorToggle > cursorBlinkInterval) {
				drawCursor = !drawCursor;
				lastCursorToggle = currentTime;
			}
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
						copy(selectStartChar, selectLastChar);
					}
					return;
				}
				else if (isCtrl && Keyboard.isKeyDown(Keyboard.KEY_V)) {
					paste();
					isSelecting = false;

				} else if (isCtrl && Keyboard.isKeyDown(Keyboard.KEY_Z)) {
					undo();
				} else if (isCtrl && Keyboard.isKeyDown(Keyboard.KEY_Y)) {
					redo();

				} else if (isShift && key == Keyboard.KEY_BACK) {
					deleteSequence(selectStartChar, selectLastChar);
					return;
				}
				else if (key == Keyboard.KEY_BACK) {
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
				} else if (key == Keyboard.KEY_END) {
					currentCharPos = currentLineCharCount;
				} else if (key == Keyboard.KEY_HOME) {
					currentCharPos -= currentLineCharCount;

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

		$onTextChanged.emit(characters);
	}

	private void deleteCharacter() {
		if (focused) {
			if (currentCharPos > 0) {
				characters.remove(currentCharPos-1);
				currentCharPos--;

				$onTextChanged.emit(characters);
			}
		}
	}

	private void jumpLine() {
		characters.add(currentCharPos, '\n');
		currentCharPos++;
		$onTextChanged.emit(characters);
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
