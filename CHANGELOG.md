# Changelog

All notable changes to this project will be documented in this file.

## [1.0.0]

First stable release.

### Added
- **Majestic Core is now a required dependency.** The underground halls, corridors and side rooms are
  decorated with the Order's masonry and lanterns from it. Only the dungeon pieces change; the
  surface towers keep their vanilla palettes.
  - All four dungeons: `lantern` and `soul_lantern` become `majestic_core:rune_lantern`.
  - Tower of Arcane Stone and Frost Tower: chiseled, cracked and (Arcane Stone only) mossy stone
    bricks become the matching Arcane Brick variant, and about one stone brick in six becomes plain
    Arcane Brick.
  - Sandstone Tower: cut and oxidised copper become Aged Bronze.
  - Umbral Tower: crying obsidian becomes Runic
    Obsidian, and about one deepslate brick in ten becomes Runic Brick.
- `scripts/apply_order_decor.py` re-applies those swaps after a workshop re-import.

### Notes
- Built against Majestic Core 0.0.0-beta.11. Not verified in game.

## [0.0.0-beta.7]

### Fixed
- **Three of the four towers could not be climbed.** The stair runs were five steps for a six-block
  rise, which leaves you one block short of the floor above with no way up from where you stand. Only
  the Tower of Arcane Stone was climbable. Runs are now six steps, in a spiral around the shaft, with
  the facing matching the direction of travel.
- **The Frost Tower had no stairs at all.** Its `STAIR` constant held solid blocks, so what looked like
  a stair run was a ramp of full cubes. It now has real `prismarine_brick_stairs` over the existing
  packed-ice ramp: vanilla has no ice stair block in 1.21.1, and prismarine brick is the closest blue
  available, which keeps the frozen palette.
- **The Tower of Arcane Stone's stairs were three different materials** in the same flight. Unified to
  `stone_brick_stairs`.
- Two Frost Tower runs were moved out of spots that blocked them: one ended under a lectern, and the
  next landed in an alcove you could not walk to.

### Notes
- A new `audit_climb.py` check walks each tower from its door and reports `SE SUBE` / `NO SUBE` per
  floor transition. Counting stair blocks is not the same as being climbable, which is how three broken
  towers shipped in the first place.
- Roof and the y=53 landing stay unreachable on purpose: the shaft is sealed and the roof is not part
  of the route.
- Nothing verified in game. That check assumes a one-block step, so it proves reachability rather than
  that these exact stairs can be walked; the orientations were verified by hand. Climb it to be sure.

## [0.0.0-beta.6]

### Changed
- **All 60 structure pieces re-imported** from workshop beta.34: 15 per tower across `stone_tower`,
  `frost_tower`, `sandstone_tower` and `umbral_tower`.
- **The ground floor moved up 6 blocks** in every tower, with a solid rock base beneath it, and
  `start_height` changed from `absolute: -24` to `absolute: -30` to match. Together these keep the
  playable floor at the same world height while adding 6 blocks of base, so a tower on sloped ground
  has less of a gap under it.

### Notes
- **This mitigates the reported problem rather than solving it.** The towers carry
  `project_start_to_heightmap: WORLD_SURFACE_WG`, which is what makes a tower follow the terrain: it
  is seated at the surface height of a single column, so on a steep slope the rest of the footprint
  can still float or bury. The extra rock base absorbs that, up to 6 blocks of it. Nothing has been
  placed in a world yet.
- Verified from the delivered NBTs: door open at local (11, 30, 0) in three towers and an open
  threshold step at (11, 30, 1) in the fourth, floor at y=29 with the player's feet at y=30, and the
  shaft jigsaw still at (10, 0, 10).

## [0.0.0-beta.5]

### Changed
- **Taller part 3 — looks and decoration** (same pieces, pools and connectors): magical exteriors (stone: corner
  turrets with copper spires, amethyst-rune buttresses, octagonal parapet, banners; frost: thick buttresses, tall
  octagonal spire up to y 89, floating ice rings; sandstone: rune rings, copper observatory dome with telescope,
  gold spire; umbral: leaning tower with buttresses, broken crown and floating ring, crystal spire), fully
  decorated floors (library, alchemy lab, astronomer's study…) and irregular dungeon rooms (pilasters, collapsed
  corners, rubble, cobwebs, stalactites).
