# Nonogram Project
After solving many nonograms by hand and getting bored with how mechanical it felt, I wondered how efficiently a computer could solve a nonogram. There are other nonograms solvers on the Internet, but I wanted to try to implement my own.

The structure of the files works as such:
- <b>FileFinder</b>: A soft coded way to interact with your images and textfiles. Used when a prorgam needs to import an image or textfile.
- <b>ImageToTextFile</b>: May be used to import images into a textfile describing the nonogram.
- <b>NonogramDriver</b>: The central file for the nonogram program. Imports a textfile, creates a Nonogram object, sends it to NonogramSolver, and handles NonogramSolver.
- <b>Nonogram</b>: Object that information about the nonogram "board". It contains a enumerated type Tile (XED, FILL, UNKN), which describe the 3 states a tile on the nonogram board can be in.
- <b>NonogramSolver</b>: Orders list of tiles to be investigated, handles which solve state the nonogram board is currently in, investigates tiles, and alters the Nonogram when applicable. Can also do a test run of a nonogram in hasSolution(). This class may be split into 2 classes to help with readability and efficiency.
- <b>NonogramDrawer</b>: Creates and saves images of the current Nonogram. May be called after every step in NonogramSolver, or only after the Nonogram is solved.

The section of the program that is most likely to change is how NonogramSolver solves. It's hard to gauge efficiency of the program because many factors affect the difficulty: how tiles the board has, the ratio of the board, how many tiles are filled in, how much whitespace occurs between the filled in tiles, and how closely the filled in tiles appear along the edges of the board. That said, the program seems to become extremely inefficient once the size of the board gets past 40, even if the puzzle should be easy to solve otherwise. I plan to rectify this with a new primary step which goes around the edges of the board. If there is a filled in tile  closer to the edge than the closest integer describing that side, other tiles can be filled in.


E.g.

If a row looked like this:
... ? ? ? ? X ? 

And the row's descriptor was [... 4], then the the row can be futher filled in as such:
... ? ? X X X ?

This new method may no fix the primary issue with scalabiltiy, but it should at least allow the program to more quickly solve easier nonograms, regardless of the size of the board.

(This README file is currently incomplete.)
