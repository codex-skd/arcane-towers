# Arcane Towers — taller work order (2026-09-29)

> New mod `arcane_towers` (namespace `arcane_towers`, Minecraft 1.21.1, DataVersion 3955). Four tower
> dungeons + one block. Design reference (Spanish): `arcane_towers/neoforge/1.21.1/docs/DESIGN_ARCANE_TOWERS_1-21-1.md`.
>
> **Delivery**: produce and validate inside `taller_minecraft` only (e.g. `structures/arcane_towers/<id>/output/1.21.1/`
> and `blocks/arcane_towers/output/1.21.1/`), with validation file, previews and README. Never copy into the mod —
> we pull from the taller. Output paths mirror the mod: `data/arcane_towers/structure/<id>/<piece>.nbt`,
> `assets/arcane_towers/{models,textures,blockstates}/...`.

---

## Task A — Four tower dungeons (`structures/arcane_towers/<id>`)

Four **different** towers, one per biome family:

| id | Theme / biomes | Palette | Floor creatures (spawners) | Dungeon spawners | Guardian room |
|---|---|---|---|---|---|
| `stone_tower` | Arcane stone tower — plains, forests, meadows | stone bricks, andesite, tuff, weathered/oxidized copper, amethyst accents | `goblin`, `gnome`, `elf` | `undead` | for a **tauren** (~1.5× player) |
| `frost_tower` | Frost tower — snowy plains/slopes, groves, mountains | stone bricks, packed/blue ice, calcite, diorite, soul lanterns | `wolf`, `satyr` | `undead` | for a **goliath** (2.5×, ~4.5 blocks tall) |
| `sandstone_tower` | Sandstone tower — desert, badlands, savanna | cut/chiseled/smooth sandstone, terracotta, gold/copper trims | `goblin`, `arcane_mage` | `undead` | for a **centaur** (1.8×, long body) |
| `umbral_tower` | Umbral tower — dark forest, swamp, mangrove | deepslate bricks/tiles, basalt, dark oak, soul lanterns, sculk accents | `undead`, `satyr`, `arcane_mage` | `undead` | for a **giant** (2.5×, ~4.5 blocks tall) |

Each tower must have its own silhouette (not a recolour): e.g. round stone tower with a copper-dome observatory
top; tiered frost spire with icicle buttresses; square stepped sandstone ziggurat-tower with a gold crown; crooked
leaning umbral tower with a broken top.

### A1. Tower piece `tower.nbt` (one per id)
- **Width**: large enough to climb comfortably — interior at least **15×15** per floor.
- **Central column**: a closed, empty shaft with a **4×4 hollow interior** surrounded by walls (6×6 outside),
  running from the top floor down through the whole tower and **24 blocks below the ground floor**.
  It is closed on every floor **except the last floor** (the top floor below the roof), where one side has a
  2-wide × 3-tall opening so the player can jump in. Never open on the ground floor.
- **Stairs** climb around the column (spiral or switchback), **at least 4 floors + roof**, ~6 blocks per floor.
- **Ground floor** at **y = 24** of the NBT (the 24 blocks below are the underground shaft section: only the
  shaft walls, the rest `structure_void`, so terrain fills in). Entrance door on the **−Z (north)** face.
- **Spawners**: 1-2 per floor, `minecraft:spawner` with `SpawnData.entity.id` = `majestic_bestiary:<creature>`
  from the table (vary per floor). Keep them away from the stairs so they can be fought.
- **Chests**: 1 per floor, loot table `arcane_towers:chests/<id>_floor`.
- Jigsaw at the **bottom of the shaft** (y = 0, inside the 4×4 hollow, facing down): name
  `arcane_towers:shaft_bottom`, target `arcane_towers:dungeon_entry`, pool `arcane_towers:<id>/dungeon_entry`,
  final state `minecraft:air`.

### A2. Dungeon pieces (one set per id, underground)
- `dungeon_entry.nbt` — landing room under the shaft: the shaft opens into a **water pool 2 deep, 4×4** exactly
  below it (the fall is ~30+ blocks). Jigsaw on top matching `arcane_towers:dungeon_entry` (name) ← shaft
  (facing up). 1-2 exits with jigsaws `arcane_towers:corridor` → pool `arcane_towers:<id>/dungeon_rooms`.
- `dungeon_rooms` pool — 3-4 pieces (`room_a`, `room_b`, `corridor_a`, `corridor_b`), each with `undead` spawners,
  optional chest (`<id>_floor`), and exits `arcane_towers:corridor` → same pool, at least one piece with a jigsaw
  `arcane_towers:end` → pool `arcane_towers:<id>/dungeon_end`. Keep the whole chain within 116 blocks of the start.
- `dungeon_end.nbt` — the guardian hall: open floor **at least 15×8×15** (19×10×19 for goliath/giant), no
  spawners (the mod spawns the guardian once, at the room centre), a treasure chest (`arcane_towers:chests/<id>_vault`)
  and the **Exit Stone** (`arcane_towers:exit_stone`, 2 blocks tall: `half=lower` + `half=upper` above it, facing
  into the room) on a small dais at the far end. Record its offset and the room centre in the README.
- All dungeon pieces: fully enclosed (solid walls/floor/ceiling), torches/lanterns for light at spawners' edges.

### A3. Validation (hard requirements)
- `VALIDATION OK` for every piece; jigsaw names/targets/pools exactly as above.
- **Walkability BFS** (lesson from the observatory): from the tower door you can walk to the top-floor shaft
  opening; from the `dungeon_entry` pool you can walk to the Exit Stone in every generated chain.
- The shaft is sealed on every floor but the last; the 4×4 pool is exactly under the shaft.
- Previews: side cut-away of each tower (floors + shaft), top views, dungeon layout.

## Task B — Exit Stone block (`blocks/arcane_towers`)
- `arcane_towers:exit_stone`: a **two-block-tall** standing stone with **pointed, jagged ends**, **resting on one
  of its tips** against the floor (leaning, like a shard driven into the ground), with faint glowing runes.
- Deliver: `blockstates/exit_stone.json` (properties `half=lower|upper`, `facing=north|south|east|west`),
  `models/block/exit_stone_lower.json`, `models/block/exit_stone_upper.json`, `models/item/exit_stone.json`,
  textures under `textures/block/`. 16×16 grid elements (rotated elements allowed).
- README: **collision boxes** per half (x1,y1,z1,x2,y2,z2 px) for the VoxelShape, per facing north (the mod rotates).

## Done means
- 4 towers × (tower + dungeon_entry + 3-4 room pieces + dungeon_end), all validated + BFS OK + previews.
- Exit Stone models/textures/blockstate + collision boxes + preview.
- Delivery note listing every file and the documented offsets (ground floor y, door, shaft, exit stone, hall centre).
