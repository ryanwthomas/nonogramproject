// Code written by Ryan WH Thomas
// Last updated: 2/8/2019

package nonogramProjectv1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

import nonogramProjectv1.Nonogram;
import nonogramProjectv1.Nonogram.Tile;

public class NonogramSolver {

	private static int counter = 1;
	private static int debugCounter = 1;

	public static final boolean printNonogram = NonogramDriver.printNonogram;
	public static final boolean debug = NonogramDriver.debug;

	private Nonogram n;
	private int steps = 0;

	private int index = 0;

	private int solvedTiles = 0;

	// An list of Coord(inates) that indicate unsolved Tiles in the nonogram.
	// An ArrayList is used because the amount of elements is variable
	private ArrayList<Coord> points = new ArrayList<>();

	private boolean progressed = false;
	private boolean tertiary = false;
	public boolean progressable = true;
	public boolean broken = false;

	public NonogramSolver(Nonogram n) throws Exception {
		this.n = n;

		// use preliminary method on rows
		for(int i = n.getHeight()-1; i >= 0; i --)
			n.setRow( primaryFill( n.getRowID(i), n.getWidth() ), i );

		// use preliminary method on columns
		for(int i = n.getWidth()-1; i >= 0; i --)
			n.setColumn( primaryFill( n.getColumnID(i), n.getHeight() ),
					i );

		// top edge downward
		for(int x = n.getWidth()-1; x >= 0; x --) {
			int len = n.getColumnID(x)[0];
			boolean filled = false;

			for(int y = 0; y < len; y++) {
				filled |= n.getTile(x, y) == Tile.FILL;

				if( filled && n.getTile(x, y) == Tile.UNKN )
					n.setTile(x, y, Tile.FILL);
			} }

		// left edge rightward
		for(int y = n.getHeight()-1; y >= 0; y --) {
			int len = n.getRowID(y)[0];
			boolean filled = false;

			for(int x = 0; x < len; x++) {
				filled |= n.getTile(x, y) == Tile.FILL;

				if( filled && n.getTile(x, y) == Tile.UNKN )
					n.setTile(x, y, Tile.FILL);
			} }

		// bottom edge upward
		for(int x = n.getWidth()-1; x >= 0; x --) {
			int len = n.getColumnID(x)[ n.getColumnID(x).length-1 ];
			boolean filled = false;

			for(int y = 0; y < len; y++) {
				filled |= n.getTile(x, n.getHeight() -y -1) == Tile.FILL;

				if( filled && n.getTile(x, n.getHeight() -y -1) == Tile.UNKN )
					n.setTile(x, n.getHeight() -y -1, Tile.FILL);
			} }

		// right edge leftward
		for(int y = n.getHeight()-1; y >= 0; y --) {
			int len = n.getRowID(y)[ n.getRowID(y).length-1 ];
			boolean filled = false;

			for(int x = 0; x < len; x++) {
				filled |= n.getTile(n.getWidth() -x -1, y) == Tile.FILL;

				if( filled && n.getTile(n.getWidth() -x -1, y) == Tile.UNKN )
					n.setTile(n.getWidth() -x -1, y, Tile.FILL);
			} }





		points = generateCoords(n);
		solvedTiles = n.getHeight()*n.getWidth() - points.size();

		// sort the Coords (by the number of solved tiles in its row and column)
		Collections.sort( points );
		/*
		 *  Reverse the order from non-decreasing to non-increasing order
		 *  	i.e. the tile with the max value will be at index 0.
		 */
		Collections.reverse( points );
	}

	public Nonogram getNonogram() {
		return n;
	}

	private static ArrayList<Coord> generateCoords(Nonogram myNonogram){
		// these arrays count how many filled tiles are in each row or column
		int[] valueX = new int[myNonogram.getWidth()];
		int[] valueY = new int[myNonogram.getHeight()];

		// iterate through every tile; if the tile is filled,
		// 		increment the appropriate index of the value arrays.
		for(int y = 0; y < myNonogram.getHeight(); y++ )
			for(int x = 0; x < myNonogram.getWidth(); x++ ) 
				if( myNonogram.getTile(x, y) == Tile.FILL ) { 
					valueX[x]++; valueY[y]++;
				}

		ArrayList<Coord> toReturn = new ArrayList<>();

		/*
		 * Iterate over every tile and create a Coord if the tile is unknown.
		 */
		for(int y = 0; y < myNonogram.getHeight(); y++ )
			for(int x = 0; x < myNonogram.getWidth(); x++ )
				if( myNonogram.getTile(x, y) == Tile.UNKN )
					toReturn.add( new Coord( x, y,
							valueX[x] + valueY[y] ) );

		return toReturn;
	}

