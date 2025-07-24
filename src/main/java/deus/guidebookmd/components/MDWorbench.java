package deus.guidebookmd.components;

import net.minecraft.client.gui.ItemElement;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.Item;

public class MDWorbench extends MDFakeSlot {

	protected ItemElement[] elements = new ItemElement[9];
	protected Item[] items = new Item[9];

	public MDWorbench(String itemsStr) {
		super("");
		width = 18 * 3;
		height = 18 * 3;

		for (int i = 0; i < 9; i++) {
			elements[i] = new ItemElement(mc);
		}

		String trimmed = itemsStr.trim();
		if (!trimmed.isEmpty()) {
			String[] split = trimmed.split(",");
			for (int j = 0; j < 9 && j < split.length; j++) {
				String n = split[j].trim();
				if (n.equals("empty")) {
					this.items[j] = null;
				} else {
					this.items[j] = ITEMS.get(n);
				}
			}
		}
	}

	@Override
	public void render(int x, int y) {
		for (int i = 0; i < 9; i++) {
			int row = i / 3;
			int col = i % 3;
			int nx = (x + col * 18) + 28;
			int ny = (y + row * 18);

			drawTexturedIcon(nx, ny, 18, 18, TextureRegistry.getTexture("guidebookmd:gui/hud/slot"));

			if (items[i] != null) {
				elements[i].render(items[i].getDefaultStack(), nx + 1, ny + 1);
			}
		}
	}
}
