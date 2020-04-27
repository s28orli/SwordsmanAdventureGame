# Overview
A top down, procedurally generated tile game completely in Java and AWT. It will feature keyboard press events for player movement, mouse press events for actions and threading for animations.

# Features and Subsystems
## Terrain Generation
We will implement a infinate procederal terrain generation. The specific algorithm is still to be determined. Some options are
* Perlin or Simplex noise ( Will look the best)
* Voroni noise ( Will look interesting)
* Diamond Square noise (Light weight and reasonably realistic)
To simplify this process, we will subdivide the terrain into square groups of a specific size called chunks and generate it with a couple chunks at a time. We will render only the immediate chunks to avoid lag.

## Game
The player avatar will be a sprite image. The sprite will hae many seraeate images to provide animation by cycling through the sprites.
The animation will be done on a thread. The player's actions will be updated in an enum value to determine the proper sprite for the action.

## Controls
The player movement will be controlled by keyboard keys which is why we will use keypress events. Game actions will be controlled by mouse which is why we need the use mouse events. 
