# Changelog

All notable changes to this project will be documented in this file.

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
