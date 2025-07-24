package deus.guidebookmd;

import deus.guidebookmd.components.MDComponent;
import deus.guidebookmd.components.MDImage;
import deus.guidebookmd.components.MDText;
import deus.guidebookmd.components.MDTitle;

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
		patterns.add(new PatternType(Pattern.compile("\\[([^\\]]+)]\\(([^)]+\\.png)\\)\\((\\d+),(\\d+)\\)"), "IMAGE"));
		patterns.add(new PatternType(Pattern.compile("\\[([^\\]]+)\\]\\(([^\\)]+)\\)"), "LINK"));
		patterns.add(new PatternType(Pattern.compile("^######\\s+(.+)$"), "H6"));
		patterns.add(new PatternType(Pattern.compile("^#####\\s+(.+)$"), "H5"));
		patterns.add(new PatternType(Pattern.compile("^####\\s+(.+)$"), "H4"));
		patterns.add(new PatternType(Pattern.compile("^###\\s+(.+)$"), "H3"));
		patterns.add(new PatternType(Pattern.compile("^##\\s+(.+)$"), "H2"));
		patterns.add(new PatternType(Pattern.compile("^#\\s+(.+)$"), "H1"));
		patterns.add(new PatternType(Pattern.compile("^[+*]\\s+(.+)$"), "LIST"));
		patterns.add(new PatternType(Pattern.compile("^(?!#|\\*|\\+)(.+)$"), "TEXT"));

	}

	public static List<MDComponent> compile(List<String> lines) {
		List<MDComponent> currentPage = new ArrayList<>();

		for (String line : lines) {
			boolean matched = false;

			for (int i = 0; i < patterns.size(); i++) {
				PatternType pt = patterns.get(i);
				Matcher m = pt.pattern.matcher(line);

				if (m.matches()) {
					String content = m.group(1);
					String type = pt.type;

					switch (type) {
						case "IMAGE":
							currentPage.add(new MDImage(content, m.group(2), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4))));
							break;
						case "H1":
							currentPage.add(new MDTitle(content, 1));
							break;
						case "H2":
							currentPage.add(new MDTitle(content, 2));
							break;
						case "H3":
							currentPage.add(new MDTitle(content, 3));
							break;
						case "H4":
							currentPage.add(new MDTitle(content, 4));
							break;
						case "H5":
							currentPage.add(new MDTitle(content, 5));
							break;
						case "H6":
							currentPage.add(new MDTitle(content, 6));
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
					}

					matched = true;
					break;
				}
			}

			if (!matched) {
				System.out.println("No se reconoció: " + line);
			}
		}

		return currentPage;
	}

	public static List<MDComponent> compile(String path, Class<?> c) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(c.getResourceAsStream(path)))) {
			List<String> lines = reader.lines().collect(Collectors.toList());
			return compile(lines);
		} catch (IOException e) {
			throw new RuntimeException("Error reading Markdown file: " + path, e);
		}
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
