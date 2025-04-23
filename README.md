# AOOP Turn-Based RPG Game

Welcome to **AOOP Turn-Based Game** — a Java-based text RPG developed as part of the Advanced Object-Oriented Programming (AOOP) course.

## Description

This is a console-based turn-based role-playing game inspired by *Dungeons and Dragons*. The player chooses a character class and battles various enemies on a randomly generated grid map, collecting treasure and using potions along the way.

## Project Structure

The project is organized into the following packages:

- `Game.Characters` – Player and enemy classes (e.g., Warrior, Mage, Goblin, Dragon)
- `Game.Engine` – Game world, map logic, and main game loop
- `Game.Combat` – Combat interfaces and combat system
- `Game.Items` – Game items like potions, power potions, treasure, and walls
- `Game.Core` – Core interfaces and utility classes
- `Game.Map` – `Position` class to manage coordinates

## How to Run?

1. Compile the code using your favorite Java IDE or `javac` from the terminal.
2. Run the `Main` class.
3. Follow the prompts to:
   - Set the map size (minimum 10x10)
   - Choose your character class
   - Play the game turn by turn

## Gameplay Features

- Multiple enemy types with unique behaviors and stats
- Melee and ranged combat (including magical attacks)
- Health and power potions
- Treasure chests with randomized rewards
- Fog of war using visibility range
- Logging system for detailed combat actions
