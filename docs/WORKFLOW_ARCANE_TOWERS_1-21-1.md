# Flujo de trabajo — Arcane Towers (NeoForge)

> **Versión del workflow**: 1.21.0 (codex-docs)
> Este archivo pertenece al proyecto **Arcane Towers** (librería de criaturas del ecosistema Majestic). Cambios aquí solo afectan a este proyecto.
> **Trabaja directamente con este archivo**: es el workflow operativo del mod, autocontenido. No leas `codex-docs/WORKFLOW_AGENT.md` ni `WORKFLOW_GENERIC.md` de forma rutinaria.
> On-demand (solo si la tarea lo necesita): `codex-docs/reference/CURSEFORGE.md` (formato HTML al publicar), `codex-docs/reference/GRAPHIFY.md` (backend LLM de Graphify), `codex-docs/reference/REPO_SETUP.md` (setup único de repo), `codex-docs/reference/WIKI.md` (wiki pública del mod — solo si hay que crearla o cambiarla).
> Diseño: [`DESIGN_ECOSYSTEM.md`](DESIGN_ECOSYSTEM.md) (visión cruzada + decisiones §9), [`DESIGN_MAJESTIC_1-21-1.md`](DESIGN_MAJESTIC_1-21-1.md), [`PROGRESSION.md`](PROGRESSION.md), [`CONTENT_MAGIC.md`](CONTENT_MAGIC.md), [`CONTENT_WORLD.md`](CONTENT_WORLD.md), [`INTEGRATIONS.md`](INTEGRATIONS.md), [`LORE.md`](LORE.md).

## Específico del mod

| Campo | Valor |
|---|---|
| **Mod ID** | `arcane_towers` |
| **Clase principal** | `com.skd.arcanetowers.ArcaneTowers` |
| **Display name** | `Arcane Towers` |
| **Versión Minecraft** | `1.21.1` |
| **Versión NeoForge** | `21.1.249` |
| **Rama de trabajo** | `minecraft/1.21.1/neoforge-21.1.249/production` (única) |
| **Repositorio GitLab** | `https://gitlab.com/stalking-dragons/minecraft/arcane-towers.git` — **privado** (pendiente de crear en la web) |
| **Paquete base** | `com.skd.arcanetowers` |
| **Licencia** | **All Rights Reserved** |
| **CurseForge project id** | *(pendiente de registrar — `mod_curseforge_project_id` en `gradle.properties`)* |

## Qué es este mod

**Mod de contenido**: cuatro torres arcanas (mazmorras) del ecosistema Majestic, una por familia de biomas.
Se sube cada torre planta a planta por escaleras alrededor de una **columna central 4×4 cerrada y vacía**; desde la
última planta (no la azotea) el jugador se tira por la columna hasta la **mazmorra subterránea**, la recorre, vence
a su guardián y usa la **Piedra de salida** (bloque de 2 de alto). Hasta usarla, la torre y su mazmorra **no se
pueden picar ni construir** (protección de `expedition_core`). Criaturas de `majestic_bestiary`.

- **No depende de majestic** (decisión 2026-09-29): se puede jugar solo con sus librerías.
- Diseño: [`DESIGN_ARCANE_TOWERS_1-21-1.md`](DESIGN_ARCANE_TOWERS_1-21-1.md). Encargo al taller:
  [`ARCANE_TOWERS_TALLER_GUIDE.md`](ARCANE_TOWERS_TALLER_GUIDE.md).

Dependencias (todas **externas, nunca jar-in-jar**): `expedition_core`, `majestic_bestiary`, `geckolib` = required.
En desarrollo las dos librerías SKD se consumen como `compileOnly files("libs/<jar>")`.

## Convenciones de nomenclatura

| Convención | Uso | Ejemplo |
|---|---|---|
| **snake_case** | `mod_id` en gradle.properties, assets/, data/ | `arcane_towers` |
| **PascalCase** | Clases Java principales | `ArcaneTowers` |
| **camelCase** | Variables, métodos, config keys | `spawnWeight` |
| **Title Case** | Display name en README, CHANGELOG, docs, CurseForge | `Arcane Towers` |

