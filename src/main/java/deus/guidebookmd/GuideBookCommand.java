package deus.guidebookmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeString;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.nbt.tags.CompoundTag;
import deus.guidebookmd.gui.elements.TextArea;
import deus.guidebookmd.item.Items;
import deus.guidebookmd.utils.RLoading;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;


public class GuideBookCommand implements CommandManager.CommandRegistry {

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(
			ArgumentBuilderLiteral.<CommandSource>literal("guidebook")
				.then(
					ArgumentBuilderLiteral.<CommandSource>literal("export")
						.then(
							ArgumentBuilderRequired.<CommandSource, String>argument("title", ArgumentTypeString.string())
								.executes(this::executeExport)
						)
				)	.then(
					ArgumentBuilderLiteral.<CommandSource>literal("load")
						.then(
							ArgumentBuilderRequired.<CommandSource, String>argument("title", ArgumentTypeString.string())
								.executes(this::executeLoad)
						)
				)
		);
	}

	private int executeExport(CommandContext<CommandSource> context) {
		CommandSource source = context.getSource();
		Player player = source.getSender();
		String title = context.getArgument("title", String.class);

		// Check if player is holding an item
		ItemStack stack = player.inventory.getCurrentItem();
		if (stack == null) {
			source.sendMessage("You must be holding an editable-book to export");
			return 0;
		}

		// Check if the held item is an editable book
		if (stack.itemID != Items.MD_ITEM_EDITABLE_BOOK.id) {
			source.sendMessage("You must hold an editable book to export");
			return 0;
		}

		try {
			EditableBook.exportMarkdownFolder(player, stack, title);
			return 1; // Success
		} catch (Exception e) {
			Guidebookmd.LOGGER.error("Failed to export book '{}': {}", title, e.getMessage());
			source.sendMessage("Failed to export book: " + e.getMessage());
			return 0; // Failure
		}
	}
	private int executeLoad(CommandContext<CommandSource> context) {
		CommandSource source = context.getSource();
		Player player = source.getSender();
		String title = context.getArgument("title", String.class);

		// Check if player is holding an item
		ItemStack stack = player.inventory.getCurrentItem();
		if (stack == null) {
			source.sendMessage("You must be holding an book to load a book");
			return 0;
		}

		// Check if the held item is an editable book
		if (stack.itemID != Items.MD_ITEM_EDITABLE_BOOK.id) {
			source.sendMessage("You must hold an editable book to load a book");
			return 0;
		}

		try {

			if (player.inventory.consumeInventoryItem(net.minecraft.core.item.Items.BOOK.id)) {

				ItemStack newStack = new ItemStack(Items.MD_ITEM_EDITABLE_BOOK, 1);
				CompoundTag compoundTag = newStack.getData();
				String pages = TextArea.fuseStrings(RLoading.loadMarkdownFilesFromFolderName(title));
				compoundTag.putString("pages", pages);
				newStack.setData(compoundTag);
				player.world.dropItem((int) player.x, (int) player.y, (int) player.z, newStack);

				player.sendMessage("§aBook successfully loaded.");
			} else {
				player.sendMessage("§cYou need an book to load a book.");
			}
			return 1; // Success
		} catch (Exception e) {
			Guidebookmd.LOGGER.error("Failed to load book '{}': {}", title, e.getMessage());
			source.sendMessage("Failed to load book: " + e.getMessage());
			return 0; // Failure
		}
	}
}
