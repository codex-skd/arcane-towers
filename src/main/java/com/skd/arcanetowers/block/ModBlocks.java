package com.skd.arcanetowers.block;

import com.skd.arcanetowers.ArcaneTowers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Blocks of Arcane Towers.
 *
 * <p>Only the Exit Stone for now: the two-block-tall standing stone that unseals a tower and teleports
 * the player back to the surface.</p>
 */
public final class ModBlocks {

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ArcaneTowers.MOD_ID);

    public static final DeferredBlock<ExitStoneBlock> EXIT_STONE = BLOCKS.registerBlock(
            "exit_stone",
            ExitStoneBlock::new,
            BlockBehaviour.Properties.of()
                    .sound(SoundType.STONE)
                    .strength(50.0f, 1200.0f)
                    .noOcclusion()
                    .lightLevel(state -> 5)
                    .requiresCorrectToolForDrops()
    );

    private ModBlocks() {}

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
