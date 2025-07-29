package deus.guidebookmd.item;

import deus.guidebookmd.EditableBook;
import deus.guidebookmd.IntroBook;
import deus.guidebookmd.gui.MarkdownEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

public class ItemEditorBook extends Item {
	public ItemEditorBook(String translationKey, String namespaceId, int id) {
		super(translationKey, namespaceId, id);
	}

	@Override
	public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
		Minecraft.getMinecraft().displayScreen(new EditableBook());
		return super.onUseItem(itemstack, world, entityplayer);
	}
}
