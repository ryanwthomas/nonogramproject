# Nonogram Project
After solving many nonograms by hand and getting bored with how mechanical it felt, I wondered how efficiently a computer could solve a nonogram. There are other nonograms solvers on the Internet, but I wanted to try to implement my own.

The structure of the files works as such:
- <b>FileFinder</b>: A soft coded way to interact with your images and textfiles. Used when a prorgam needs to import an image or textfile.
- <b>ImageToTextFile</b>: May be used to import images into a textfile describing the nonogram.
- <b>NonogramDriver</b>: The central file for the nonogram program. Imports a textfile, creates a Nonogram object, sends it to NonogramSolver, and handles NonogramSolver.
- <b>Nonogram</b>: Object that information about the nonogram "board". It contains a enumerated type Tile (XED, FILL, UNKN), which describe the 3 states a tile on the nonogram board can be in.
- <b>NonogramSolver</b>: Orders list of tiles to be investigated, handles which solve state the nonogram board is currently in, investigates tiles, and alters the Nonogram when applicable. Can also do a test run of a nonogram in hasSolution(). This class may be split into 2 classes to help with readability and efficiency.
- <b>NonogramDrawer</b>: Creates and saves images of the current Nonogram. May be called after every step in NonogramSolver, or only after the Nonogram is solved.

Currently this project is under major renovations. I've designed a new approach for NonogramSolver. The previous design (while it worked efficienctly on most nonograms meant for humans to solve) didn't scale properly for a number of reasons.
<ul>
  <li>First, the Solver analyzed specific cells in the nonogram, </li>
</ul>

