package deus.guidebookmd.formats;

public class MarkdownFormat {
	public static final char BOLD = 'l';
	public static final char ITALIC = 'o';
	public static final char UNDERLINE = 'n';
	public static final char STRIKETHROUGH = 'm';
	public static final char MAGIC = 'k';
	public static final char RESET = 'r';
	public static final char SHADOWS = 's';


	public static boolean isFormatCode(char c) {
		return c == BOLD ||
			c == ITALIC ||
			c == UNDERLINE ||
			c == STRIKETHROUGH ||
			c == MAGIC ||
			c == RESET ||
			c == SHADOWS;
	}
}