- `mod_id` en `gradle.properties` coincide con el nombre del directorio del proyecto (`arcane_towers`).
- Clase principal en **PascalCase** del `mod_id`: `arcane_towers` → `ArcaneTowers`.
- Paquete base `com.skd.arcanetowers` (sin guion bajo, como el resto de mods SKD).
- Namespace de recursos: `arcane_towers:` (assets y data).

## Organización y ramas

| Rama | Propósito |
|---|---|
| `minecraft/1.21.1/neoforge-21.1.249/production` | **Rama única.** Repo privado, todo el trabajo aquí. |

- **No hay rama `main` ni espejo público** (a diferencia de las 3 librerías, que sí lo tienen).
- **No hay `.gitlab-ci.yml` de mirror.** Se puede añadir un CI de build/test más adelante; no publica nada.
- Localmente `arcane_towers/neoforge/1.21.1/` es un clon independiente con su propio `.git/`, en `production`.

## Estructura del proyecto

```
arcane_towers/neoforge/1.21.1/
├── build.gradle · gradle.properties · settings.gradle
├── src/main/java/com/skd/arcanetowers/
│   ├── ArcaneTowers.java                   # @Mod
│   ├── registry/                           # bloques/ítems (Piedra de salida)
│   ├── block/                              # ExitStoneBlock
│   ├── event/                              # protección, guardianes, salida
│   └── datagen/                            # loot, tags, lang en_us
├── src/main/resources/
│   ├── data/arcane_towers/{structure,worldgen/{structure,structure_set,template_pool}}/
│   └── assets/arcane_towers/{blockstates,models,textures,lang/es_es.json}
├── libs/                                   # jars de expedition_core y majestic_bestiary. Versionado.
├── docs/                                   # DESIGN, WORKFLOW, guía del taller, curseforge/
└── CHANGELOG.md · README.md · LICENSE (ARR)
```

## Versionado

| Estado | Formato | Ejemplos |
|---|---|---|
| Beta / desarrollo | `0.0.0-beta.X` | `0.0.0-beta.1` |
| Alpha jugable | `0.X.0-alpha` | `0.1.0-alpha` |
| Release estable | `X.Y.Z` (SemVer) | `1.0.0` |

- Incrementar en cada commit funcional y al preparar subida a CurseForge; se define en `gradle.properties` (`mod_version`).
- JAR: `arcane_towers-1.21.1-neoforge-21.1.249-<mod_version>.jar` (`base.archivesName`).

## Commits (Conventional Commits)

`<tipo>[<ámbito>]: <descripción>` + body con `v<version>`. Tipos: `feat` · `fix` · `refactor` · `docs` · `chore` · `style` · `perf` · `test`.

```
git commit -m "feat[magic]: Starlight school — 4 spells via datagen

v0.1.0-alpha"
```

Cerrar los mensajes de commit con:
`Co-Authored-By: Claude Sonnet 5 <noreply@anthropic.com>`

## Sin tags

**No se crean tags git** (ni en GitLab ni en ningún remoto): apuntan a commits de `production` y el mirror los publicaría con el contenido privado. El commit exacto de cada JAR es su `chore: bump version to <version>`.

## Flujo por tarea

**0. Alcance** — Este mod solo tiene una versión (1.21.1) y un framework (NeoForge). No requiere selector.

**1. Desarrollo**

```bash
git checkout minecraft/1.21.1/neoforge-21.1.249/production
# refrescar libs/ con el jar construido de expedition_core si cambió
./gradlew.bat runData    # regenerar el contenido datagen
./gradlew.bat build
git add -A
git commit -m "feat[world]: Celestial Plane dimension + 3 biomes

v0.1.0-alpha"
git push
```

**2. Preparar versión para CurseForge** — solo si el usuario confirma:

```bash
# bump en gradle.properties: mod_version=0.1.0-alpha
./gradlew.bat clean runData build
# release notes: docs/curseforge/versions/0.1.0-alpha.md + CHANGELOG.md
git commit -m "chore: bump version to 0.1.0-alpha"
# Subir JAR solo si el usuario confirma:
# powershell -File ../../../codex-docs/scripts/curseforge-upload.ps1
```

