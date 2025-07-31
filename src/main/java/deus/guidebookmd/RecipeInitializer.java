package deus.guidebookmd;


import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.data.registry.recipe.RecipeRegistry;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import turniplabs.halplibe.helper.RecipeBuilder;

public class RecipeInitializer extends RecipeRegistry {
	public static final RecipeNamespace MDBOOKS = new RecipeNamespace();

	public static void InitRecipes() {
		RecipeBuilder.Shaped(Guidebookmd.MOD_ID)
			.setShape("P  ", "B  ", "   ")
			.addInput('P', Items.PAINTBRUSH)
			.addInput('B', Items.BOOK)
			.create("guidebookmd:recipe/editable_book", deus.guidebookmd.item.Items.MD_ITEM_EDITABLE_BOOK.getDefaultStack());
	}

	public static void InitNameSpaces() {
		final RecipeGroup<RecipeEntryCrafting<?, ?>> WORKBENCH = new RecipeGroup<>(
			new RecipeSymbol(new ItemStack(deus.guidebookmd.item.Items.MD_ITEM_GUIDE))
		);

		MDBOOKS.register("guidebokmd", WORKBENCH);

		Registries.RECIPES.register(Guidebookmd.MOD_ID, MDBOOKS);
	}
}
