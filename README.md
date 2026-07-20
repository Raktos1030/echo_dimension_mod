# Echo Dimension

A NeoForge mod for Minecraft 1.21.1 that adds a unique Echo Dimension where your past actions haunt the world as playable echoes.

## Concept

The **Echo Dimension** is a *temporal reflection* of the Overworld where the player's past actions "haunt" the world in the form of playable echoes.

## Features

### Phase 1 (Current)
- ✅ Custom Echo Portal block for dimension travel
- ✅ Echo Recorder item to track and display echo data
- ✅ Echo Dimension with dark purple/cyan aesthetic
- ✅ Basic player action recording (structures, kills, resources)
- ✅ Network packet infrastructure for multiplayer preparation
- ✅ Gradle build system (NeoForge 1.21.1)

### Phase 2 (Planned)
- [ ] Echo Mobs - hostile ghost versions of killed entities
- [ ] Echo Structures - ghostly buildings from player construction
- [ ] Echo Fragments - repairable broken resources

### Phase 3 (Planned)
- [ ] Enhanced dimension visuals (color grading, fog)
- [ ] Echo terrain generation
- [ ] Custom biomes (Silent Forest)

### Phase 4 (Planned)
- [ ] Echo boss fights
- [ ] Dimension stability mechanics
- [ ] Echo progression system

## Requirements

- **Minecraft**: 1.21.1
- **NeoForge**: 1.21.1
- **Java**: JDK 21+
- **Gradle**: 8.9+

## Building Locally

```bash
# Clone the repository
git clone https://github.com/Raktos1030/echo_dimension_mod.git
cd echo_dimension_mod

# Setup workspace
./gradlew setup

# Build the mod
./gradlew build

# Run client for testing
./gradlew runClient
```

The compiled JAR will be in `build/libs/`.

## Project Structure

```
echo_dimension_mod/
├── src/main/java/com/raktos/echodimension/
│   ├── EchoDimensionMod.java          # Main mod entry point
│   ├── block/
│   │   ├── EchoPortalBlock.java       # Portal block logic
│   │   └── EchoPortalBlockEntity.java # Portal block entity
│   ├── dimension/
│   │   ├── EchoDimension.java         # Dimension definition
│   │   ├── EchoDimensionType.java    # Dimension type
│   │   ├── EchoDimensionProvider.java # Dimension provider
│   │   ├── EchoChunkGenerator.java    # Terrain generation
│   │   └── EchoWorldCarver.java       # Cave generation
│   ├── item/
│   │   └── DimensionAccessItem.java   # Echo Recorder item
│   ├── data/
│   │   └── PlayerEchoData.java        # Per-player echo tracking
│   ├── event/
│   │   └── PlayerActionRecorder.java  # Event listener for player actions
│   └── network/
│       └── EchoPacketHandler.java     # Network packet handling
├── src/main/resources/
│   ├── assets/echodimension/
│   │   ├── lang/en_us.json           # English translations
│   │   ├── blockstates/             # Block state definitions
│   │   ├── models/                  # JSON models
│   │   └── textures/                # Textures
│   └── META-INF/neoforge.mods.toml  # Mod metadata
├── build.gradle                      # Build configuration
├── settings.gradle                  # Project settings
└── gradle.properties                # Gradle properties
```

## Mod ID

`echodimension`

## Theme Colors

- Primary: Dark Purple (#2a0a4a)
- Accent: Cyan (#00c8c8)
- UI: Deep violet and teal gradients

## Configuration

Place the built JAR in your Minecraft mods folder:
```
~/.minecraft/mods/echodimension-x.x.x.jar
```

## License

MIT License

## Author

Raktos1030
