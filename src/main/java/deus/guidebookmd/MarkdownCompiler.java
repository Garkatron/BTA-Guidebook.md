package deus.guidebookmd;

import deus.guidebookmd.components.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

public class MarkdownCompiler {

	private static final List<PatternType> patterns = new ArrayList<>();
	public static final int[] colors = {};

	static {
		patterns.add(new PatternType(Pattern.compile("!\\[([^]]*)\\]\\(([^)]*)\\)?"), "SPECIAL"));
		patterns.add(new PatternType(Pattern.compile("\\[([^\\]]+)]\\(([^)]+)\\)\\((\\d+),(\\d+)(?:,\\s*([^\\)]+))?\\)"), "IMAGE"));
		patterns.add(new PatternType(Pattern.compile("\\[([^\\]]+)\\]\\(([^\\)]+)\\)"), "LINK"));
		patterns.add(new PatternType(Pattern.compile("^######\\s+(.+)$"), "H6"));
		patterns.add(new PatternType(Pattern.compile("^#####\\s+(.+)$"), "H5"));
		patterns.add(new PatternType(Pattern.compile("^####\\s+(.+)$"), "H4"));
		patterns.add(new PatternType(Pattern.compile("^###\\s+(.+)$"), "H3"));
		patterns.add(new PatternType(Pattern.compile("^##\\s+(.+)$"), "H2"));
		patterns.add(new PatternType(Pattern.compile("^#\\s+(.+)$"), "H1"));
		patterns.add(new PatternType(Pattern.compile("^[+*]\\s+(.+)$"), "LIST"));
		patterns.add(new PatternType(Pattern.compile("^\\s*(?![#*+])(.*)$"), "TEXT"));
	}

	// [image](a.png)(w,h,type=default|icon)
	public static List<MDComponent> compile(List<String> lines) {
		return compile(lines, -1);
	}


	public static List<MDComponent> compile(List<String> lines, int maxLines) {
		List<MDComponent> currentPage = new ArrayList<>();
		int lineCount = 0;
		for (String line : lines) {
			if (maxLines > 0 && lineCount >= maxLines) {
				Guidebookmd.LOGGER.warn("Max lines for MD reached: {}", maxLines);
				break;
			}
			boolean matched = false;
			lineCount++;
			for (int i = 0; i < patterns.size(); i++) {
				PatternType pt = patterns.get(i);
				Matcher m = pt.pattern.matcher(line);

				if (m.matches()) {
					String content = m.groupCount() >= 1 ? m.group(1) : "";
					String type = pt.type;

					switch (type) {
						case "IMAGE":
							String tipo = m.group(5);
							if (tipo == null) tipo = "default";
							currentPage.add(new MDImage(content, m.group(2), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), tipo));
							break;
						case "H1":
							currentPage.add(new MDTitle(content, 2.0f));
							break;
						case "H2":
							currentPage.add(new MDTitle(content, 1.5f));
							break;
						case "H3":
							currentPage.add(new MDTitle(content, 1f));
							break;
						case "H4":
							currentPage.add(new MDTitle(content, 0.5f));
							break;
						case "H5":
							currentPage.add(new MDTitle(content, 0.3f));
							break;
						case "H6":
							currentPage.add(new MDTitle(content, 0.1f));
							break;
						case "LIST":
							currentPage.add(new MDText("• " + content));
							break;
						case "TEXT":
							currentPage.add(new MDText(content));
							break;
						case "LINK":
							currentPage.add(new MDText(content));
							break;
						case "SPECIAL": {
							switch (content) {
								case "slot":
									currentPage.add(new MDFakeSlot(m.group(2)));
									break;
								case "workbench":
									currentPage.add(new MDWorbench(m.group(2)));
							}
							break;
						}
					}

					matched = true;
					break;
				}

			}

			if (!matched) {
				System.out.println("Can't recognize: " + line);
			}
		}

		return currentPage;
	}

	public static List<MDComponent> compile(String path, Class<?> c, int maxLines) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(c.getResourceAsStream(path)))) {
			List<String> lines = reader.lines().collect(Collectors.toList());
			return compile(lines, maxLines);
		} catch (IOException e) {
			throw new RuntimeException("Error reading Markdown file: " + path, e);
		}
	}
	public static List<MDComponent> compile(String path, Class<?> c) {
		return compile(path, c, -1);
	}

	private static class PatternType {
		public Pattern pattern;
		public String type;

		public PatternType(Pattern pattern, String type) {
			this.pattern = pattern;
			this.type = type;
		}

	}


}
