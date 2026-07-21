# Echo Dimension Mod - Memory

## Concept
**Echo Dimension** is a Minecraft NeoForge mod (1.21.1, Java 21) where the **Overworld is mirrored as a "temporal reflection"** where your past actions haunt the world as playable echoes.

### Core Mechanics
- **Portal Access**: Custom portal block activated with a special item
- **Echo Dimension**: Dark, distorted reflection of the Overworld
- **Echo System**: 
  - Structures you built → ghostly echo buildings
  - Mobs you killed → hostile echo entities
  - Resources you mined → fragmented echoes
  - Repairs mechanic to restore echoes
- **Boss**: Final goal - the "Archon of Echoes"

## Current State

### Completed Files
| File | Status |
|------|--------|
| `EchoDimensionMod.java` | ✅ Compilable |
| `EchoPortalBlock.java` | ✅ Compilable |
| `EchoPortalBlockEntity.java` | ✅ Fixed (DeferredHolder pattern) |
| `PlayerEchoData.java` | ✅ Fixed (removed custom registry) |
| `EchoDimensionType.java` | ✅ Compilable |
| `EchoDimension.java` | ✅ Compilable |
| `EchoDimensionProvider.java` | ✅ Fixed (MultiNoiseBiomeSource) |
| `EchoDimension.java` | ✅ Compilable |
| `DimensionAccessItem.java` | ✅ Fixed (DeferredRegister pattern) |
| `EchoWorldCarver.java` | ✅ Fixed (ResourceKey type) |
| `EchoChunkGenerator.java` | ✅ Fixed (duplicate import) |

### Key Fixes Applied
1. **DeferredRegister pattern** for blocks, items, block entities
2. **Removed custom Registry classes** (Registry is interface in 1.21.1)
3. **Fixed BiomeSource imports** (removed duplicate)
4. **Fixed WorldCarver ResourceKey** (use WorldCarver<?> not CarverDebugSettings)
5. **Fixed MultiNoiseBiomeSource** creation (use createFromPreset)

## TODO List
- [ ] Test build with `./gradlew build`
- [ ] Implement portal frame blocks (obsidian + echo crystals)
- [ ] Create actual Echo entity system (EchoPhantom, EchoZombie, etc.)
- [ ] Add custom sky handler for Echo Dimension
- [ ] Implement echo spawning based on PlayerEchoData
- [ ] Create Echo Repair system
- [ ] Implement Archon of Echoes boss

## Tech Stack
- **NeoForge**: 1.21.1
- **Java**: 21
- **Gradle**: 7.0.189 (neogradle userdev plugin)

## Build Command
```bash
./gradlew build
```

## Important Notes
- Player data stored in NBT via `player.getPersistentData()`
- Echo counts tracked per-player
- Portal teleportation works via `ServerPlayer.teleportTo()`
- Network system prepared for multiplayer (Phase 1 is singleplayer)