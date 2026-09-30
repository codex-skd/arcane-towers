# Graph Report - .  (2026-09-30)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 149 nodes · 299 edges · 15 communities (13 shown, 2 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 3 edges (avg confidence: 0.8)
- Token cost: 610 input · 137 output

## Graph Freshness
- Built from commit: `8d935f6d`
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

## God Nodes (most connected - your core abstractions)
1. `ExitStoneBlock` - 26 edges
2. `GuardianSpawnEvents` - 11 edges
3. `ModBlocks` - 9 edges
4. `GuardianData` - 8 edges
5. `Located` - 7 edges
6. `ArcaneTowers` - 6 edges
7. `ModCreativeTabs` - 6 edges
8. `Tower` - 5 edges
9. `TowerGuardian` - 5 edges
10. `DataGenerators` - 4 edges

## Surprising Connections (you probably didn't know these)
- `ModBlocks` --references--> `ExitStoneBlock`  [EXTRACTED]
  src/main/java/com/skd/arcanetowers/block/ModBlocks.java → src/main/java/com/skd/arcanetowers/block/ExitStoneBlock.java

## Import Cycles
- None detected.

## Communities (15 total, 2 thin omitted)

### Community 0 - "Guardian Events"
Cohesion: 0.19
Nodes (14): EntityType, Mob, Post, ResourceLocation, GuardianSpawnEvents, BlockPos, BoundingBox, Player (+6 more)

### Community 1 - "Mod Blocks"
Cohesion: 0.17
Nodes (12): BlockItem, Blocks, CreativeModeTab, DeferredBlock, DeferredHolder, DeferredItem, DeferredRegister, Items (+4 more)

### Community 2 - "Project Info"
Cohesion: 0.14
Nodes (14): api_tokens, branch, client_server_env, dependencies, exit_stone_runes_texture, exit_stone_texture, game_versions_ids, icon (+6 more)

### Community 3 - "Block Properties"
Cohesion: 0.20
Nodes (9): Block, BlockGetter, BlockState, Builder, CollisionContext, ItemStack, LevelReader, LivingEntity (+1 more)

### Community 4 - "Guardian Data"
Cohesion: 0.22
Nodes (6): CompoundTag, Factory, Provider, SavedData, GuardianData, Override

### Community 5 - "Data Generators"
Cohesion: 0.23
Nodes (8): EventBusSubscriber, GatherDataEvent, LanguageProvider, PackOutput, ArcaneLang, DataGenerators, Override, SubscribeEvent

### Community 6 - "Exit Stone"
Cohesion: 0.27
Nodes (6): Direction, DoubleBlockHalf, EnumProperty, LevelAccessor, ExitStoneBlock, VoxelShape

### Community 7 - "Arcane Towers"
Cohesion: 0.36
Nodes (7): Logger, Mod, ModContainer, ArcaneTowers, IEventBus, ResourceKey, Structure

### Community 8 - "Block Interaction"
Cohesion: 0.36
Nodes (5): BlockHitResult, InteractionResult, ServerPlayer, BlockPos, ServerLevel

### Community 9 - "Tower Location"
Cohesion: 0.42
Nodes (6): BoundingBox, ResourceKey, Structure, StructureStart, Located, Tower

### Community 10 - "Build Tools"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

## Knowledge Gaps
- **2 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ExitStoneBlock` connect `Exit Stone` to `Mod Blocks`, `Block Properties`, `Block Interaction`, `Tower Location`, `Player Interactions`, `Block Placement`?**
  _High betweenness centrality (0.112) - this node is a cross-community bridge._
- **Why does `ModBlocks` connect `Mod Blocks` to `Exit Stone`?**
  _High betweenness centrality (0.063) - this node is a cross-community bridge._
- **Should `Project Info` be split into smaller, more focused modules?**
  _Cohesion score 0.14285714285714285 - nodes in this community are weakly interconnected._