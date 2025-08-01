package deus.guidebookmd.components;

import deus.guidebookmd.Guidebookmd;
import net.minecraft.client.gui.ItemElement;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.HardIllegalArgumentException;
import net.minecraft.core.util.collection.NamespaceID;

public class MDWorbench extends MDFakeSlot {

	protected ItemElement[] elements = new ItemElement[10];
	protected Item[] items = new Item[10];

	public MDWorbench(String itemsStr) {
		super(Items.AMMO_PEBBLE.namespaceID.toString());
		width = 18 * 3;
		height = 18 * 3;

		for (int i = 0; i < 10; i++) {
			elements[i] = new ItemElement(mc);
		}

		try {
			String trimmed = itemsStr.trim();
			if (!trimmed.isEmpty()) {
				String[] split = trimmed.split(",");
				for (int j = 0; j < 10 && j < split.length; j++) {
					String n = split[j].trim();
					if (n.equals("empty")) {
						this.items[j] = null;
					} else {
						NamespaceID namespace = NamespaceID.getTemp(n);
						if (Item.itemsMap.containsKey(namespace)) {
							this.items[j] = Item.itemsMap.getOrDefault(namespace, Items.AMMO_PEBBLE);
						}
					}
				}
			}
		} catch (HardIllegalArgumentException e) {
			Guidebookmd.LOGGER.error("Failed to parse itemsStr: " + itemsStr, e);
		}
	}

	@Override
	public void render(int x, int y, int mx, int my) {
		// Render 3x3 grid
		for (int i = 0; i < 9; i++) {
			int row = i / 3;
			int col = i % 3;
			int nx = x + 28 + col * 18;
			int ny = y + row * 18;

			drawTexturedIcon(nx, ny, 18, 18, TextureRegistry.getTexture("guidebookmd:gui/hud/slot"));

			if (items[i] != null) {
				elements[i].render(items[i].getDefaultStack(), nx + 1, ny + 1);
			}
		}

		int outputX = x + 28 + 3 * 18 + 18;
		int outputY = y + 18;
		drawTexturedIcon(outputX, outputY, 18, 18, TextureRegistry.getTexture("guidebookmd:gui/hud/slot"));

		if (items[9] != null) {
			elements[9].render(items[9].getDefaultStack(), outputX + 1, outputY + 1);
		}
	}
}
