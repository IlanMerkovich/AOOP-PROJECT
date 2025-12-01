# D&D Turn-Based Strategy Game
### Advanced Object-Oriented Programming Project (Java)

This project is a **fully-structured, object-oriented turn-based strategy game** inspired by Dungeons & Dragons and implemented entirely in **Java**.  
It demonstrates clean architecture, scalable design, and practical use of multiple design patterns — making it suitable as a **portfolio project** showcasing software engineering ability.

---

## Project Overview

The player controls a character on a grid-based map, battling enemies, collecting items, and progressing through strategic turn-based gameplay.  
The game engine is modular and built to be easily extended with new mechanics, characters, items, and behaviors.

This project emphasizes **maintainability**, **extensibility**, and **clarity of design**, similar to real-world applications.

---

## Technical Highlights

### Multi-Layer Architecture
The project is organized into clear logical layers:
- **Engine Layer** – world simulation, turn execution, enemy logic
- **Character Layer** – player classes, enemies, stats, and behaviors
- **Combat Layer** – attack rules, damage formulas, interfaces
- **Items & Inventory Layer**
- **Builder / Factory / Decorator Layers**
- **GUI Layer (Swing)** – rendering, input handling, event routing

### Advanced OOP Practices
- Abstraction and interface‑driven design
- Inheritance hierarchy for character specialization
- Composition for behavior logic
- Runtime ability enhancement via decorators
- Thread‑safe game operations using `ReentrantLock`

### Design Patterns Used
- **Factory** – enemy creation
- **Builder** – structured creation of complex characters
- **Decorator** – extending enemy behavior dynamically
- **Memento** – complete world save/load system
- **MVC‑inspired separation** between logic and interface

---

## Gameplay Systems

### Characters
- **Player classes:** Warrior, Mage, Archer
- **Enemy types:** Goblin, Orc, Dragon
- Unique stats, combat styles, resistances, and movement patterns

### Combat System
- Physical, magical, and ranged attacks
- Elemental mechanics
- Evasion, critical hits, resistances
- Modular damage calculation handled by `CombatSystem`

### Items & Inventory
- Potions
- Power potions
- Treasure items
- Walls & interactables
- Inventory with usage logic

### Enemy Behavior Enhancements (Decorators)
- Teleportation
- Shielding
- Rage mode
- Lifesteal
- Agility boosts
- Explosive effects

---

## High-Level Architecture Diagram

```
[ GUI Layer ]
  └── StartFrame
  └── MainFrame
  └── MapPanel / StatusPanel / InventoryPanel
             ↓ events
[ Controller ]
             ↓ delegates
[ Engine Layer ]
  └── GameWorld
  └── EnemyManager
  └── GameMap
             ↓ accesses
[ Character System ]
  └── Players
  └── Enemies
             ↓ uses
[ Combat System ]  [ Items ]  [ Decorators ]  [ Builders ]
```

---

## Project Structure

```
Game.Engine        → world logic, controller, enemy manager, memento
Game.Characters    → players, enemies, stats, shared behaviors
Game.Combat        → attack interfaces, combat engine
Game.Items         → potions, treasure, interactables
Game.Builders      → creation of players and enemies
Game.Decorators    → enemy behavior modifiers
Game.GUI           → Java Swing UI components
Game.Logs          → logging services
Game.Utils         → Position and additional helpers
```

---

## How to Run

1. Clone the repository:
```
git clone https://github.com/IlanMerkovich/AOOP-PROJECT.git
```

2. Open in **IntelliJ IDEA** or any Java IDE
3. Ensure **Java 17+**
4. Run:
```
MainFrame
```

---


## Author

Developed by **Ilan Markovich**  
Advanced Object-Oriented Programming – Final Project  
Designed as a professional, extensible, and portfolio‑ready Java application.