	public int getSteps() {
		return steps;
	}

	/*
	 * If no Coords remain, then the Nonogram is finalized.
	 * We then check if every row and column is solved correctly.
	 * If any row or column is incorrect after the Nonogram is finalized,
	 * 		then return false.
	 */
	public boolean nonogramSolved() {
		// 	Remove the tail of the ArrayList (repeatedly) if it's already solved.
		while( !points.isEmpty() &&
				n.getTile( points.get( points.size()-1 ).getX(),
						points.get( points.size()-1 ).getY() )
				!= Tile.UNKN)
			points.remove( points.size()-1 );

		// If points is empty, check if every row and column is solved correctly
		if( points.isEmpty() ) {
			boolean solvedCorrectly = true;

			for(int x = n.getWidth()-1; x >= 0; x--)
				solvedCorrectly &= lineSolved( n.getColumn(x), n.getColumnID(x) );

			for(int y = n.getHeight()-1; y >= 0; y--) 
				solvedCorrectly &= lineSolved( n.getRow(y), n.getRowID(y) );
			return solvedCorrectly;
		}
		else
			return false;
	}

	public boolean step() throws Exception {
		// sets index to correct location (see getIndex() for detail)
		// returns copy of index that goes unused here

		if(debug)
			System.out.println( "P\t"+progressable );
		getIndex();


		if( points.isEmpty() || !progressable )
			return false;

		steps++;

		// If tertiary is false, use secondarySolve(), else use tertiarySolve()
		Tile t = !tertiary? secondarySolve( n, points.get(index) ) :
			tertiarySolve( n, points.get(index) );

		if( printNonogram )
			NonogramDrawer.saveImage(n, n.name,
					points.get(index).getX(), points.get(index).getY(),
					t == Tile.FILL ? Color.GREEN :
						t == Tile.XED ? Color.RED : Color.YELLOW
					);

		if( t == Tile.FILL || t == Tile.XED )
			solvedTiles++;

		// System.out.println( points.get(index) +"\t"+ t);
		if( debug )
			System.out.println( "S\t"+steps+"\t"+solvedTiles );

		/*
		 * If t is Fill or XED, then
		 *		1. set tile of Nonogram
		 *		2. remove current Coord from ArrayList
		 *		3. and add surrounding Coords.
		 */
		if( t == Tile.FILL || t == Tile.XED ) {
			progressed = true;
			tertiary = false;
			progressable = true;

			for(int i = 0; i < index; i++ ) {
				points.add( points.remove(0) );
			}

			index = 0;

			int myX = points.get(index).getX(),
					myY = points.get(index).getY();
			// 1
			n.setTile( myX, myY, t);

			// 2
			points.remove(index);

			int[] distance = new int[4];
			Coord[] cToAdd = new Coord[4];
			int point = -1;

			// 3
			// move to nearest unsolved tile on left
			{
				int x = myX - 1;				
				point++;

				// find closest unsolved Tile
				while( x >= 0 && n.getTile(x, myY) != Tile.UNKN )
				{ x--; distance[point]++; }
				if( x >= 0) cToAdd[point] = new Coord( x, myY );
			}



			// move to nearest unsolved tile downward
			{
				int y = myY - 1;				
				point++;

				// find closest unsolved Tile
				while( y >= 0 && n.getTile(myX, y) != Tile.UNKN )
				{ y--; distance[point]++; }
				if( y >= 0 ) cToAdd[point] =  new Coord( myX, y );
			}

			// move to nearest unsolved tile on right
			{
				int x = myX + 1;				
				point++;

				// find closest unsolved Tile
				while( x < n.getWidth() &&
						n.getTile(x, myY) != Tile.UNKN )
				{ x++; distance[point]++;}
				if( x < n.getWidth() ) cToAdd[point] = new Coord( x, myY );
			}

			// move to nearest unsolved tile upward
			{
				int y = myY + 1;				
				point++;

				// find closest unsolved Tile
				while( y < n.getHeight() &&
						n.getTile(myX, y) != Tile.UNKN )
				{ y++; distance[point]++; }
				if( y < n.getHeight() ) cToAdd[point] = new Coord( myX, y );
			}

			// bubble sort (its only 4 elements who cares)
			// order non-ordered elements
			// shuffle ordered elements
			for(int i = 0; i < 4-1; i++)
				for(int j = i + 1; j < 4; j++ )
					if( distance[i] >= distance[j] ) {
						int tempInt = distance[i];
						Coord tempCoord = cToAdd[i];

						distance[i] = distance[j];
						cToAdd[i] = cToAdd[j];

						distance[j] = tempInt;
						cToAdd[j] = tempCoord;
					}

			for(int i = 3; i >= 0; i--)
				if( cToAdd[i] != null )
					points.add( index, cToAdd[i] );

			return true;
		}
		// if the tile is still unsolved, then simply increase index
		else if( t == Tile.UNKN ) {
			index++;
			return false;
		}
		// else, the method return NULL, meaning a contradiction has occured
		else {
			if( debug )
				System.out.println("NULL");

			broken = true;

			// clearing the points, means no more progress can occur
			// and because the nonogram is not solved, nonogramSolved()
			//		will return false
			points.clear();
			return false;
		}
	}

