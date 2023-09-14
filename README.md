![Mankala 3000](logo.png)

## Description

This is a game of Mankala, a game of strategy and luck. The game is played on a board with 12 holes,
6 on each side. Each player has a store to the right of their side of the board. The game starts
with 6 stones in each hole. The player to the right of the board starts by picking up all the stones
in one of their holes and placing one stone in each hole counter-clockwise around the board. If the
player places a stone in their store, they get to go again. If the player places a stone in an empty
hole on their side of the board, they get to take all the stones in the hole directly across from it
and place them in their store. The game ends when one player has no stones on their side of the
board. The player with the most stones in their store wins.

## How to run

1. Clone the repository
2. Run `docker-compose up`
3. Navigate to http://localhost:3000 in your browser
4. Enjoy the game!

## Issues

1. I failed to build web client in the container. So I had to add client build to the repository.
It's a bad practice and has to be fixed.
2. Web client is hideous inside.
3. Real name is Mancala, but I made a misprint in the beginning, and now I think it looks better
this way.
