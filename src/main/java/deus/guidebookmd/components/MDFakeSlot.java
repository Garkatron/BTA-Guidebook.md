package deus.guidebookmd.components;

import net.minecraft.client.gui.ItemElement;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.Items;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MDFakeSlot extends MDComponent{

	protected static final Map<String, Item> ITEMS = initializeItems();
	private static final Item DEFAULT_ITEM = Items.AMMO_PEBBLE;

	Item item;
	ItemElement itemElement;

	public MDFakeSlot(String item) {
		this.item = ITEMS.getOrDefault(item, Items.AMMO_PEBBLE);
		this.itemElement = new ItemElement(mc);
		width = 18;
		height = 18;
	}

	@Override
	public void render(int x, int y, int mx, int my) {
		super.render(x+10, y, mx, my);

		drawTexturedIcon(x+10 , y,width, height, TextureRegistry.getTexture("guidebookmd:gui/hud/slot"));
		this.itemElement.render(item.getDefaultStack(), x+10, y);
	}

	private static Map<String, Item> initializeItems() {
		Map<String, Item> items = new HashMap<>();
		try {
			Field[] fields = Items.class.getDeclaredFields();
			for (Field field : fields) {
				if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) &&
					Item.class.isAssignableFrom(field.getType())) {
					field.setAccessible(true);
					Object value = field.get(null);
					if (value instanceof Item) {
						items.put(((Item) value).namespaceID.toString(), (Item) value);
					}
				}
			}
		} catch (IllegalAccessException e) {
			System.err.println("Failed to initialize items map: " + e.getMessage());
			e.printStackTrace();
		}
		return items;
	}
}
