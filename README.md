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
<ol>
  <li>The lineSolvable() bool function was inefficient, and hanged on invalid cases for too long. This is highly problematic because this function is called on each step. A new algorithm has been designed to replace this problem.</li>
  <li>The algorithm would analyze cells on the board where no changed had occured in its column or row. Logically, if the cell wasn't filled and its slices didn't change, it will remain unsolvable. Now only areas of the board where a change has occured will be checked.</li>
<li>When checking for a contradiction, a solution must be found to prove no contradiction occurs, but this solution isn't stored anywhere. If the slice solution is the right-most solution found, the program will have to adjust all the blocks right every time step is called. The current program remembers previous solutions, creating an ammortized solution.</li>
  <li>Solver tracked all unsolved cells in the nonogram and iterated over them in a loop until they were all gone (or they had all been checked, meaning no progress was able to be made). This ignores the fact that often larger clusters of cells will be solved together, and addressing each individual cell is often redundant. The new algorithm address slices instead of cells, which allows for larger clusters of cells to be solved at once.</li>
</ol>

Most testing needs to be done before I can definitely say my design will return faster results, but the current results are promising. It will take a lot of work to switch from my "find a contradiction" method to "find all possibilities" method, but I'm passionate about this project, and as soon as I have time I will implement it to the best of my ability.

Thank you for reading!

