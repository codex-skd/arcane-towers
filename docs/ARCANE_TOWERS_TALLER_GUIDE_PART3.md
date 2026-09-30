# Arcane Towers — taller work order, part 3: looks and decoration (playtest 2026-09-30)

> Playtest of beta.2/beta.3. The staged dungeon structure works; this pass is about **how they look**.
> Same delivery rules (generate/validate in the taller, we pull). **Keep every contract unchanged**: piece names,
> pools, jigsaw names/targets, `tower` ground floor at y = 23/24, door on −Z, shaft position and opening,
> `dungeon_entry` pool landing, `dungeon_end` size and **room centre**, spawner/chest counts per piece
> (±1 is fine). Re-run `jigsaw_layout.py` (50 seeds, same thresholds) and the walk checks.

## Global (all four towers)
1. **Exteriors: magical towers.** Today they read as plain stone tubes. Give each a striking silhouette: buttresses,
   floating/overhanging rings, spires or domes, glowing runes/crystals (amethyst, sea lanterns, end rods, soul
   lanterns), banners, balconies, broken arches. Recognisable from 100+ blocks.
2. **Interiors decorated, not just walls**: furniture and dressing on every floor (bookshelves, lecterns,
   cauldrons, brewing stands, tables of slabs/stairs, carpets, chains, candles, barrels, armour stands, rubble,
   cobwebs, vines), themed per tower.
3. **Irregular rooms** in the dungeon: break the rectangular boxes — uneven walls, pillars, alcoves, collapsed
   corners, height changes (steps, pits with a way out), rubble piles. Keep the doors (jigsaws) where they are.
4. Keep spawners readable and reachable (not buried in decoration), chests reachable.

## Per tower

| Tower | Feedback | Ask |
|---|---|---|
| `frost_tower` | Exit Stone looks glued to the wall; hall too regular and bare | Move the **Exit Stone** to a dais **in the room**, at least 3 blocks from any wall, framed (ice pillars, frozen braziers). Irregular icy hall: ice columns, frozen waterfalls, snow drifts. Taller spire top (the low pyramid was already noted). |
| `sandstone_tower` | Interior poorly decorated; underground is fine | Decorate all tower floors (desert library/observatory theme: terracotta patterns, gold, jars/pots, hanging lanterns, sand piles). |
| `stone_tower` | Some rooms should be irregular; exterior and everything much more decorated | Irregular rooms + full decoration pass inside and out (copper dome observatory top, amethyst lights, arcane study rooms). |
| `umbral_tower` | Good overall | Its guardian becomes a **goliath** (code side); make sure the hall fits a 2.5× creature (≥ 19×10×19). Light decoration pass only. |

## Done means
- All pieces `VALIDATION OK`, `jigsaw_layout.py` 50/50 end hall and Exit Stone reachable, walk checks OK.
- Previews (front/back/top/cutaway per tower + dungeon rooms) and an updated delivery note, including the **new
  Exit Stone offset** in `frost_tower/dungeon_end`.
