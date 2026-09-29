# Diseño — Arcane Towers (1.21.1)

> Documento técnico del mod de torres arcanas del ecosistema Majestic. Creado 2026-09-29.

## 1. Concepto

Cuatro torres-mazmorra en el overworld, una por familia de biomas. Recorrido fijo:

1. **Subir la torre** planta a planta por escaleras que rodean una **columna central** cerrada y vacía:
   hueco interior de 4×4 rodeado por muro (6×6 por fuera). Cada planta tiene criaturas (spawners) y botín.
2. En la **última planta** (no la azotea) la columna tiene una abertura: el jugador **se tira por el hueco**.
3. Cae por la columna, atraviesa el suelo y llega a la **mazmorra subterránea** (agua al fondo para no morir).
4. Recorre la mazmorra hasta la **sala final**: guardián + cámara del tesoro + **Piedra de salida**.
5. Usar la **Piedra de salida** desbloquea toda la estructura (torre + mazmorra) y saca al jugador a la
   superficie, frente a la puerta de la torre.

Hasta ese momento la estructura está **sellada**: no se puede picar, colocar bloques, ni entran fluidos o
explosiones (`expedition_core` `StructureProtection`, margen 1). El creativo lo ignora (config de expedition_core).

## 2. Las cuatro torres (decisión 2026-09-29: por bioma)

| id | Nombre | Biomas (tag `arcane_towers:has_structure/<id>`) | Paleta | Plantas (spawners) | Mazmorra | Guardián |
|---|---|---|---|---|---|---|
| `stone_tower` | Torre de Piedra Arcana | llanuras, bosques, praderas | piedra, andesita, toba, cobre oxidado | goblin, gnomo, elfo | no muerto | **tauren** |
| `frost_tower` | Torre de Escarcha | nevados, montaña, arboleda | ladrillo de piedra, hielo compacto, calcita, diorita | lobo, sátiro | no muerto | **goliath** |
| `sandstone_tower` | Torre de Arenisca | desierto, badlands, sabana | arenisca cortada/cincelada, terracota, oro | goblin, mago arcano | no muerto | **centauro** |
| `umbral_tower` | Torre Umbría | bosque oscuro, pantano, manglar | pizarra abismal, basalto, madera oscura, linternas de alma | no muerto, sátiro, mago arcano | no muerto | **gigante** |

Todas las criaturas son de `majestic_bestiary` (`majestic_bestiary:<id>`).

## 3. Contrato de estructura (NBT del taller ↔ datos del mod)

- Estructura jigsaw por torre: `data/arcane_towers/worldgen/structure/<id>.json`, `start_pool`
  `arcane_towers:<id>/start`, `project_start_to_heightmap: WORLD_SURFACE_WG`,
  **`start_height: {"absolute": -24}`** (la pieza torre lleva 24 bloques de columna/sótano bajo su planta baja),
  `size: 7`, `max_distance_from_center: 116`, `terrain_adaptation: beard_thin`, `step: surface_structures`.
- Piezas (plantillas `data/arcane_towers/structure/<id>/<pieza>.nbt`):
  - `tower` — torre completa + tramo de columna bajo tierra. Planta baja en **y = 24** de la NBT. Puerta en la
    cara **−Z (norte)**. Jigsaw en el fondo de la columna: nombre `arcane_towers:shaft_bottom`, target
    `arcane_towers:dungeon_entry`, pool `arcane_towers:<id>/dungeon_entry`, orientación hacia abajo.
  - `dungeon_entry` — sala donde se cae (poza de agua 2 de hondo bajo el hueco), con 1-2 jigsaws
    `arcane_towers:corridor` hacia pool `arcane_towers:<id>/dungeon_rooms`.
  - `dungeon_rooms` pool — 3-4 salas/pasillos (`room_a`, `room_b`, `corridor_a`, …), alguno con jigsaw hacia
    `arcane_towers:<id>/dungeon_end`.
  - `dungeon_end` — sala final: espacio abierto para el guardián (≥ 15×8×15), cofre de tesoro y la
    **Piedra de salida** (`arcane_towers:exit_stone`, 2 de alto) en su posición documentada.
- Pools en `data/arcane_towers/worldgen/template_pool/<id>/{start,dungeon_entry,dungeon_rooms,dungeon_end}.json`
  (fallback `minecraft:empty`), proyección `rigid`.
- `structure_set` único `arcane_towers:towers`: las 4 estructuras con el mismo peso, `random_spread`
  spacing 48 / separation 16 (cada una solo cuaja en sus biomas).
- Spawners: bloques `minecraft:spawner` en las NBT con `SpawnData.entity.id` = `majestic_bestiary:<id>`.
- Guardián: **no** es spawner; el mod lo genera **una vez** por estructura cuando un jugador entra en la pieza
  `dungeon_end` (SavedData), en el centro de la sala.
- Cofres: loot tables `arcane_towers:chests/<id>_floor` (plantas) y `arcane_towers:chests/<id>_vault` (tesoro).

## 4. Piedra de salida (`arcane_towers:exit_stone`)

- Bloque de **2 de alto** (mitades `lower`/`upper`, como una puerta), roca con formas puntiagudas en los
  extremos, apoyada sobre una de sus puntas. Modelo y colisión del taller.
- Irrompible mientras la estructura esté sellada (lo cubre la protección); `destroyTime` alto igualmente.
- Clic derecho: desbloquea la estructura en la que está, teletransporta al jugador frente a la puerta de la torre
  (centro de la cara −Z de la pieza `tower`, 2 bloques fuera, a la altura de superficie) con partículas y
  sonido. Mensaje en action bar. Se puede usar más veces (siempre saca al jugador).

## 5. Historial

- 2026-09-29: creado el repo (esqueleto MDK, deps expedition_core beta.3 + majestic_bestiary beta.3 + GeckoLib).
  Encargo al taller: `ARCANE_TOWERS_TALLER_GUIDE.md`.