	public boolean nonogramSolvable() throws Exception {
		NonogramSolver nsCopy = new NonogramSolver( this.n );

		while( !nsCopy.points.isEmpty() && !nsCopy.nonogramSolved() ) {
			nsCopy.step();
			if( debug ) {
				System.out.println( "X\t"+nsCopy.steps +"\t"+
						nsCopy.index);
			}
		}
		return nsCopy.nonogramSolved();
	}

	/*
	 *	This method validates the current index and changes it if is invalid.
	 *	The method makes sure Coord at index is unsolved.
	 * 	If the Coord is undex IS solved,
	 * 		the method removes the Coord at index until it arrives at a Coord
	 * 			that is unsolved OR the index becomes out of bounds
	 * 			(i.e. the were no unsolved Coord in [index, points.size()]).
	 * 
	 * If the index is (or becomes) out of bounds, the method loops the index
	 * 		to the front of the array (i.e. index = 0), updates 'progressed'
	 * 		and calls getIndex() again to ensure the 0 is a valid index.
	 */
	private int getIndex() {
		// remove solved Coords from current index (repeatedly)
		/*
		 * While the index is less or equal to the the last index
		 * 		AND the Coord at index is solved, remove said Coord.
		 */
		while( index < points.size() &&
				n.getTile( points.get(index).getX(),
						points.get(index).getY() ) != Tile.UNKN )
			points.remove(index);

		progressable &= points.size() > 0;

		if( index + 1 < points.size() &&
				points.get(index).equals( points.get(index+1)) )
			points.remove( index + 1 );

		/*
		if( index + 2 < points.size() &&
				points.get(index).equals(  points.get(index+2)) )
			points.remove( index + 2 );
		 */

		/*
		 * If the index has fallen of the end of the ArrayList,
		 * 		then start at the beginning.
		 * If points.size() <= 0, then DO NOT enter if-statement because
		 * 		the recursive call will create an infinite loop.
		 */
		if( index >= points.size() && points.size() > 0 ) {
			index = 0;
			getIndex();

			progressable &= !tertiary || progressed;

			/*
			 *  If no progressed occurred from index = 0 to index out of bounds,
			 *  	then the steps uses the tertiary method instead of the
			 *  	secondary method.
			 */
			tertiary = !progressed;

			// progressed is set back to false
			progressed = false;
		} else if ( points.size() == 0 ) {
			progressable = false;
			index = -1;
		}

		if(debug) System.out.println("END");
		return index;
	}

