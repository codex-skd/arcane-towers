package com.skd.arcanetowers;

import com.skd.arcanetowers.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Creative tab and display entries of Arcane Towers. */
public final class ModCreativeTabs {

    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArcaneTowers.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ARCANE_TOWERS = TABS.register(
            "arcane_towers",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.arcane_towers.arcane_towers"))
                    .icon(() -> new ItemStack(ModBlocks.EXIT_STONE.get()))
                    .displayItems((parameters, output) -> output.accept(ModBlocks.EXIT_STONE.get()))
                    .build()
    );

    private ModCreativeTabs() {}

    public static void register(IEventBus modEventBus) {
        TABS.register(modEventBus);
    }
}
