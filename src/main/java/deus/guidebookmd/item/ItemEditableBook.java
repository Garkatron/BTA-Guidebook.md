package deus.guidebookmd.item;

import com.mojang.nbt.tags.CompoundTag;
import deus.guidebookmd.EditableBook;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

import java.util.ArrayList;
import java.util.List;

public class ItemEditableBook extends Item {


	private boolean editable = true;
	public ItemEditableBook(String translationKey, String namespaceId, int id, boolean editable) {
		super(translationKey, namespaceId, id);
		this.setMaxStackSize(1);
		this.editable = editable;
	}

	@Override
	public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
		Minecraft.getMinecraft().displayScreen(new EditableBook(entityplayer, itemstack, editable));
		return itemstack;
	}



}
