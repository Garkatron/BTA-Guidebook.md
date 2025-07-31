package deus.guidebookmd.utils;

import deus.guidebookmd.Guidebookmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RLoading {
	// * Yeah, I'm too lazy to figure out how to write path-safety on my own.
	public static List<String> loadMarkdownFilesFromFolderName(String name) {
		Objects.requireNonNull(name, "Folder name cannot be null");

		// Construct and validate folder path
		String folderPath = Guidebookmd.BOOKS_DIRECTORY + "load/" + name + "/";
		Path dir = Paths.get(folderPath).normalize();
		if (!dir.startsWith(Paths.get(Guidebookmd.BOOKS_DIRECTORY))) {
			Guidebookmd.LOGGER.error("Invalid folder name '{}' attempted path traversal", name);
			throw new RuntimeException("Invalid folder name: attempted path traversal: " + name);
		}

		// Check if folder exists
		if (!Files.isDirectory(dir)) {
			Guidebookmd.LOGGER.error("Folder does not exist: {}", folderPath);
			throw new RuntimeException("Folder does not exist: " + folderPath);
		}

		List<String> pages = new ArrayList<>();
		try {
			// Get all .md files in the folder
			File[] files = dir.toFile().listFiles((file, fileName) -> fileName.toLowerCase().endsWith(".md"));
			if (files == null || files.length == 0) {
				Guidebookmd.LOGGER.warn("No Markdown files found in folder: {}", folderPath);
				return pages; // Return empty list
			}

			// Process each .md file
			for (File file : files) {
				try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
					// Read all lines and join with newlines
					String content = reader.lines().collect(Collectors.joining("\n"));
					Guidebookmd.LOGGER.info("Read {} characters from {}", content.length(), file.getPath());
					pages.add(content);
				} catch (IOException e) {
					Guidebookmd.LOGGER.error("Error reading Markdown file {}: {}", file.getPath(), e.getMessage());
					// Continue processing other files
				}
			}

			if (pages.isEmpty()) {
				Guidebookmd.LOGGER.warn("No valid Markdown files read from folder: {}", folderPath);
			} else {
				Guidebookmd.LOGGER.info("Successfully read {} pages from folder: {}", pages.size(), folderPath);
			}

			return pages;

		} catch (Exception e) {
			Guidebookmd.LOGGER.error("Failed to process folder {}: {}", folderPath, e.getMessage());
			throw new RuntimeException("Error processing folder: " + folderPath, e);
		}
	}
}