	/*
	 * This method performs a basic fill of some tiles of a nonogram.
	 * 		and should be applied to the nonogram first.
	 * 
	 * The method works like this:
	 * For example, if a row is 10 tiles long, and its id is [10],
	 * 		then the whole row is filled.
	 * If, instead, its id was [9], then you can't know the exact filling.
	 * BUT you know the middle-most 8 tiles will be filled because
	 * 		of the two orientations ( left-justified and right-justified )
	 * 		there exists no solution where the middle-most tiles are filled.
	 * 
	 * You can think of the space cut off either end of a block as a buffer.
	 * A row's buffer is defined by the row's length minus the ID's length.
	 * 
	 * Therefore, any time a block is longer than the buffer, there must exist
	 * 		some blocks that must be filled in all given solutions.
	 * 
	 */
	private static Tile[] primaryFill( int[] nums, int width ) {

		Tile[] toReturn = new Tile[width];

		// set toReturn to all UNKN
		for(int i = 0; i < width; i++)
			toReturn[i] = Tile.UNKN;

		int buffer = width - IntStream.of(nums).sum() - nums.length + 1;

		/*
		 *  Only enter if buffer is less than (or equal to) the width of the
		 *  	row/column. If the buffer is larger than half the width, then
		 *  	it's impossible for any block to be longer than the buffer,
		 *  	meaning no Tiles can be filled in this method.
		 */
		if( buffer <= width/2 ) {
			int[] leftIndices = new int [nums.length];

			for(int i = 0; i < nums.length; i++) {
				// leftIndices[0] is correctly set at 0, so no need to change
				if( i > 0)
					// set current index to the index of the last block plus
					//		its length plus 1 for space between blocks
					leftIndices[ i ] = leftIndices[ i-1 ] + nums[ i-1 ] + 1;

				/*
				 *  Fill Tiles in range [ index + buffer, index + length of block ].
				 *  If buffer > length of block, the for-loop iterates 0 times.
				 */
				for(int s = leftIndices[i] + buffer;
						s < leftIndices[i] + nums[i]; s++)
					toReturn[s] = Tile.FILL;
			}

		}
		return toReturn;
	}

	private static Tile secondarySolve(Nonogram n, Coord c) {
		// GetColumn and getRow are independent from the nonogram,
		// i.e. modifying these arrays will not affect the nonogram.
		Tile[] vertical = n.getColumn( c.getX() ),
				horizontal = n.getRow( c.getY() );

		// if the line is already unsolvable, return null
		// ((((MAY CHANGE RETURN TYPE LATER)))
		if( !lineSolvable(vertical, n.getColumnID(c.getX())) ||
				!lineSolvable(horizontal, n.getRowID(c.getY())))
			return null;



		// does Xing the space make the nonogram unsolvable?
		vertical[c.getY()] = Tile.XED;
		horizontal[c.getX()] = Tile.XED;

		if( !lineSolvable(vertical, n.getColumnID(c.getX())) ||
				! lineSolvable(horizontal, n.getRowID(c.getY())))
			// if so, then the space must be filled in the solution
			return Tile.FILL;
		else {
			// if not, does filling the space make the nonogram unsolvable?
			vertical[c.getY()] = Tile.FILL;
			horizontal[c.getX()] = Tile.FILL;

			if( !lineSolvable(vertical, n.getColumnID(c.getX())) ||
					! lineSolvable(horizontal, n.getRowID(c.getY())))
				// if so, then the must be Xed in the solution
				return Tile.XED;
			// if not, try tertiary solve
			else {

			}
		}

		// if the nonogram is solvable when Xed or filled,
		// 		then it cannot be determined if it's one or the other.
		return Tile.UNKN;
	}

	// this can be static w/o the reference to name
	// private static Tile tertiarySolve(Nonogram n, Coord c) {
	private Tile tertiarySolve(Nonogram myNonogram, Coord c) throws Exception {
		if(debug) {
			System.out.println("Teriary Solve:\t" + n.name);
			System.out.println(this);
		}
		/*
		if( Math.random() > 0 )
			System.exit(0);
		 */
		if( !hasSolution( myNonogram ) )
			return null;

		Nonogram nonoCopy1 = new Nonogram(myNonogram);

		nonoCopy1.setTile(c.getX(), c.getY(), Tile.FILL);

		counter++;

		if( !hasSolution( nonoCopy1 ) )
			return Tile.XED;
		else {
			Nonogram nonoCopy2 = new Nonogram(myNonogram);
			nonoCopy2.setTile(c.getX(), c.getY(), Tile.XED);

			if( !hasSolution( nonoCopy2 ) )
				return Tile.FILL;
			else
				return Tile.UNKN;
		}

	}

