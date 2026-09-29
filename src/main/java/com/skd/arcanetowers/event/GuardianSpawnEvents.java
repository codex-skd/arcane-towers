package com.skd.arcanetowers.event;

import com.skd.arcanetowers.ArcaneTowers;
import com.skd.arcanetowers.world.GuardianData;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * Spawns each tower's guardian the first time a player stands in its {@code dungeon_end} room.
 *
 * <p>Runs on the server every 20 ticks; once a room has spawned its guardian the fact is remembered in
 * {@link GuardianData} so it never happens twice.</p>
 */
public final class GuardianSpawnEvents {

    private static final int CHECK_INTERVAL = 20;
    private static final double GUARDIAN_BONUS = 0.5;

    private static final ResourceLocation GUARDIAN_BONUS_ID =
            ResourceLocation.fromNamespaceAndPath(ArcaneTowers.MOD_ID, "guardian_bonus");

    /** Tower id -> guardian entity id (majestic_bestiary). */
    private static final List<TowerGuardian> TOWERS = List.of(
            new TowerGuardian("stone_tower", "tauren"),
            new TowerGuardian("frost_tower", "goliath"),
            new TowerGuardian("sandstone_tower", "centaur"),
            new TowerGuardian("umbral_tower", "giant")
    );

    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        if (player.tickCount % CHECK_INTERVAL != 0) {
            return;
        }

        BlockPos pos = player.blockPosition();
        var registry = serverLevel.registryAccess().registryOrThrow(Registries.STRUCTURE);

        for (TowerGuardian tower : TOWERS) {
            Structure structure = registry.get(tower.key());
            if (structure == null) {
                continue;
            }
            StructureStart start = serverLevel.structureManager().getStructureAt(pos, structure);
            if (!start.isValid()) {
                continue;
            }
            Optional<BoundingBox> endBox = findPieceBox(start, tower.id() + "/dungeon_end");
            if (endBox.isEmpty()) {
                continue;
            }
            BoundingBox box = endBox.get();
            if (!box.isInside(pos)) {
                continue;
            }
            trySpawn(serverLevel, player, tower, box);
            return;
        }
    }

    private static void trySpawn(ServerLevel level, Player player, TowerGuardian tower, BoundingBox box) {
        GuardianData data = level.getDataStorage().computeIfAbsent(GuardianData.factory(), "arcane_towers_guardians");
        long pieceKey = new BlockPos(box.minX(), box.minY(), box.minZ()).asLong() * 31L + tower.id().hashCode();
        if (data.isSpawned(pieceKey)) {
            return;
        }

        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(
                ResourceLocation.fromNamespaceAndPath("majestic_bestiary", tower.guardian()));
        if (type == null) {
            data.markSpawned(pieceKey);
            return;
        }

        BlockPos centre = floorCentre(level, box);
        Mob guardian = spawnMob(type, level, centre);
        if (guardian == null) {
            return;
        }

        applyBossTraits(guardian, tower.id());
        data.markSpawned(pieceKey);

        player.playNotifySound(SoundEvents.RAID_HORN.value(), SoundSource.HOSTILE, 1.0f, 1.0f);
        player.displayClientMessage(Component.translatable("arcane_towers.guardian.awakens"), true);
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    private static Mob spawnMob(EntityType<?> type, ServerLevel level, BlockPos pos) {
        if (!(type.create(level) instanceof Mob mob)) {
            return null;
        }
        mob.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, level.random.nextFloat() * 360.0f, 0.0f);
        try {
            mob.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null);
        } catch (Exception exception) {
            ArcaneTowers.LOGGER.warn("Guardian {} failed to finalize spawn", type, exception);
        }
        level.addFreshEntity(mob);
        return mob;
    }

    private static void applyBossTraits(Mob guardian, String towerId) {
        AttributeInstance maxHealth = guardian.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.addPermanentModifier(new AttributeModifier(
                    GUARDIAN_BONUS_ID, GUARDIAN_BONUS, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            guardian.setHealth(guardian.getMaxHealth());
        }
        guardian.setPersistenceRequired();
        guardian.setCustomName(Component.translatable("arcane_towers.guardian." + towerId));
    }

    /** Finds the first collision-free Y from {@code minY + 1} upwards, at the room's centre column. */
    private static BlockPos floorCentre(ServerLevel level, BoundingBox box) {
        int x = (box.minX() + box.maxX()) / 2;
        int z = (box.minZ() + box.maxZ()) / 2;
        for (int y = box.minY() + 1; y <= box.maxY(); y++) {
            BlockPos pos = new BlockPos(x, y, z);
            if (level.noCollision(new net.minecraft.world.phys.AABB(
                    x, y, z, x + 1.0, y + 1.0, z + 1.0))) {
                return pos;
            }
        }
        return new BlockPos(x, box.minY() + 1, z);
    }

    /**
     * Finds the bounding box of the piece whose template is {@code arcane_towers:<name>}.
     *
     * <p>{@link SinglePoolElement}'s template has no getter in 1.21.1, so the element is matched by its
     * string form, which reads {@code "Single[Left[arcane_towers:<name>]]"}.</p>
     */
    private static Optional<BoundingBox> findPieceBox(StructureStart start, String name) {
        String template = ArcaneTowers.MOD_ID + ":" + name;
        for (StructurePiece piece : start.getPieces()) {
            if (!(piece instanceof PoolElementStructurePiece poolPiece)) {
                continue;
            }
            StructurePoolElement element = poolPiece.getElement();
            if (!(element instanceof SinglePoolElement)) {
                continue;
            }
            if (element.toString().contains(template)) {
                return Optional.of(poolPiece.getBoundingBox());
            }
        }
        return Optional.empty();
    }

    private record TowerGuardian(String id, String guardian) {
        ResourceKey<Structure> key() {
            return ResourceKey.create(Registries.STRUCTURE,
                    ResourceLocation.fromNamespaceAndPath(ArcaneTowers.MOD_ID, this.id));
        }
    }

    private GuardianSpawnEvents() {}
}
