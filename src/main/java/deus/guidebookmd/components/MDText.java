package deus.guidebookmd.components;

import deus.guidebookmd.components.MDComponent;
import deus.guidebookmd.formats.MarkdownColor;
import deus.guidebookmd.formats.MarkdownFormat;
import deus.guidebookmd.formats.TextPart;

import java.util.ArrayList;
import java.util.List;

public class MDText extends MDComponent {



	public boolean centered = false;
	public int argbColor = 0xffffff;
	public String originalText = "";
	public List<TextPart> parts = new ArrayList<>();

	public MDText(String text) {
		this.height = mc.font.fontHeight;
		this.width = 18;
		this.originalText = text;
		this.parts = parseTextWithFormatting(text);
	}

	private List<TextPart> parseTextWithFormatting(String text) {
		List<TextPart> parsed = new ArrayList<>();
		int color = 0xffffff;
		boolean shadow = false;

		StringBuilder buffer = new StringBuilder();

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);

			if (c == '&' && i + 1 < text.length()) {
				char code = text.charAt(i + 1);
				i++;

				if (buffer.length() > 0) {
					parsed.add(new TextPart(buffer.toString(), color, shadow));
					buffer.setLength(0);
				}

				int newColor = MarkdownColor.get(code);
				if (newColor != -1) {
					color = newColor;
					continue;
				}

				switch (code) {
					case MarkdownFormat.SHADOWS:
						shadow = true;
						break;
					case MarkdownFormat.RESET:
						color = 0xffffff;
						shadow = false;
						break;
				}
			} else {
				buffer.append(c);
			}
		}

		if (buffer.length() > 0) {
			parsed.add(new TextPart(buffer.toString(), color, shadow));
		}

		return parsed;
	}
	@Override
	public void render(int x, int y, int mx, int my) {
		int drawX = this.x + x;
		int drawY = this.y + y;
		int offsetX = 0;

		for (TextPart part : parts) {
			int color = part.color;
			String text = part.text;

			if (centered) {
				drawStringCentered(mc.font, text, drawX, drawY, color);
			} else {
				if (part.shadow) {
					drawString(mc.font, text, drawX + offsetX, drawY, color);
				} else {
					drawStringNoShadow(mc.font, text, drawX + offsetX, drawY, color);
				}
				offsetX += mc.font.getStringWidth(text);
			}
		}

	}
}
