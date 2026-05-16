# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Code generation policy

seed4j scaffolds the project skeleton (build config, framework wiring, technical modules). It does **not** generate business code. Split work accordingly:

- **Scaffolding, build files, technical modules, framework integrations** → must go through the seed4j MCP server. Do not hand-write these.
- **Business code** (domain logic, use cases, feature-specific adapters) → written by the agent or by hand. When the agent writes it, it **must follow the hexagonal architecture conventions established by seed4j** in this repo (see below).

Available seed4j MCP tools:
- `mcp__seed4j__create_project` — scaffold a new project
- `mcp__seed4j__list_presets` / `mcp__seed4j__get_preset_details` / `mcp__seed4j__apply_preset` — discover and apply curated module bundles
- `mcp__seed4j__list_modules` / `mcp__seed4j__search_modules` / `mcp__seed4j__get_module_details` / `mcp__seed4j__get_module_dependencies` — discover individual modules
- `mcp__seed4j__apply_module` / `mcp__seed4j__apply_modules` — apply one or many modules to the project
- `mcp__seed4j__validate_properties` — validate module properties before applying
- `mcp__seed4j__get_project_status` — inspect what has already been applied

Recommended flow for a new request: `get_project_status` → discover (`list_*` / `search_modules` / `get_module_details`) → `validate_properties` → `apply_module(s)`.

## Hexagonal architecture for business code

When writing business code by hand or with the agent, follow the hexagonal layout that seed4j generates. Before writing any new business code:

1. Inspect the existing tree to see how seed4j has organized layers (typically `domain` for pure business logic with no framework imports, `application` / use cases for orchestration, and `infrastructure` / `primary` / `secondary` adapters for inbound and outbound I/O).
2. Place each new file in the matching layer; keep the domain free of framework, persistence, and transport concerns.
3. Dependencies point inward only — domain knows nothing of application or infrastructure; infrastructure depends on domain via ports/interfaces defined in the domain.
4. Mirror the package/module naming, test layout, and style of the seed4j-generated code already present. If no example exists yet for a given concern, apply a seed4j module first so there is a template to follow.

## Working with module properties

When a seed4j module requires properties, **never invent values** (project names, package names, ports, database names, credentials, etc.). Ask the user for each required property before calling `apply_module` / `apply_modules`. Use `get_module_details` to see what a module needs, and `validate_properties` to check inputs before applying.

## Repository state

The working directory is currently empty except for `logs/seed4j-mcp.log` (output from the seed4j MCP server itself). There is no existing build system, test suite, or source tree — everything is expected to be generated via the tools above.