	// finds solution for raw, unprocessed nonogram
	private static boolean hasSolution(Nonogram myNonogram) throws Exception {
		if(debug) System.out.println( debugCounter++ );

		if(debugCounter > Integer.MAX_VALUE -1 ) {
			System.out.println("DEBUG COUNT OVERRUN");
			System.exit(1);
		}

		for(int x = myNonogram.getWidth()-1; x >= 0; x--)
			if( !lineSolvable( myNonogram.getColumn(x),
					myNonogram.getColumnID(x) ) )
				return false;

		for(int y = myNonogram.getHeight()-1; y >= 0; y--)
			if( !lineSolvable( myNonogram.getRow(y),
					myNonogram.getRowID(y) ) )
				return false;

		// find first unsolved Tile
		Coord c = null;

		for(int y = 0; y < myNonogram.getHeight(); y++  )
			for(int x = 0; x < myNonogram.getWidth(); x++)
				if( c != null ) { y = myNonogram.getHeight()+1;
				x = myNonogram.getWidth()+1; }
				else if ( myNonogram.getTile(x, y) == Tile.UNKN  )
					c = new Coord(x, y);

		// no unknown tiles were found
		if( c == null ) {
			NonogramSolver temp = new NonogramSolver( myNonogram );

			if(debug)
				System.out.println( "INSIDE C == NULL \n"+myNonogram+
						"\n"+temp.nonogramSolved());

			//	/*
			if( temp.nonogramSolvable() && printNonogram )
				NonogramDrawer.saveImage( myNonogram, "SOLUTION",
						-1, -1, Color.PINK
						);
			// */

			return temp.nonogramSolved();
		}

		Nonogram alteredNonogram = new Nonogram( myNonogram );

		if(debug)
			System.out.println("\t"+c +"\n"+alteredNonogram);

		alteredNonogram.setTile( c.getX() , c.getY(), Tile.FILL);

		if(debug)
			System.out.println("\n"+alteredNonogram);

		// This is very inefficient,
		// because (as I see it currently) we only need to run solving methods the row and column of the Coord c
		// but, it should work
		/*
		NonogramSolver temp = new NonogramSolver( new Nonogram(alteredNonogram) );
		while( temp.progressable && !temp.tertiary ) { temp.step(); }
		 */
		if( hasSolution( alteredNonogram ) ) { return true; }
		else {
			alteredNonogram.setTileOverride( c.getX(), c.getY(), Tile.XED );
			/*
			NonogramSolver temp2 = new NonogramSolver( new Nonogram(alteredNonogram) );
			while( temp2.progressable && !temp.tertiary ) { temp2.step(); }
			 */
			return hasSolution( alteredNonogram );
		}

	}

	private static boolean lineSolvable( Tile[] arr, int[] nums ) {
		if( lineSolved(arr, nums) )
			return true;

		int firstBlock;

		{
			int temp = 0;
			while( temp < arr.length && arr[temp] != Tile.FILL )
				temp ++;
			firstBlock = temp;
		}


		int a = ( arr.length - (IntStream.of(nums).sum() + nums.length -1) );

		// System.out.println( a+"\t"+nums.length+"\t"+permutations(a, nums.length)  );

		// list of indices of the line's blocks
		int[] indices = new int[nums.length];

		// list of indices that 'indices' cannot go past
		int[] maxes = new int[nums.length];

		maxes[ maxes.length-1 ] = arr.length - nums[maxes.length-1];
		for(int i = maxes.length-2; i >= 0; i--)
			maxes[i] = maxes[i+1] - nums[i] - 1;

		// set the indices if all the blocks were left-justified
		for(int i = 0; i < indices.length; i++) {
			// set indices of current block to be one tile
			//		after the previous Tile's end
			if( i > 0 )
				indices[i] = indices[i-1] + nums[i-1] +1;

			indices[i] = findValidIndex( indices[i], maxes[i], nums[i], arr );
		}


		while(true){

			if( testIndices(arr, indices, nums) )
				return true;

			// while the last block hasn't fallen off the edge, 
			// increase the index of the last block
			while( indices[indices.length - 1] <
					maxes[indices.length -1]  ) {
				indices[indices.length - 1]++;

				if( testIndices(arr, indices, nums) )
					return true;
			}

			int index = indices.length-1;

			// reverse
			while( index >= 0 && 
					indices[index] > maxes[index] )
				index--;

			//if you've ended the series, exit loop
			if( index == -1 || indices[0] > firstBlock ) {
				// System.out.println("ALOHA");
				break;
			}

			// update the first non-maxed index
			indices[index]++;

			indices[index] = findValidIndex( indices[index],
					maxes[index], nums[index], arr );

			index++;

			// forward
			while( index < indices.length ) {
				indices[index] = indices[index-1] + nums[index-1] + 1;

				indices[index] = findValidIndex( indices[index],
						maxes[index], nums[index], arr );

				index++;
			}
		}

		// iterate over all END
		if( testIndices(arr, indices, nums) )
			return true;

		return false;
	}

