package com.skd.arcanetowers.datagen;

import com.skd.arcanetowers.ArcaneTowers;
import com.skd.arcanetowers.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/**
 * Datagen wiring for Arcane Towers: only the en_us lang file.
 *
 * <p>The Exit Stone's blockstate, block models and item model are hand-written in
 * {@code src/main/resources} (the taller owns those paths), so datagen must not emit them.</p>
 */
@EventBusSubscriber(modid = ArcaneTowers.MOD_ID)
public final class DataGenerators {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var packOutput = generator.getPackOutput();

        if (event.includeClient()) {
            generator.addProvider(true, new ArcaneLang(packOutput));
        }
    }

    private static class ArcaneLang extends LanguageProvider {
        ArcaneLang(PackOutput output) {
            super(output, ArcaneTowers.MOD_ID, "en_us");
        }

        @Override
        protected void addTranslations() {
            add(ModBlocks.EXIT_STONE.get(), "Exit Stone");
            add("itemGroup.arcane_towers.arcane_towers", "Arcane Towers");

            add("arcane_towers.exit_stone.inert", "The stone is silent here.");
            add("arcane_towers.exit_stone.used", "The Exit Stone carries you back to the surface.");
            add("arcane_towers.tower.unsealed", "The tower's seal is broken.");
            add("arcane_towers.guardian.awakens", "The guardian of the tower awakens!");

            add("arcane_towers.guardian.stone_tower", "Guardian of the Arcane Stone");
            add("arcane_towers.guardian.frost_tower", "Guardian of the Frost");
            add("arcane_towers.guardian.sandstone_tower", "Guardian of the Sands");
            add("arcane_towers.guardian.umbral_tower", "Guardian of the Umbra");
        }
    }

    private DataGenerators() {}
}
