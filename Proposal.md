# Overview
A top down, procedurally generated tile game completely in Java and AWT. It will feature keyboard press events for player movement, mouse press events for actions and threading for animations.

# Feautures and Subsystems
## Terrain Generation
We will implement a infinate procederal terrain generation. The specific algorithm is still to be determined. Some options are
* Perlin or Simplex noise ( Will look the best)
* Voroni noise ( Will look interesting)
* Diamond Square noise (Light weight and reasonably realistic)

## Player
The player avatar will be a sprite image. The sprite will hae many seraeate images to provide animation by cycling through the sprites.
The animation will be done on a thread. 
