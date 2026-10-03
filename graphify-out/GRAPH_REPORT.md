# Graph Report - 1.21.1  (2026-10-03)

## Corpus Check
- 97 files · ~115,519 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 238 nodes · 378 edges · 25 communities (24 shown, 1 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 3 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `ccddeaa8`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Guardian Events
- Mod Blocks
- Project Info
- Block Properties
- Guardian Data
- Data Generators
- Exit Stone
- Arcane Towers
- Block Interaction
- Tower Location
- Build Tools
- Player Interactions
- Block Placement
- Arcane Towers — taller work order, part 3: looks and decoration (playtest 2026-09-30)
- README.md

## God Nodes (most connected - your core abstractions)
1. `ExitStoneBlock` - 27 edges
2. `Flujo de trabajo — Arcane Towers (NeoForge)` - 13 edges
3. `GuardianSpawnEvents` - 11 edges
4. `CurseForge — Variables del proyecto` - 10 edges
5. `ModBlocks` - 9 edges
6. `Changelog` - 9 edges
7. `GuardianData` - 8 edges
8. `Located` - 7 edges
9. `ArcaneTowers` - 6 edges
10. `ModCreativeTabs` - 6 edges

## Surprising Connections (you probably didn't know these)
- `ModBlocks` --references--> `ExitStoneBlock`  [EXTRACTED]
  src/main/java/com/skd/arcanetowers/block/ModBlocks.java → src/main/java/com/skd/arcanetowers/block/ExitStoneBlock.java

## Import Cycles
- None detected.

## Communities (25 total, 1 thin omitted)

### Community 0 - "Guardian Events"
Cohesion: 0.20
Nodes (13): EntityType, Mob, Post, GuardianSpawnEvents, BlockPos, BoundingBox, Player, ResourceKey (+5 more)

### Community 1 - "Mod Blocks"
Cohesion: 0.16
Nodes (13): BlockItem, Blocks, CreativeModeTab, DeferredBlock, DeferredHolder, DeferredItem, DeferredRegister, Items (+5 more)

### Community 2 - "Project Info"
Cohesion: 0.14
Nodes (14): api_tokens, branch, client_server_env, dependencies, exit_stone_runes_texture, exit_stone_texture, game_versions_ids, icon (+6 more)

### Community 3 - "Block Properties"
Cohesion: 0.11
Nodes (30): Block, BlockGetter, BlockHitResult, BlockPlaceContext, BlockState, Builder, CollisionContext, Direction (+22 more)

### Community 4 - "Guardian Data"
Cohesion: 0.22
Nodes (6): CompoundTag, Factory, Provider, SavedData, GuardianData, Override

### Community 5 - "Data Generators"
Cohesion: 0.23
Nodes (8): EventBusSubscriber, GatherDataEvent, LanguageProvider, PackOutput, ArcaneLang, DataGenerators, Override, SubscribeEvent

### Community 6 - "Exit Stone"
Cohesion: 0.07
Nodes (26): A1. Tower piece `tower.nbt` (one per id), A2. Dungeon pieces (one set per id, underground), A3. Validation (hard requirements), Arcane Towers — taller work order (2026-09-29), Done means, Task A — Four tower dungeons (`structures/arcane_towers/<id>`), Task B — Exit Stone block (`blocks/arcane_towers`), 1. Concepto (+18 more)

### Community 7 - "Arcane Towers"
Cohesion: 0.32
Nodes (8): Logger, Mod, ModContainer, ResourceLocation, ArcaneTowers, IEventBus, ResourceKey, Structure

### Community 8 - "Block Interaction"
Cohesion: 0.10
Nodes (20): [0.0.0], [0.0.0-beta.1], [0.0.0-beta.2], [0.0.0-beta.3], [0.0.0-beta.4], [0.0.0-beta.5], [0.0.0-beta.6], [0.0.0-beta.7] (+12 more)

### Community 9 - "Tower Location"
Cohesion: 0.17
Nodes (11): CurseForge — Variables del proyecto, Descripción del proyecto y logo, Entorno "Client & Server", Flujo completo (primera vez), IDs de `gameVersions` para 1.21.1 (verificados, mismos que el resto de mods 1.21.1 del workspace), Proyecto, Rama, Relaciones (dependencias declaradas en CurseForge) (+3 more)

### Community 10 - "Build Tools"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

### Community 11 - "Player Interactions"
Cohesion: 0.40
Nodes (4): CLAUDE.md — arcane_towers (1.21.1), Prioridad de instrucciones, Recordatorios específicos, Workflow del mod

### Community 12 - "Block Placement"
Cohesion: 0.40
Nodes (4): Arcane Towers — taller work order, part 2: bounded dungeons with a guaranteed end hall (2026-09-29), Findings from the integration, Task — staged dungeon layout (all four towers), Validation (hard requirements)

### Community 15 - "Arcane Towers — taller work order, part 3: looks and decoration (playtest 2026-09-30)"
Cohesion: 0.40
Nodes (4): Arcane Towers — taller work order, part 3: looks and decoration (playtest 2026-09-30), Done means, Global (all four towers), Per tower

## Knowledge Gaps
- **52 isolated node(s):** `Workflow del mod`, `Recordatorios específicos`, `Prioridad de instrucciones`, `Fixed`, `Notes` (+47 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **1 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ExitStoneBlock` connect `Block Properties` to `Mod Blocks`?**
  _High betweenness centrality (0.046) - this node is a cross-community bridge._
- **Why does `GuardianSpawnEvents` connect `Guardian Events` to `Arcane Towers`?**
  _High betweenness centrality (0.038) - this node is a cross-community bridge._
- **Why does `ModBlocks` connect `Mod Blocks` to `Block Properties`?**
  _High betweenness centrality (0.025) - this node is a cross-community bridge._
- **What connects `Workflow del mod`, `Recordatorios específicos`, `Prioridad de instrucciones` to the rest of the system?**
  _52 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Project Info` be split into smaller, more focused modules?**
  _Cohesion score 0.14285714285714285 - nodes in this community are weakly interconnected._
- **Should `Block Properties` be split into smaller, more focused modules?**
  _Cohesion score 0.10588235294117647 - nodes in this community are weakly interconnected._
- **Should `Exit Stone` be split into smaller, more focused modules?**
  _Cohesion score 0.06896551724137931 - nodes in this community are weakly interconnected._