CurseForge: proyecto **público, descarga libre**, `mod_license = All Rights Reserved`. Declarar
`expedition_core` y `geckolib` como **required dependencies**.

**3. Release estable** — `mod_version=1.0.0` + commit `chore: bump version to X.Y.Z` (sin tag).
Los mods del ecosistema (`astral_core`, `expedition_core`, `almanac_core`, `arcane_towers`, `majestic`) suben a
`1.0.0` **a la vez** (ver roadmap `DESIGN_ECOSYSTEM.md §8 Fase 4`).

**4. Actualizar Knowledge Graph (Graphify)** — tras cada push a remoto:

```bash
GRAPHIFY="C:\Users\llagu\AppData\Local\Packages\PythonSoftwareFoundation.Python.3.13_qbz5n2kfra8p0\LocalCache\local-packages\Python313\Scripts\graphify.exe"
"$GRAPHIFY" extract .              # 1ª vez (si graphify-out/ NO existe)
"$GRAPHIFY" update . --force       # actualización tras cambios de código
git add graphify-out/ && git commit -m "chore: update knowledge graph" && git push
```

Leer solo `GRAPH_REPORT.md`, nunca `graph.json`/`graph.html`. Sin copias fechadas.

## Buenas prácticas

- **Un commit por cambio lógico** · commit+push tras cada cambio funcional y de documentación.
- **`clean build` siempre antes del JAR final**. `runData` antes de `build` si cambió el contenido.
- **Versionar antes de subir a CurseForge** · **CHANGELOG.md siempre actualizado**.
- **Nunca actualizar** NeoForge, MC ni dependencias sin petición explícita.
- **Nunca borrar archivos** sin petición explícita.
- **Licencia ARR**: repo privado, sin espejo `main`, sin bundling de dependencias. No relicenciar.
- **Datagen** para loot/tags/modelos de ítem/`en_us`. Estructuras NBT y modelo de la Piedra de salida vienen del taller.
- **`es_es` a mano** (contenido propio); otros idiomas después con el flujo habitual.
- `expedition_core`: `compileOnly files("libs/<jar>")` en dev, `required` en `neoforge.mods.toml`. GeckoLib `implementation` + `required`. Nunca jar-in-jar.
- README.md en inglés y actualizado.

## Idioma

| Ámbito | Idioma |
|---|---|
| Código fuente, logs, nombres técnicos, commits | **Inglés** (en-US) |
| README.md | **Inglés** (en-US) |
| Wiki pública (repos de wiki GitHub + GitLab, ver `codex-docs/reference/WIKI.md`) | **Inglés** (en-US) |
| Documentación interna (docs/, CHANGELOG, WORKFLOW) | **Castellano** (es-ES) |
| CurseForge (descripción, release notes) | **Inglés** (en-US) |
| Guías para `taller_minecraft` (`*_MODEL_GUIDE.md`, tandas nuevas de `TEXTURE_GUIDE.md`) | **Inglés** (en-US) — mejor comprensión en el taller |

**Guías para el taller**: el taller genera y valida todo en su propio repo (`taller_minecraft/.../output/`);
nunca se le pide copiar nada a este ni a otro proyecto. Nosotros traemos los ficheros desde ahí (`entities/<id>/output/1.21.1/assets/arcane_towers/`).

---

## Hitos de implementación (delegación OpenCode)

1. **M1 — Torres v1**: Piedra de salida, 4 estructuras jigsaw (torre + mazmorra), spawners/guardianes de
   `majestic_bestiary`, loot, protección hasta usar la piedra, salida a la superficie.
2. **M2 — Pulido**: variantes, sonidos/música propios, logros.

Claude compila y verifica cada hito (`./gradlew.bat runData build` + arranque `runServer`, BFS de paso sobre
las NBT del taller). Implementación delegada en OpenCode; diseño, docs, git y publicación los lleva Claude.
