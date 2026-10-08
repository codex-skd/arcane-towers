# CLAUDE.md — arcane_towers (1.21.1)

Mod de **mazmorras (torres arcanas)** del ecosistema **Majestic** (mod NeoForge del grupo `stalking-dragons/minecraft`).
No depende de `majestic`. Construye sobre `expedition_core` + `majestic_bestiary` + `majestic_core` (decoración subterránea) (+ GeckoLib).

## Workflow del mod

1. **Trabaja con `docs/WORKFLOW_ARCANE_TOWERS_1-21-1.md`** — workflow operativo autocontenido. Léelo y síguelo.
2. Diseño: `docs/DESIGN_ARCANE_TOWERS_1-21-1.md`. Visión del ecosistema: `../../../majestic/neoforge/1.21.1/docs/DESIGN_ECOSYSTEM.md`.
3. Reglas generales (idioma, no asumir, no borrar, delegación OpenCode, prioridad): `../../../codex-docs/reference/CLAUDE.md`.
4. On-demand: `../../../codex-docs/reference/CURSEFORGE.md` (publicar), `../../../codex-docs/reference/GRAPHIFY.md`, `../../../codex-docs/reference/REPO_SETUP.md`, `../../../codex-docs/reference/WIKI.md` (wiki pública). No leerlos de forma rutinaria.

## Recordatorios específicos

- **Licencia: All Rights Reserved.** Repo **privado**, **solo rama `production`**, **sin espejo `main`**, sin `.gitlab-ci.yml` de mirror.
- **Sin bundling / jar-in-jar**. `expedition_core`, `majestic_bestiary`, `majestic_core` y `geckolib` = required.
- Criaturas: solo las de `majestic_bestiary` (spawners / guardianes).
- Implementación delegada en OpenCode; diseño, docs, git, Graphify y publicación los lleva Claude.

## Prioridad de instrucciones

1. Petición del usuario en esta sesión.
2. Este archivo + `codex-docs/reference/CLAUDE.md`.
3. Workflow del proyecto.
4. Convenciones existentes del proyecto.
