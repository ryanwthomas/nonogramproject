// Code written by Ryan WH Thomas
// Last updated: 2/8/2019

package nonogramProjectv1;

import java.io.File;
import java.util.IllegalFormatException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NonogramDriver {

	static final boolean printNonogram = false;
	static final int pixelWidth = 20, borderWidth = 0;

	static final boolean debug = false;


	static int mode = 0;

	public static void main(String[] args) throws Exception {

		String filepath = FileFinder.getTextFile();

		System.out.println( filepath  );

		// convert the text file into a 2D matrix
		Nonogram n = textTo2DArray( filepath );


		String name =  filepath.substring( filepath.lastIndexOf("\\"),
				filepath.length()-4 );
		System.out.println( "A\t"+filepath );

		n.name = filepath;

		// print the empty nonogram
		//System.out.println( n.toString( ) +"\n" );

		NonogramSolver ns = new NonogramSolver(n);
		//System.out.println( n.toString( ) +"\n" );

		//long tStart = System.currentTimeMillis();
		long tStart = System.nanoTime();

		if( debug?true:false || ns.nonogramSolvable() ) {
			System.out.println( "Solvable" );

			// start timer
			tStart = System.nanoTime();
			// System.out.println( (System.currentTimeMillis()-tStart)/1000.0);

			while( ns.progressable ) {
				if( ns.step() )
				{}
				if( ns.debug )
					System.out.println( "\ttime (min):\t"+
							(System.nanoTime()-tStart)/(60.0*Math.pow(10, 9)) +"\n");
			}
			//	System.out.println( "\n"+ns );
			//System.out.println("Stepped");
		}
		else
			System.out.println( "Unsolvable" );

		if( ns.printNonogram ||  mode == 2)
			NonogramDrawer.saveImage(ns.getNonogram(), n.name+"_FIN");

		System.out.println( "\nTime (s):\t"+
				(System.nanoTime()-tStart)/Math.pow(10, 9) + " s");
		System.out.println( "\n"+ns );


		//	tileDescription( ns.getNonogram() );
	}

	public static void tileDescription(Nonogram n ) {
		String toReturn = "";

		String raw = n.toString();

		System.out.println("A");


		raw = raw.replace("X",  " Tile.FILL , ");

		System.out.println( raw +"\n");

		raw = raw.replace("=",  " Tile.UNKN , ");

		System.out.println( raw +"\n");

		raw = raw.replace("o", " Tile.XED  , ");

		System.out.println( raw +"\n");

		raw = raw.replace("\n", "}\n{");

		raw = "{" + raw + "}";

		raw = raw.replace(", }", "} ,");


		System.out.println( "{"+raw+"};" );
	}

	/*
	 * This methods converts a text file into a list of ids for a nonogram's
	 * 		rows or columns.
	 * The text file should be formatted with spaces in between each number and a new line
	 * 		to indicate a new row/column.
	 * 
	 * THIS METHOD CURRENTLY DOESNT HANDLE ROW/COL THAT ARE COMPLETELY EMPTY
	 */
	public static Nonogram textTo2DArray( String filepath ) throws Exception{
		File myText = new File( filepath );

		Scanner sc = new Scanner( myText );

		if( !sc.nextLine().equals("X") ) {
			sc.close();
			throw new Exception("Missing X at the begining of textfile.");
		}

		// find the how many rows/columns
		int lengthX = 0, lengthY = 0;
		boolean beforeX = true;

		while( sc.hasNextLine( ) ) {
			String line = sc.nextLine();

			String pattern = "[1-9](\\d)*( [1-9](\\d)*)*$";

			// Create a Pattern object
			Pattern r = Pattern.compile(pattern);

			// Now create matcher object.
			Matcher m = r.matcher(line);
			if (m.find( )) {
				if( beforeX )
					lengthX++;
				else
					lengthY++;
			}
			else {
				if( !line.equals("Y") ) {
					sc.close();
					throw new Exception("Malformation on line " +
							(1 + lengthX + lengthY + (beforeX?0:1) ) + ".\n"+line);
				}else {
					System.out.println( "Y is here"+line );
					beforeX = false;
				}
			}


		}

		if( beforeX ) {
			sc.close();
			throw new Exception("Missing Y at the middle of textfile.");
		}

		// reset the scanner
		sc.close( );
		sc = new Scanner( myText );

		int[][] x2dArray = new int[lengthX][];
		int[][] y2dArray = new int[lengthX][];

		// this is "X"
		sc.nextLine();

		System.out.println( "lengthX\t"+lengthX );

		// iterate through each col
		for( int i = 0; i < lengthX; i++ ) {
			// This method turns the line into a series of strings
			//		that will be converted into ints. 
			String[] strs = sc.nextLine( ).split( " " );
			int[] nums = new int[strs.length];

			// iterate through the list of strings
			for( int j = 0; j < strs.length; j++ ) {
				int temp = Integer.parseInt( strs[j] );

				// if the number is invalid, throw an error
				if( temp > 0 )
					nums[j] = temp;
				else {
					sc.close( );
					throw new Exception( "Text file contained a "
							+ "positive natural number:\t"+temp );
				}}

			// set the col to the array of ints
			x2dArray[i] = nums;
		}

		// this is "Y"
		sc.nextLine();

		// iterate through each row
		for( int i = 0; i < lengthY; i++ ) {
			// This method turns the line into a series of strings
			//		that will be converted into ints. 
			String[] strs = sc.nextLine( ).split( " " );
			int[] nums = new int[strs.length];

			// iterate through the list of strings
			for( int j = 0; j < strs.length; j++ ) {
				int temp = Integer.parseInt( strs[j] );

				// if the number is invalid, throw an error
				if( temp > 0 )
					nums[j] = temp;
				else {
					sc.close( );
					throw new Exception( "Text file contained a "
							+ "positive natural number:\t"+temp );
				}}

			// set the row to the array of ints
			y2dArray[i] = nums;
		}

		sc.close( );

		return new Nonogram( x2dArray, y2dArray );
	}


}
