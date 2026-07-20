# Echo Dimension Mod

Un mod Minecraft NeoForge qui crée une **Dimension Écho** - un reflet temporel de l'Overworld où les actions passées du joueur "hantent" le monde sous forme d'échos jouables.

## 📋 Concept

La Dimension Écho est un **REFLET TEMPOREL** de l'Overworld où :
- Les structures construites par le joueur apparaissent comme des **bâtisses fantômes**
- Les mobs tués réapparaissent comme des **entités "échos"** hostiles
- Les ressources minées réapparaissent **fragmentées**, à "réparer"

## 🚀 Installation

### Prérequis
- Minecraft 1.21.1
- NeoForge 21.1.243
- Java 21
- Gradle (fourni via le wrapper)

### Compilation

```bash
# Cloner le dépôt
git clone https://github.com/Raktos1030/echo_dimension_mod.git
cd echo_dimension_mod

# Compiler le mod
./gradlew build

# Le JAR sera dans build/libs/
```

### Exécution en développement

```bash
./gradlew runClient
# ou
./gradlew runServer
```

## 📁 Structure du Projet

```
src/main/java/com/echodimension/
├── core/           # Classe principale du mod
├── registry/       # Registres custom
├── blocks/          # Blocs (portail, écho stone, etc.)
│   └── portal/     # Système de portail
├── items/          # Items (déclencheur, recorder, etc.)
├── dimensions/     # Configuration de la Dimension Écho
├── world/          # Génération de monde
├── entities/       # Entités (échos, boss)
│   └── echo/       # Entités écho
├── events/         # Événements (capture d'actions)
├── network/        # Synchronisation client/serveur
└── data/           # Données sauvegardées
```

## 📅 Phases de Développement

### Phase 1 : Fondations ✅
- [x] Structure du projet
- [x] Configuration Gradle
- [x] Fichier mods.toml
- [ ] Classes de base vides avec TODOs

### Phase 2 : Monde
- [ ] Dimension Écho configuration
- [ ] ChunkGenerator custom
- [ ] Génération de terrain "déformé"
- [ ] Biomes personnalisés

### Phase 3 : Portail
- [ ] Bloc central du portail
- [ ] Cadre du portail
- [ ] Item déclencheur
- [ ] Téléportation entre dimensions

### Phase 4 : Échos
- [ ] Système de capture des actions
- [ ] Entité EchoPhantom
- [ ] Stockage des données
- [ ] Événements de capture

### Phase 5 : Contenu
- [ ] Blocs de la Dimension Écho
- [ ] Items de réparation
- [ ] Réseau client/serveur
- [ ] Effets visuels

### Phase 6 : Boss
- [ ] Boss Echo Wraith
- [ ] Mécaniques de boss
- [ ] Récompenses

## 🎮 Mécaniques

### Le Portail
1. Craft l'item déclencheur du portail
2. Place les blocs de cadre en rectangle (min 2x3)
3. Utilise l'item sur le cadre pour créer le portail
4. Traverse le portail pour entrer dans la Dimension Écho

### Système d'Échos
- **Capture** : Chaque action du joueur est enregistrée
- **Reproduction** : Dans la Dimension Écho, les actions sont "rejouées"
- **Réparation** : Les fragments peuvent être réparés avec un marteau spécial

### Ambiance
- Palette de couleurs décalée (teinte bleutée/noirâtre)
- Effets de particules "écho"
- Sons déformés

## 🛠️ Développement

### IDE Recommandé
- IntelliJ IDEA (recommandé)
- Eclipse (avec plugin Gradle)

### Commandes utiles

```bash
# Régénérer les configs IDE
./gradlew eclipse
./gradlew idea

# Compiler
./gradlew build

# Nettoyer
./gradlew clean

# Tester avec Minecraft
./gradlew runClient
```

## 📝 Licence

MIT License - Voir LICENSE pour plus de détails.

## 👤 Auteur

**Raktos1030** - Développement initial

---

*Ce mod est en développement actif. Suivez le dépôt pour les mises à jour !*
