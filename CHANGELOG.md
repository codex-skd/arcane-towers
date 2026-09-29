# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased — M1]

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
- **Seal**: every tower is protected (expedition_core) until its Exit Stone is used.
- **Guardians**: spawned once per tower when a player enters the end hall (tauren / goliath / centaur / giant,
  +50% max health, named).
- Loot tables for floors and vaults; `en_us` (datagen) + `es_es`; "Arcane Towers" creative tab.

## [0.0.0]

### Added
- Project skeleton (NeoForge 21.1.249, moddev 2.0.142). Required dependencies: `expedition_core`
  0.0.0-beta.3, `majestic_bestiary` 0.0.0-beta.3, GeckoLib 4.7.6. Empty `@Mod` entry point.
- Design doc and taller work order for the four towers and the Exit Stone.
