package NonogramProject5;

import java.io.File;
import java.util.Scanner;

public class NonogramDriver {

	static final boolean printNonogram = false;
	static final int pixelWidth = 20, borderWidth = 0;
	
	static final boolean debug = false;
	
	
	static int mode = 0;

	public static void main(String[] args) throws Exception {

		Scanner userInput = new Scanner(System.in);

		System.out.println( "Normal (1), testing (2), DR1 Sprites (3),\n"
				+"or SDR2 Sprites (4)?" );
		mode = userInput.nextInt();

		if( mode > 4 || mode < 0 ) {
			System.out.println( "Enter 1 for Normal; 2 for testing;\n"
					+" 3 for Dangan Ronpa: Trigger Happy Havoc sprites,\n"
					+"or 4 for Super Dangan Ronpa 2: Goodbye Despair sprites?" );
			mode = userInput.nextInt();
		}

		String[] files;

		if( mode == 1 ) {
			String[] temp = {
					"eggplant",
					"eggplant mirrored",
					"camel", "deer", "elephant",
					"rabbit",
					"snake", "whale",
					"couple", "jester",
					"farmer",
					"castle", "kanji",
					"soul eater",
					"avengers",
			};
			files = temp;
		}else if ( mode == 2 ) {
			String[] temp = {

					"no solution 1", "no solution 2", 
					"multiple solutions 1", "pattern",
					"helmet", "sans",


					"gaster",
					"ice cream cone",
					"maryland flag",
					"peridot",
					"qr code",
					"frank"

					/*
					"skull",
					"leaf",
					"jack skellington",
					"kirby pose",
					"kirby float",
					"annoying dog",
					 */
			};
			files = temp;
		}
		else if ( mode == 3 ) {
			String[] temp = {
					"monobear",				// 54%

					"celestia ludenberg",	// 100%
					"sayaka maizono",		// 100%
					"touko fukawa",			// 100%
					"yasuhiro hagakure",	// 100%


					"aoi asahina",			// 89%
					"kyoko kirigiri",		// 73%
					"mondo owada",			// 63%
					"chihiro fujisaki",		// 54%
					"kiyotaka ishimaru",	// 21%
					"byakuya togami",		// 11%
					"makoto naegi",			//  4.7%
					"leon kuwata",			//  2.2%
					"hifumi yamada",		//  0.8%
					"sakura ogami",			//  0.2%
					"junko enoshima",		//  0.0%
			};
			files = temp;
		}
		else {
			String[] temp = {
					"monomi",

					"mikan tsumiki",		// 100%
					"nekomaru nidai",		// 100%
					"akane owari",			// 100%

					"ibuki mioda",		 	// 99%
					"kazuichi soda",	 	// 95%
					"peko pekoyama",		// 91%

					"sonia nevermind",		// 48%
					"gundham tanaka",		// 43%
					"fuyuhiko kuzuryu",	 	// 23%
					"hajime hinata",		// 16%
					"teruteru hanamura",	// 15%

					"mahiru koizumi",		// 1.7%
					"chiaki nanami",		// 1.5%
					"imposter",		 		// 0.8%
					"nagito komaeda",		// 0.0%
					"hiyoko saionji",		// 0.0%

			};
			files = temp;
		}

		System.out.println("Enter ID of nonogram to solve.");

		// print out all the file names and IDs
		for(int i = 0; i < files.length; i++)
			System.out.println( (i+1)+": "+files[i] );
		System.out.println();

		int puzzleSelect = userInput.nextInt();

		// verify that the ID is valid
		while( puzzleSelect <= 0 || puzzleSelect > files.length ) {
			System.out.println("Invalid ID. Pick ID between 1 and "+files.length );
			puzzleSelect = userInput.nextInt();
		}

		userInput.close();

		// convert the text file into a 2D matrix
		Nonogram n = new Nonogram( 
				textTo2DArray( files[puzzleSelect-1]+" x.txt" ),
				textTo2DArray( files[puzzleSelect-1]+" y.txt" )
				);

		n.name = files[puzzleSelect-1];

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
	public static int[][] textTo2DArray( String filename ) throws Exception{
		String subfolder = "";

		switch( mode ) {
		case 1: subfolder = "Normal\\"; break;
		case 2: subfolder = "Testing\\"; break;
		case 3: subfolder = "DRSprites\\"; break;
		case 4: subfolder = "SDR2Sprites\\"; break;
		}


		File myText = new File(
				"C:\\Users\\ryanw\\eclipse-workspace\\workspaceSepThird"
						+ "\\src\\TextFiles\\"
						+ subfolder
						+ filename
				);

		//File myText = new File( filename );
		Scanner sc = new Scanner( myText );

		int length = 0;

		// find the how many rows/columns
		while( sc.hasNextLine( ) ) {
			sc.nextLine( );
			length++;
		}

		// reset the scanner
		sc.close( );
		sc = new Scanner( myText );

		int[][] ans = new int[length][];

		// iterate through each row/col
		for( int i = 0; i < length; i++ ) {
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

			// set the row/col to the array of ints
			ans[i] = nums;
		}

		sc.close( );

		return ans;
	}


}
