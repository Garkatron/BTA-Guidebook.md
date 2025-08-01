package deus.guidebookmd;

import com.mojang.nbt.tags.CompoundTag;
import deus.guidebookmd.config.BookConfig;
import deus.guidebookmd.gui.MDEditablePage;
import deus.guidebookmd.gui.MarkdownGuidebook;
import deus.guidebookmd.gui.elements.PageButton;
import deus.guidebookmd.gui.elements.TextEditor;
import deus.guidebookmd.item.Items;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Screen;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EditableBook extends MarkdownGuidebook<MDEditablePage> {

	// ? State
	private boolean editable = true;

	// ? Components
	private final PageButton exportBookButton = new PageButton("guidebookmd:gui/hud/export");
	private final PageButton addPageButton = new PageButton("guidebookmd:gui/hud/add");
	private final PageButton cloneButton = new PageButton("guidebookmd:gui/hud/clone");
	private final TextEditor titleTextArea = new TextEditor();

	// ? Stuff
	private final ItemStack itemStack;
	private final Player player;

	// ? Constructor
	public EditableBook(Player player, ItemStack itemStack, boolean editable) {
		this.itemStack = itemStack;
		this.player = player;

		String path = "/assets/guidebookmd/markdown/editablebook/";
		this.editable = editable;

		config = BookConfig.fromJsonResource(getClass(), path + "config.json");


		List<List<Character>> content = loadFromStack(itemStack);
		if (content != null && !content.isEmpty()) {
			loadFromLLC(content);
		} else {
			addPage();
		}

		String title = itemStack.getData().getString("title");
		titleTextArea.drawBackground = false;
		titleTextArea.maxTextLength = 15;
		titleTextArea.drawBorder = this.editable;
		titleTextArea.drawCursor = this.editable;
		titleTextArea.drawExtraCursors = false;
		titleTextArea.drawLineCount = false;
		titleTextArea.drawLineCharCount = false;
		titleTextArea.maxLines = 17;
		titleTextArea.height = 170;
		titleTextArea.width = 80;
		titleTextArea.setContent(title.chars()
			.mapToObj(ch -> (char) ch)
			.collect(Collectors.toList()));

		exportBookButton.onClick = this::exportBookButtonOnclick;
		cloneButton.onClick = this::cloneButtonOnclick;
		addPageButton.onClick = this::addPageButtonOnclick;

		exportBookButton.disabled = cloneButton.disabled = addPageButton.disabled = !this.editable;

	}

	public EditableBook(Player player, ItemStack itemStack) {
		this(player, itemStack, itemStack.getData().getBoolean("editable"));
	}

	// ? Saving methods
	public void saveEditorsToItemStack(Player editor, ItemStack stack) {
		CompoundTag data = stack.getData();

		String editorsRaw = data.getString("editors");
		List<String> editors = new ArrayList<>();
		if (editorsRaw != null && !editorsRaw.isEmpty()) {
			for (String name : editorsRaw.split(",")) {
				if (!name.isEmpty()) editors.add(name);
			}
		}

		if (!editors.contains(editor.nickname)) {
			editors.add(editor.nickname);
		}

		String title = titleTextArea.getContentAsString();
		data.putString("title", title.isEmpty() ? "": title);
		data.putString("editors", String.join(",", editors));
		stack.setData(data);
	}

	public void savePagesToItemStack(ItemStack stack) {
		CompoundTag data = stack.getData();

		List<String> pageStrings = new ArrayList<>();
		for (List<Character> page : getAllPagesContent()) {
			StringBuilder sb = new StringBuilder();
			for (Character c : page) {
				if (c == ',') sb.append("\\,");
				else sb.append(c);
			}
			pageStrings.add(sb.toString());
		}
		data.putString("pages", String.join(",", pageStrings));
		stack.setData(data);
	}

	public void completeSaveToItemStack(Player author, ItemStack stack) {
		saveEditorsToItemStack(author, stack);
		savePagesToItemStack(stack);
	}

	public ItemStack createCloneStack(Player author, ItemStack stack, boolean editable) {
		completeSaveToItemStack(author, stack);
		CompoundTag data = stack.getData();
		data.putString("author", author.nickname);
		ItemStack newStack = new ItemStack(editable ? Items.MD_ITEM_EDITABLE_BOOK : Items.MD_ITEM_READONLY_BOOK, 1);
		newStack.setData(stack.getData());
		return newStack;

	}

	// ? Exporting methods
	public ItemStack exportItemStackBook(Player author, ItemStack stack, boolean editable) {
		ItemStack itemStack1 = createCloneStack(author, stack, editable);
		CompoundTag data = itemStack1.getData();
		data.putBoolean("editable", editable);
		itemStack1.setData(data);
		return itemStack1;
	}

	// ? Loading methods
	public static List<List<Character>> loadFromStack(ItemStack stack) {
		List<List<Character>> result = new ArrayList<>();
		CompoundTag data = stack.getData();

		String raw = data.getString("pages");
		if (raw != null && !raw.isEmpty()) {
			StringBuilder current = new StringBuilder();
			List<String> pages = new ArrayList<>();

			boolean escaping = false;
			for (int i = 0; i < raw.length(); i++) {
				char c = raw.charAt(i);
				if (escaping) {
					current.append(c);
					escaping = false;
				} else if (c == '\\') {
					escaping = true;
				} else if (c == ',') {
					pages.add(current.toString());
					current = new StringBuilder();
				} else {
					current.append(c);
				}
			}
			if (current.length() > 0) {
				pages.add(current.toString());
			}

			for (String page : pages) {
				List<Character> charList = new ArrayList<>();
				for (char c : page.toCharArray()) {
					charList.add(c);
				}
				result.add(charList);
			}
		}

		return result;
	}

	public void loadFromLLC(List<List<Character>> content) {
		pages.clear();
		for (List<Character> characters : content) {
			MDEditablePage p = addPage();
			p.textArea.setContent(characters);
			if (!editable) {
				p.compileContent();
			}
		}
	}

	// ? Methods
	@Override
	public void render(int mx, int my, float partialTick) {
		super.render(mx, my, partialTick);
		if (currentPageNumber != -1) {
			for (ButtonElement buttonElement : this.buttons) {
				buttonElement.drawButton(this.mc, mx, my);
			}
			drawButtons(mx, my);
		} else {
			drawTextarea(mx, my);
		}
	}

	@Override
	public void mouseClicked(int mx, int my, int buttonNum) {
		if (!titleTextArea.isHovered()) {
			super.mouseClicked(mx, my, buttonNum);
		}
		cloneButton.mouseClick(mx, my);
		addPageButton.mouseClick(mx, my);
		exportBookButton.mouseClick(mx, my);
	}

	public MDEditablePage addPage() {
		MDEditablePage p = new MDEditablePage(null, new ArrayList<>(), editable);
		pages.add(p);
		shareReferenceToComponents();
		return p;
	}

	@Override
	public void keyPressed(char eventCharacter, int eventKey, int mx, int my) {
		if (eventKey == Keyboard.KEY_ESCAPE) {
			completeSaveToItemStack(player, itemStack);
			this.mc.displayScreen((Screen) null);
		}
		if (eventKey == Keyboard.KEY_TAB) {
			this.mc.displayScreen((Screen) null);
		}
	}
	public void drawTextarea(int mx, int my) {
		titleTextArea.x = (width-titleTextArea.width)/2;
		titleTextArea.y = (height/2)-85;
		if (editable) {
			titleTextArea.updateMousePos(mx, my);
			titleTextArea.update();
		}
		titleTextArea.render();
	}

	public void drawButtons(int mx, int my) {
		int startY = (height/2)-85;
		int buttonX = (width/2)+157;
		int base = 16;

		addPageButton.x = buttonX;
		cloneButton.x = buttonX;
		exportBookButton.x = buttonX;

		addPageButton.y = startY + base;
		cloneButton.y = startY + base*2;
		exportBookButton.y = startY + base*3;

		cloneButton.updateMousePos(mx, my);
		cloneButton.update();
		cloneButton.render();

		addPageButton.updateMousePos(mx, my);
		addPageButton.update();
		addPageButton.render();

		exportBookButton.updateMousePos(mx, my);
		exportBookButton.update();
		exportBookButton.render();
	}

	// ? Utils
	public List<List<Character>> getAllPagesContent() {
		List<List<Character>> data = new ArrayList<>();
		for (MDEditablePage page : pages) {
			data.add(page.textArea.cloneContent());
		}
		return data;
	}

	// ? Buttons
	public void addPageButtonOnclick() {
		addPage();
		goNext();
	}
	public void cloneButtonOnclick() {
		if (player.inventory.consumeInventoryItem(net.minecraft.core.item.Items.BOOK.id)) {
			player.world.dropItem((int) player.x, (int) player.y, (int) player.z, exportItemStackBook(player, itemStack, true));
			player.sendMessage("§aBook successfully cloned.");
		} else {
			player.sendMessage("§cYou need an editable-book to clone this book.");
		}
		this.mc.displayScreen((Screen) null);
	}

	public void exportBookButtonOnclick() {
		if (player.inventory.consumeInventoryItem(net.minecraft.core.item.Items.BOOK.id)) {
			player.world.dropItem((int) player.x, (int) player.y, (int) player.z, exportItemStackBook(player, itemStack, false));
			player.sendMessage("§aBook successfully exported.");
		} else {
			player.sendMessage("§cYou need an book to export this book.");
		}
		this.mc.displayScreen((Screen) null);
	}

	public static void exportMarkdownFolder(Player player, ItemStack stack, String ttitle) {
		Objects.requireNonNull(player, "Player cannot be null");
		Objects.requireNonNull(stack, "ItemStack cannot be null");
		Objects.requireNonNull(ttitle, "Title cannot be null");

		String title = ttitle.trim();
		if (title.isEmpty()) {
			title = "unnamed_book";
		}
		title = title.replaceAll("[<>:\"/\\\\|?*]", "_");

		String directoryPath = Guidebookmd.BOOKS_DIRECTORY + "/exported/" + title ;
		Path dir = Paths.get(directoryPath);

		try {
			Files.createDirectories(dir);

			// Load content
			List<List<Character>> content = loadFromStack(stack);
			if (content == null || content.isEmpty()) {
				Guidebookmd.LOGGER.warn("No pages found in ItemStack for book: {}", title);
				player.sendMessage("No pages to export");
				return;
			}

			// Convert content to pages with proper line breaks
			List<String> pages = content.stream()
				.map(characters -> {
					if (characters == null || characters.isEmpty()) {
						return "";
					}

					Object linesResult = TextEditor.getLines(characters, 26, true);
					if (linesResult instanceof List) {

						return ((List<String>) linesResult).stream()
							.collect(Collectors.joining("\n"));
					} else {
						return linesResult.toString();
					}
				})
				.collect(Collectors.toList());

			if (pages.isEmpty()) {
				Guidebookmd.LOGGER.error("No pages to export for book: {}", title);
				return;
			}

			DecimalFormat formatter = new DecimalFormat("000");

			for (int i = 0; i < pages.size(); i++) {

				String fileName = "page_" + formatter.format(i + 1) + ".md";
				Path filePath = dir.resolve(fileName);

				try (FileWriter fileWriter = new FileWriter(filePath.toFile())) {
					fileWriter.write(pages.get(i));
				} catch (IOException e) {
					Guidebookmd.LOGGER.error("Failed to write page {}: {}", fileName, e.getMessage());
				}
			}

			player.sendMessage("Successfully exported " + pages.size() + " pages to " + directoryPath);
			Guidebookmd.LOGGER.info("Successfully exported {} pages to {}", pages.size(), directoryPath);

		} catch (IOException e) {
			Guidebookmd.LOGGER.error("Failed to create directory or export pages for book {}: {}", title, e.getMessage());
		}
	}


}
