package deus.guidebookmd.item;

import deus.guidebookmd.gui.MDBookScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

public class ItemPage extends Item {
	public ItemPage(String translationKey, String namespaceId, int id) {
		super(translationKey, namespaceId, id);
	}

	@Override
	public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
		Minecraft.getMinecraft().displayScreen(new MDBookScreen());
		System.out.println(namespaceID.toString());
		return super.onUseItem(itemstack, world, entityplayer);
	}
}
