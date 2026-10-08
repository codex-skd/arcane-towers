"""Swap decorative blocks in the underground pieces for majestic_core ones.

Only the dungeon pieces are touched, never tower.nbt (the surface tower keeps its vanilla look).
Swaps are decoration only: no container, spawner, door or structural-only block is replaced.
Run from the repo root: python scripts/apply_order_decor.py
"""
import glob, hashlib, os
import nbtlib
from nbtlib import Compound, String

ROOT = "src/main/resources/data/arcane_towers/structure"
C = "majestic_core:"

# vanilla id -> majestic_core id, whole block (every occurrence)
FULL = {
    "all": {"minecraft:lantern": C + "rune_lantern", "minecraft:soul_lantern": C + "rune_lantern"},
    "stone_tower": {
        "minecraft:chiseled_stone_bricks": C + "chiseled_arcane_brick",
        "minecraft:cracked_stone_bricks": C + "cracked_arcane_brick",
        "minecraft:mossy_stone_bricks": C + "mossy_arcane_brick",
    },
    "frost_tower": {
        "minecraft:chiseled_stone_bricks": C + "chiseled_arcane_brick",
        "minecraft:cracked_stone_bricks": C + "cracked_arcane_brick",
    },
    "sandstone_tower": {
        "minecraft:cut_copper": C + "aged_bronze",
        "minecraft:oxidized_copper": C + "aged_bronze",
    },
    "umbral_tower": {
        "minecraft:crying_obsidian": C + "runic_obsidian",
    },
}
# vanilla id -> (majestic_core id, 1-in-N positions): sprinkled so the wall reads as mixed masonry
SPRINKLE = {
    "stone_tower": {"minecraft:stone_bricks": (C + "arcane_brick", 6)},
    "frost_tower": {"minecraft:stone_bricks": (C + "arcane_brick", 6)},
    "umbral_tower": {"minecraft:deepslate_bricks": (C + "runic_brick", 10)},
}


def pick(path, pos, n):
    h = hashlib.md5(f"{os.path.basename(path)}{tuple(int(v) for v in pos)}".encode()).digest()
    return int.from_bytes(h[:4], "big") % n == 0


def main():
    total = {}
    for tower in ("stone_tower", "frost_tower", "sandstone_tower", "umbral_tower"):
        for path in sorted(glob.glob(f"{ROOT}/{tower}/*.nbt")):
            if os.path.basename(path) == "tower.nbt":
                continue
            nbt = nbtlib.load(path)
            palette = nbt["palette"]
            names = [str(p["Name"]) for p in palette]
            full = {**FULL["all"], **FULL.get(tower, {})}
            changed = 0
            for i, nm in enumerate(names):
                if nm in full:
                    palette[i]["Name"] = String(full[nm])
                    changed += sum(1 for b in nbt["blocks"] if int(b["state"]) == i)
            for src, (dst, n) in SPRINKLE.get(tower, {}).items():
                if src not in names:
                    continue
                i = names.index(src)
                palette.append(Compound({"Name": String(dst)}))
                j = len(palette) - 1
                for b in nbt["blocks"]:
                    if int(b["state"]) == i and pick(path, b["pos"], n):
                        b["state"] = type(b["state"])(j)
                        changed += 1
            nbt.save(path, gzipped=True)
            total[tower] = total.get(tower, 0) + changed
    print(total)


if __name__ == "__main__":
    main()
