package com.skd.arcanetowers.world;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

/**
 * Level-persisted record of which tower dungeon-end rooms have already spawned their guardian.
 *
 * <p>Keyed by the piece's bounding-box minimum corner combined with the structure id, so each structure
 * hosts a single guardian even across restarts.</p>
 */
public class GuardianData extends SavedData {

    private static final String SPAWNED_KEY = "Spawned";

    private final Set<Long> spawned = new HashSet<>();

    public GuardianData() {}

    public boolean isSpawned(long key) {
        return this.spawned.contains(key);
    }

    public void markSpawned(long key) {
        this.spawned.add(key);
        this.setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putLongArray(SPAWNED_KEY, this.spawned.stream().mapToLong(Long::longValue).toArray());
        return tag;
    }

    public static GuardianData load(CompoundTag tag, HolderLookup.Provider registries) {
        GuardianData data = new GuardianData();
        for (long key : tag.getLongArray(SPAWNED_KEY)) {
            data.spawned.add(key);
        }
        return data;
    }

    public static SavedData.Factory<GuardianData> factory() {
        return new SavedData.Factory<>(GuardianData::new, GuardianData::load);
    }
}
