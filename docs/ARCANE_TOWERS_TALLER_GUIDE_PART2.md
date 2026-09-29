# Arcane Towers — taller work order, part 2: bounded dungeons with a guaranteed end hall (2026-09-29)

> Follow-up to `ARCANE_TOWERS_TALLER_GUIDE.md` after integrating the delivery (`ENTREGA_ARCANE_TOWERS.md`) in the mod.
> Same delivery rules: generate and validate in `taller_minecraft` only; we pull.

## Findings from the integration

Measured with `tools/jigsaw_layout.py` (20 seeds, depth 7, distance 116) and in a dev server (`/place structure`):

1. **Dungeons are too big**: 25–51 pieces per dungeon → **54–98 spawners per tower** in game. Too long to clear and
   heavy for servers.
2. **The end hall is not guaranteed**: placed in 90–100% of seeds today. When it is missing there is no Exit Stone,
   and the tower is sealed → the player is trapped (the mod now unseals such towers as a safety net, but that is a
   patch, not the design).
3. Simply adding `dead_end` to the rooms pool shrinks the dungeon but drops end-hall placement to **20–65%** — not
   acceptable.

## Task — staged dungeon layout (all four towers)

Replace the free-branching pool with a **staged main path** so the end hall is structurally guaranteed:

- `dungeon_entry` → exactly **one** forward connector to pool `arcane_towers:<id>/stage_1`.
- `stage_1` pool (corridors/rooms) → each piece has **one forward** connector to `arcane_towers:<id>/stage_2`
  and **0–1 side** connectors to `arcane_towers:<id>/side` (optional side room, ends in `dead_end`; may hold a
  chest).
- `stage_2` pool → forward to `arcane_towers:<id>/stage_3`; side 0–1 as above.
- `stage_3` pool → forward to `arcane_towers:<id>/dungeon_end` (the hall).
- `side` pool → small rooms whose extra doors all go to `arcane_towers:<id>/dead_ends`.
- Fallbacks: every stage pool falls back to a pool containing a straight connector piece that keeps the main
  path going (never to `minecraft:empty`), so a blocked branch cannot cut the path to the hall.
- Target size: **8–14 pieces** per dungeon; **spawners: 1 per room/corridor, 0 in side rooms except 1 in
  every other one**, tower floors unchanged (6–8). Aim for **≤ 20 spawners per tower** in total.
- Keep all names/offsets of the `tower`, `dungeon_entry` shaft landing and `dungeon_end` hall exactly as delivered
  (the mod depends on them), and the `dead_end` piece.

## Validation (hard requirements)
- `jigsaw_layout.py`, 50 seeds per tower: **end hall placed 50/50**, Exit Stone reachable 50/50, no overlaps,
  pieces 8–14, spawners ≤ 20 (report min/avg/max).
- Update `pools.json` for each tower (we rebuild the mod's pools from it) and the delivery note.
