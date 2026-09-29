package com.skd.arcanetowers;

import com.skd.arcanetowers.block.ModBlocks;
import com.skd.arcanetowers.event.GuardianSpawnEvents;
import com.skd.expeditioncore.protection.StructureProtection;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main entry point for Arcane Towers — four arcane tower dungeons of the Majestic ecosystem.
 *
 * <p>Each tower is climbed floor by floor around a sealed central shaft; from the top floor the player
 * drops down the shaft into the underground dungeon, defeats its guardian and uses the Exit Stone to
 * leave. Towers stay protected (expedition_core structure protection) until their Exit Stone is used.
 * Creatures come from {@code majestic_bestiary}.</p>
 *
 * <p>Full design: {@code docs/DESIGN_ARCANE_TOWERS_1-21-1.md}.</p>
 */
@Mod(ArcaneTowers.MOD_ID)
public final class ArcaneTowers {

    public static final String MOD_ID = "arcane_towers";

    public static final Logger LOGGER = LoggerFactory.getLogger("Arcane Towers");

    /** The four tower structure ids, matching the ids in {@code data/arcane_towers/worldgen}. */
    private static final String[] TOWER_IDS = {
            "stone_tower", "frost_tower", "sandstone_tower", "umbral_tower"
    };

    public ArcaneTowers(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        for (String id : TOWER_IDS) {
            StructureProtection.protect(towerKey(id), 1);
        }

        NeoForge.EVENT_BUS.addListener(GuardianSpawnEvents::onPlayerTick);

        LOGGER.info("Arcane Towers v{} loading", modContainer.getModInfo().getVersion());
    }

    /** The structure key of one of the four towers. */
    public static ResourceKey<Structure> towerKey(String id) {
        return ResourceKey.create(Registries.STRUCTURE,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, id));
    }

    /** The four tower structure keys. */
    public static ResourceKey<Structure>[] towerKeys() {
        @SuppressWarnings("unchecked")
        ResourceKey<Structure>[] keys = new ResourceKey[TOWER_IDS.length];
        for (int i = 0; i < TOWER_IDS.length; i++) {
            keys[i] = towerKey(TOWER_IDS[i]);
        }
        return keys;
    }
}
