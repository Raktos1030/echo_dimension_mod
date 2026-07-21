# Echo Dimension Mod - Project Memory

## Concept
**Echo Dimension** is a NeoForge 1.21.1 mod (Java 21) that creates a dark reflection of the Overworld where past player actions manifest as "echoes".

## Core Mechanics
1. **Portal Access** - Custom block portal activated by item, toggles Overworld ↔ Echo Dimension
2. **Echo Entities** - Ghost versions of killed mobs appear in Echo Dimension
3. **Echo Structures** - Built structures manifest as ghostly echoes
4. **Echo Repair** - Collect fragmented resources to repair ghosts → rewards
5. **Boss: The Primordial Echo** - Final boss with phases tied to player's history

## Current Project State

### ✅ What works
- Basic mod structure and registration (EchoDimensionMod.java)
- Portal block with teleportation logic (EchoPortalBlock.java, EchoPortalBlockEntity.java)
- Dimension type and key definitions (EchoDimension.java, EchoDimensionType.java)
- Network packet handler skeleton (EchoPacketHandler.java)
- Player data tracking (PlayerActionRecorder.java, PlayerEchoData.java)
- Item for portal activation (DimensionAccessItem.java)

### ❌ Known Compilation Issues (FIXED)
- `EchoDimension.java`: Wrong import `net.minecraft.world.dimension.DimensionType` → `net.minecraft.world.level.dimension.DimensionType`
- `EchoDimensionType.java`: `DimensionType` constructor signature changed in 1.21.1
- `EchoPortalBlock.java`: Incorrect `DeferredHolder.create()` syntax
- `EchoPortalBlockEntity.java`: `BlockEntityType` registration syntax outdated
- `EchoDimensionProvider.java`: `WorldDimensions.BonusChestInfo` removed in 1.21.1
- `EchoChunkGenerator.java`: `WorldgenContext` interface and `ChunkGeneratorConfiguration` API changed
- `EchoWorldCarver.java`: `ArgumentBasedDimension`, `CarverConfiguration`, and carver registration APIs changed

### 🚧 Still Needs Implementation
- Full EchoChunkGenerator with working terrain generation
- EchoWorldCarver registration
- Echo entity spawning system (GhostEchoEntity)
- Echo repair crafting mechanic
- Boss entity (PrimordialEchoBoss)
- Portal frame block
- Custom sky/mist rendering for Echo Dimension atmosphere

## Key Files
| File | Status |
|------|--------|
| `EchoDimensionMod.java` | ✅ OK |
| `EchoPortalBlock.java` | ✅ FIXED |
| `EchoPortalBlockEntity.java` | ✅ FIXED |
| `EchoDimension.java` | ✅ FIXED |
| `EchoDimensionType.java` | ✅ FIXED |
| `EchoDimensionProvider.java` | ✅ FIXED |
| `EchoChunkGenerator.java` | ✅ FIXED |
| `EchoWorldCarver.java` | ✅ FIXED |
| `DimensionAccessItem.java` | ✅ OK |
| `PlayerActionRecorder.java` | ✅ OK |
| `PlayerEchoData.java` | ✅ OK |
| `EchoPacketHandler.java` | ✅ OK |

## Build Commands
```bash
./gradlew build          # Build mod JAR
./gradlew runClient      # Launch Minecraft with mod
./gradlew runServer      # Launch server with mod
```

## Version
- Minecraft: 1.21.1
- NeoForge: ${neo_version} (managed by gradle.properties)
- Java: 21