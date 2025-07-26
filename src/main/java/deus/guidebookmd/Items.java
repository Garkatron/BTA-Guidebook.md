package deus.guidebookmd;

import deus.guidebookmd.item.ItemGuidebookmd;
import net.minecraft.core.item.Item;
import turniplabs.halplibe.helper.ItemBuilder;

public class Items {

	public static Item MD_ITEM_GUIDE = null;
	//public static Item MD_ITEM_DARK_GUIDE = null;
	private static final ItemBuilder genericItemBuilder = new ItemBuilder(Guidebookmd.MOD_ID);


	public static void initialize() {
		MD_ITEM_GUIDE = genericItemBuilder.build(new ItemGuidebookmd("guide",Guidebookmd.MOD_ID+":item/guide", 23001));
		//MD_ITEM_DARK_GUIDE = genericItemBuilder.build(new ItemGuidebookmd("dark_guide",Guidebookmd.MOD_ID+":item/dark_guide", 23002));
	}
}
