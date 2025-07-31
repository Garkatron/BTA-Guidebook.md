package deus.guidebookmd.block;

import deus.guidebookmd.Guidebookmd;
import deus.guidebookmd.block.printer.BlockPrinterLogic;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.sound.BlockSound;
import net.minecraft.core.sound.BlockSounds;
import turniplabs.halplibe.helper.BlockBuilder;

public class MDBlocks {
	public static Block<? extends BlockLogic> BLOCK_PRINTER;

	private static int BLOCK_ID = 12000;

	static BlockBuilder genericBlockBuilder = new BlockBuilder(Guidebookmd.MOD_ID)
		.setBlockSound(BlockSounds.STONE)
		.setBlockSound(new BlockSound("step.stone", "step.stone", 1.0f, 1.0f))
		.setTags(BlockTags.MINEABLE_BY_PICKAXE)
		;

	public static void initialize() {
		BLOCK_PRINTER = genericBlockBuilder.build("printer", "printer", newBlockID(), (b) -> { return new BlockPrinterLogic(b, Material.steel);});
	}

	public static int newBlockID() {
		BLOCK_ID = BLOCK_ID + 1;
		return BLOCK_ID;
	}
}
