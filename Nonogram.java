// Code written by Ryan WH Thomas
// Last updated: 2/9/2019

package nonogramProjectv1;

import java.util.Arrays;

public class Nonogram {

	/*
	// A nonogram is an 2D array of Tiles (which may be filled or Xed out)
	//		with an int[] for each row and column.
	// These int[] represent the final state of the row or column.
	/// E.g. if the int[] is {3, 1, 2} in a 10 wide row (or column),
	/// 	the row may look like any of the following:
	///		[FFF F FF  ]
	///		[  FFF F FF]
	///		[FFF   F FF]
	///		[FFF  F  FF]

	/// These would NOT be acceptable
	///		[FFFFFF    ]	this would be {7} 
	///		[F FF FFF  ]	this would be {1, 2, 3}
	///		[FF F FFF  ]	this would be {2, 1, 3}
	 */

	// A complete nonogram is comprised of all filled and Xed out tiles.

	private int dim_x, dim_y;
	private int[][] x_ids;
	private int[][] y_ids;
	public String name;

	enum Tile {
		XED,	// the tile cannot be filled (represented by a space)
		FILL,	// the tile is filled (represented by a +)
		UNKN	// the tile may or may not be filled (represented by a ?)
	}

	private Tile[][] nono;

	public Nonogram( Nonogram n ) {
		x_ids = n.x_ids.clone();
		y_ids = n.y_ids.clone();

		dim_x = x_ids.length;
		dim_y = y_ids.length;

		name = n.name + " copy";

		nono = new Tile[dim_y][dim_x];

		for(int y = 0; y < dim_y; y++)
			for(int x = 0; x < dim_x; x++)
				nono[y][x]= n.nono[y][x];		
	}

	public Nonogram( int[][] x_ids, int[][]y_ids ) throws DimensionException{
		this.x_ids = x_ids.clone();
		this.y_ids = y_ids.clone();

		dim_x = x_ids.length;
		dim_y = y_ids.length;

		// Check that no IDs too big for the nonogram's respective row or column.
		for(int x = 0; x < dim_x; x++)
			if( tileLength( x_ids[x] ) > dim_y )
				throw new DimensionException("Column "+(x+1)+"has ID is too big for column.");

		System.out.println( "Alpha\t"+(y_ids==null));
		
		for(int y = 0; y < dim_y; y++)
			if( tileLength( y_ids[y] ) > dim_x )
				throw new DimensionException("Row "+(y+1)+" has ID is too big for row.");

		setNonogram();
	}

	private void setNonogram() {
		nono = new Tile[dim_y][dim_x];

		for(int y = 0; y < dim_y; y++)
			for(int x = 0; x < dim_x; x++)
				nono[y][x]= Tile.UNKN;		
	}

	@Override
	public String toString() {
		String ans = "";

		for(Tile[] arrY : nono) {
			for(Tile arrX : arrY) {
				switch( arrX ) {
				case UNKN: ans += "="; break;
				case FILL: ans += "X"; break;
				case XED: ans += " "; break;
				//case XED: ans += "o"; break;
				}
				ans += " ";
			}

			ans = ans.substring(0, ans.length()-1) + "\n";
		}

		return ans.substring(0, ans.length()-1);
	}

	public int getWidth() {
		return dim_x;
	}

	public int getHeight() {
		return dim_y;
	}

	// return a independent row
	public Tile[] getRow(int y) {
		return nono[y].clone();
	}

	// return a independent row
	public Tile[] getColumn(int x) {
		Tile[] t = new Tile[ dim_y ];

		for(int i = 0; i < dim_y; i++)
			t[i] = nono[i][x];

		return t;
	}

	public Tile getTile(int x, int y) {
		return nono[y][x];
	}

	public void setTile(int x, int y, Tile t) throws CombinationException {
		nono[y][x] = combine( t, nono[y][x] );
	}
	
	public void setTileOverride(int x, int y, Tile t) {
			nono[y][x] = t;
	}

	public int[] getRowID(int y) {
		return y_ids[y];
	}

	public int[] getColumnID(int x) {
		return x_ids[x];
	}

	// set nonogram row
	public void setRow( Tile[] arr, int y) throws CombinationException, DimensionException {
		if( arr.length != nono[y].length )
			throw new DimensionException("Dimensions are incorrect (setRow).");

		for(int i = 0; i < arr.length; i++) {
			nono[y][i] = combine( arr[i], nono[y][i] );		
		}
	}

	// set nonogram column
	public void setColumn(Tile[] arr, int x) throws CombinationException, DimensionException {
		if( arr.length != nono.length )
			throw new DimensionException("Dimensions are incorrect (setColumn).");

		for(int i = 0; i < arr.length; i++) {
			nono[i][x] = combine( arr[i], nono[i][x] );		
		}
	}

	/*
	 * This method finds the minimum number of tiles a row of series of blocks
	 * 		can fit into.
	 * The formula is the sum array plus the length of the array minus 1.
	 * This is because between each block there needs to be an empty tile.
	 * The number of empty tiles is equal to the number of blocks minus 1.
	 * */
	private static int tileLength(int[] num){
		
		if( num != null )
		System.out.println( Arrays.toString( num ) );
		else
			System.out.println( "NULL" );
		
		
		int sum = 0;
		for(int x : num)
			sum += x; // add the width of the num
		// plus one for a space in between this block and the next

		return sum + num.length -1; // remove the extra space added after the last block
	}

	public static Tile combine(Tile a, Tile b) throws CombinationException {
		// if both tiles are the same, return that type of tile
		if( a == b )
			return a;

		// if either tile is unknown, return the other tile because
		/// 	at this point the tiles aren't equal
		if( a == Tile.UNKN )
			return b;
		if( b == Tile.UNKN )
			return a;

		// At this point the only combination is FILL-XED.
		/// 	This is a contradiction, so throw an exception.
		throw new CombinationException("Combination error.");
	}

	public static class DimensionException extends Exception{
		private static final long serialVersionUID = 9054996101872941844L;
		public DimensionException() { super(); }
		public DimensionException(String s) { super(s); }
	}

	public static class CombinationException extends Exception{
		private static final long serialVersionUID = 6866781922332665323L;
		public CombinationException() { super(); }
		public CombinationException(String s) { super(s); }
	}


}