- Frost Tower: Exit Stone on a central dais; Umbral Tower: guardian hall with a 19×10×19 free volume for the
  Goliath, Exit Stone and vault moved to the walls.
- Requires Majestic Bestiary 0.0.0-beta.5.

## [0.0.0-beta.4]

### Changed
- **Towers keep their distance**: each tower has its own structure set with `expedition_core:isolated_spread`
  (spacing 40, separation 14, `isolation_chunks` 8) — never within 8 chunks of another surface structure
  (villages, outposts, ruined portals, other mods). Dev-server check: nearest village/outpost/portal 164-688 blocks.
- **Only in wide biomes**: structures use `expedition_core:wide_biome_jigsaw` with `biome_check_radius` 64 —
  the biome must cover the centre and 8 points 64 blocks around it.
- **Umbral Tower guardian** is now a Goliath (the Mountain Giant is no longer a guardian anywhere).
- Exit Stone emits enchantment, end-rod and portal particles around it.
- Requires Expedition Core 0.0.0-beta.5 and Majestic Bestiary 0.0.0-beta.4.

## [0.0.0-beta.3]

### Fixed
- Crash opening the creative inventory (`IllegalArgumentException: The stack count must be 1`): the Exit Stone
  had no block item, so the Arcane Towers creative tab received an empty stack. The block item is now registered.

## [0.0.0-beta.2]

### Changed
- **Staged dungeons** (taller part 2): entry → stage 1 → stage 2 → stage 3 → end hall, with optional side rooms
  that end in dead ends. The end hall is now structurally guaranteed (taller simulator: 50/50 seeds per tower,
  0 overlaps) and dungeons are 9-12 pieces. Measured in a dev server with `/place structure`: **15-22 spawners
  per tower** including the tower floors (was 54-98).
- Old free-branching pieces (`room_a`, `room_b`, `corridor_a`, `corridor_b`) and the `dungeon_rooms` pool
  replaced by `s1..s3_corridor/room`, `link_1..3` (fallbacks), `side_room_a/b`.

## [0.0.0-beta.1]

### Added
- **Exit Stone** (`arcane_towers:exit_stone`): two-block-tall block; using it unseals the tower it belongs to and
  teleports the player to the tower entrance (placeholder model until the taller delivers).
- **Four tower structures** (data): `stone_tower`, `frost_tower`, `sandstone_tower`, `umbral_tower` — jigsaw
  structures, template pools (tower → dungeon entry → rooms → end hall, `dead_ends` fallback), shared structure
  set (spacing 48 / separation 16) and biome tags. 32 NBT pieces from the taller (beta.29-34), verified in a
  dev server with `/place structure` (all four generate with their Exit Stone).
- Exit Stone final model/textures from the taller and real per-facing collision shapes.
- **Safety net**: a tower whose dungeon generated without its end hall (jigsaw may skip it ~5-10% of the time)
  is unsealed automatically when a player enters it, so nobody gets trapped below.

### Known issues
- Dungeons are large (25-51 pieces, many spawners) and the end hall is not always generated; a bounded,
  staged layout is being made by the taller (`docs/ARCANE_TOWERS_TALLER_GUIDE_PART2.md`).
- **Seal**: every tower is protected (expedition_core) until its Exit Stone is used.
- **Guardians**: spawned once per tower when a player enters the end hall (tauren / goliath / centaur / giant,
  +50% max health, named).
- Loot tables for floors and vaults; `en_us` (datagen) + `es_es`; "Arcane Towers" creative tab.

## [0.0.0]

### Added
- Project skeleton (NeoForge 21.1.249, moddev 2.0.142). Required dependencies: `expedition_core`
  0.0.0-beta.3, `majestic_bestiary` 0.0.0-beta.3, GeckoLib 4.7.6. Empty `@Mod` entry point.
- Design doc and taller work order for the four towers and the Exit Stone.
