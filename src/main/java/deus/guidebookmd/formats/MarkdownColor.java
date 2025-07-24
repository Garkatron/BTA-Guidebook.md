package deus.guidebookmd.formats;

import java.util.HashMap;
import java.util.Map;

public class MarkdownColor {
	private static final Map<Character, Integer> COLORS = new HashMap<>();

	static {
		COLORS.put('0', 0x000000); // Black
		COLORS.put('1', 0x0000AA); // Dark Blue
		COLORS.put('2', 0x00AA00); // Dark Green
		COLORS.put('3', 0x00AAAA); // Dark Aqua
		COLORS.put('4', 0xAA0000); // Dark Red
		COLORS.put('5', 0xAA00AA); // Dark Purple
		COLORS.put('6', 0xFFAA00); // Gold
		COLORS.put('7', 0xAAAAAA); // Gray
		COLORS.put('8', 0x555555); // Dark Gray
		COLORS.put('9', 0x5555FF); // Blue
		COLORS.put('a', 0x55FF55); // Green
		COLORS.put('b', 0x55FFFF); // Aqua
		COLORS.put('c', 0xFF5555); // Red
		COLORS.put('d', 0xFF55FF); // Light Purple
		COLORS.put('e', 0xFFFF55); // Yellow
		COLORS.put('f', 0xFFFFFF); // White
	}

	public static int get(char c) {
		return COLORS.getOrDefault(c, -1);
	}


}
