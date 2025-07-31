package deus.guidebookmd.components;

import deus.guidebookmd.Guidebookmd;
import net.minecraft.client.gui.ItemElement;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.HardIllegalArgumentException;
import net.minecraft.core.util.collection.NamespaceID;

public class MDFakeSlot extends MDComponent {

	private static final Item DEFAULT_ITEM = Items.AMMO_PEBBLE;

	Item item;
	ItemElement itemElement;

	public MDFakeSlot(String item) {
		try {
			this.item = Item.itemsMap.getOrDefault(NamespaceID.getTemp(item), DEFAULT_ITEM);
			this.itemElement = new ItemElement(mc);
			width = 18;
			height = 20;
		} catch (HardIllegalArgumentException e) {
			Guidebookmd.LOGGER.error(e.toString());
		}
	}

	@Override
	public void render(int x, int y, int mx, int my) {
		super.render(x, y, mx, my);

		drawTexturedIcon(x, y, width, height-2, TextureRegistry.getTexture("guidebookmd:gui/hud/slot"));
		this.itemElement.render(item.getDefaultStack(), x+1, y+1);
	}


}
