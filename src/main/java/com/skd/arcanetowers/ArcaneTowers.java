package com.skd.arcanetowers;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
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

    public ArcaneTowers(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Arcane Towers v{} loading", modContainer.getModInfo().getVersion());
    }
}
