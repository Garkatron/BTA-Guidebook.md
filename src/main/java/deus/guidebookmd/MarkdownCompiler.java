package deus.guidebookmd;

import deus.guidebookmd.components.*;
import deus.guidebookmd.gui.MDPage;
import deus.guidebookmd.config.PageConfig;
import deus.guidebookmd.utils.Tuple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

public class MarkdownCompiler {

	// ? Patterns
	private static class PatternType {
		public Pattern pattern;
		public String type;

		public PatternType(Pattern pattern, String type) {
			this.pattern = pattern;
			this.type = type;
		}
	}
	private static final List<PatternType> patterns = new ArrayList<>();
	static {
		patterns.add(new PatternType(Pattern.compile("!\\[([^]]*)\\]\\((.*?)\\)", Pattern.DOTALL), "SPECIAL"));
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

	// ? Main Function
	public static MDPage compile(List<String> lines, int maxLines) {
		List<MDComponent> currentPage = new ArrayList<>();
		int lineCount = 0;

		String fullText = String.join("\n", lines);

		// * Returns config and a clean text
		Tuple<PageConfig, String> info = extractConfig(fullText);

		fullText = info.y;

		String[] splitLines = fullText.split("\n");

		for (String line : splitLines) {
			if (maxLines > 0 && lineCount >= maxLines) {
				Guidebookmd.LOGGER.warn("Max lines for MD reached: {}", maxLines);
				break;
			}
			boolean matched = false;
			lineCount++;

			for (PatternType pt : patterns) {
				Matcher m = pt.pattern.matcher(line);

				if (m.find()) {
					String content = m.groupCount() >= 1 ? m.group(1) : "";
					String type = pt.type;

					// * Finds the action for each pattern
					switch (type) {
						case "IMAGE":
							String imageType = m.group(5);
							if (imageType == null) imageType = "default";
							currentPage.add(new MDImage(content, m.group(2), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), imageType));
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
							currentPage.add(new MDLink(content, m.group(2)));
							break;
						// * Works different, it depends on the content inside []
						case "SPECIAL": {
							switch (content) {
								case "slot":
									currentPage.add(new MDFakeSlot(m.group(2)));
									break;
								case "workbench":
									currentPage.add(new MDWorbench(m.group(2)));
									break;

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

		return new MDPage(info.x, currentPage);
	}


	// ? Others

	/**
	 * Extracts the config json from the markdown String
	 * @param fullText Markdown
	 * @return A Tuple with the MDPageConfig and the clean String
	 */
	public static Tuple<PageConfig, String> extractConfig(String fullText) {
		Matcher configMatcher = patterns.get(0).pattern.matcher(fullText);

		if (configMatcher.find()) {
			if ("config".equals(configMatcher.group(1))) {
				try {
					PageConfig config = PageConfig.fromJsonString(configMatcher.group(2));

					int start = configMatcher.start();
					int end = configMatcher.end();
					String before = fullText.substring(0, start).trim();
					String after = fullText.substring(end).trim();
					String cleanedText = before + "\n" + after;

					return new Tuple<>(config, cleanedText);
				} catch (Exception e) {
					Guidebookmd.LOGGER.error("Invalid config block", e);
				}
			}
		}

		return new Tuple<>(null, fullText);
	}


	// ? Compile overhead
	public static MDPage compile(List<String> lines) {
		return compile(lines, -1);
	}
	public static MDPage compile(String path, Class<?> c, int maxLines) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(c.getResourceAsStream(path)))) {
			List<String> lines = reader.lines().collect(Collectors.toList());
			return compile(lines, maxLines);
		} catch (IOException e) {
			throw new RuntimeException("Error reading Markdown file: " + path, e);
		}
	}

	public static MDPage compile(String path, Class<?> c) {
		return compile(path, c, -1);
	}


}
