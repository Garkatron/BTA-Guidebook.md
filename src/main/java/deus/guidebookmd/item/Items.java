package deus.guidebookmd.item;

import deus.guidebookmd.Guidebookmd;
import net.minecraft.core.item.Item;
import turniplabs.halplibe.helper.ItemBuilder;

public class Items {

	public static Item MD_ITEM_GUIDE = null;
	public static Item MD_ITEM_EDITABLE_BOOK = null;
	public static Item MD_ITEM_READONLY_BOOK = null;
	//public static Item MD_ITEM_DARK_GUIDE = null;
	private static final ItemBuilder genericItemBuilder = new ItemBuilder(Guidebookmd.MOD_ID).setStackSize(1);


	public static void initialize() {
		MD_ITEM_GUIDE = genericItemBuilder.build(new ItemGuidebookmd("guide",Guidebookmd.MOD_ID+":item/guide", Guidebookmd.CFG.getInt("IDs.guidebook")));
		MD_ITEM_EDITABLE_BOOK = genericItemBuilder.build(new ItemEditableBook("editable_book",Guidebookmd.MOD_ID+":item/editable_book", Guidebookmd.CFG.getInt("IDs.editable_book"), true));
		MD_ITEM_READONLY_BOOK = genericItemBuilder.build(new ItemEditableBook("readonly_book",Guidebookmd.MOD_ID+":item/readonly_book", Guidebookmd.CFG.getInt("IDs.readonly_book"), false));
	}
}