	/*
	 *  If the range of the current tile is invalid,
	 *  		move the block one tile forward.
	 *  This until the block reaches its max index.
	 *  (If the index is maxed then it wont matter if the range is
	 *  	invalid because the loop will end very quickly)
	 */
	private static int findValidIndex(int index, int max, int length, Tile[] arr) {
		while( index + 1 < max &&
				!validRange(arr, index, index+length) )
			index++;

		return index;
	}

	// end is exclusive
	private static boolean validRange(Tile[] arr, int start, int end) {
		if( start >= 0 && start < arr.length &&
				end >= 0 && end <= arr.length &&
				(start == 0 || arr[start -1] != Tile.FILL )
				&& ( end-1 == arr.length-1 || arr[end] != Tile.FILL) ) {
			for(int i = start; i < end; i++)
				if(	arr[i] == Tile.XED )
					return false;
			return true;
		}

		return false;
	}

	private static boolean testIndices(Tile[] arr, int[] indices, int[] nums ){
		Tile[] temp = arr.clone();

		for(int i = 0; i < indices.length; i++)
			for(int j = 0; j < nums[i]; j++)
				if( j + indices[i] >= arr.length ||
				temp[ j + indices[i] ] == Tile.XED ) {
					return false;
				}
				else
					temp[ j + indices[i] ] = Tile.FILL;

		return lineSolved( temp, nums );
	}

	private static boolean lineSolved (Tile[] arr, int[] solution) {
		// This array represents the number of parsed blocks.
		// The length of this array is set to the correct number of blocks.
		// If, while parsing, the program finds that there are more blocks than
		//		indices, the program will return false.
		int[] parsed = new int[solution.length];

		int index = 0;

		// this code parses the ID of the parameter Tile array
		for( int i = 0; i < arr.length; i ++ ) {
			// if on a block, add 1 to the current block
			if( arr[i] == Tile.FILL )
				// check if the block count is valid
				if( index < parsed.length )
					parsed[index]++;
			// if the block count is invalid, then there are too many blocks,
			// 		meaning the line is NOT solved
				else
					return false;
			// denotes stepping off a block
			else if ( i-1 >= 0 &&
					arr[i-1] == Tile.FILL )
				index++;
		}

		return Arrays.equals( solution, parsed );
	}

	private static int permutations(int numOfBlock, int buffer){
		if( numOfBlock < 0 || buffer < 0) return -1;

		// if buffer is 0, then there is only 1 solution, etc
		if( numOfBlock == 1) return buffer+1;

		// if buffer is 0, then there is only 1 solution
		if( buffer == 0 ) return 1;

		int b = permutations(numOfBlock, buffer-1);
		int a = permutations(numOfBlock-1, buffer);

		if( a > Integer.MAX_VALUE - b ) return -1;
		else return a + b;
	}

	@Override
	public String toString() {
		return "Steps:\t"+steps+"\n"+n; }

	private static class Coord implements Comparable<Object>{
		private int x, y;
		private int count = -1;

		public Coord(int x, int y) {
			this.x = x; this.y = y; }

		public Coord(int x, int y, int count) {
			this.x = x; this.y = y;
			this.count = count; }

		public int getX() { return x; }
		public int getY() { return y; }

		@Override
		public String toString() {
			return "(" + x + " , " + y +
					( count >= 0 ?" [" + count + "] ":"")
					+ ")"; }

		@Override
		public int compareTo(Object arg0) {
			return (arg0 instanceof Coord)? count - ((Coord) arg0).count : 0; }

		@Override
		public boolean equals(Object arg0) {
			return (arg0 instanceof Coord)
					&& ((Coord) arg0).x == x
					&& ( (Coord) arg0 ).y == y; }
	}

}
