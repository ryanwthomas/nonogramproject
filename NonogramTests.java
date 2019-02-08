package NonogramProject5;
import static org.junit.Assert.*;

import NonogramProject5.Nonogram.DimensionException;
import NonogramProject5.Nonogram.Tile;

import org.junit.Test;

public class NonogramTests {

	@Test
	public void lengthTest() throws DimensionException, Exception {
		
		String str =
				//"Normal\\eggplant";
				"Testing\\helmet";

		Nonogram n = new Nonogram(
				NonogramDriver.textTo2DArray(str+" x.txt"),
				NonogramDriver.textTo2DArray(str+" y.txt")
				
				);

		Tile[][] nonogramCopy = 
			{{ Tile.XED  ,   Tile.UNKN ,   Tile.UNKN ,   Tile.XED  ,   Tile.XED  ,   Tile.XED  ,   Tile.XED  ,   Tile.FILL ,   Tile.XED  ,   Tile.XED  ,   Tile.XED  ,   Tile.XED  ,   Tile.UNKN ,   Tile.UNKN ,   Tile.XED  } ,
					{ Tile.XED  ,   Tile.FILL ,   Tile.FILL ,   Tile.XED  ,   Tile.XED  ,   Tile.XED  ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.XED  ,   Tile.XED  ,   Tile.XED  ,   Tile.FILL ,   Tile.FILL ,   Tile.XED  } ,
					{ Tile.UNKN ,   Tile.FILL ,   Tile.UNKN ,   Tile.XED  ,   Tile.XED  ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.XED  ,   Tile.XED  ,   Tile.UNKN ,   Tile.FILL ,   Tile.UNKN } ,
					{ Tile.FILL ,   Tile.FILL ,   Tile.XED  ,   Tile.XED  ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.XED  ,   Tile.XED  ,   Tile.FILL ,   Tile.FILL } ,
					{ Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL } ,
					{ Tile.UNKN ,   Tile.UNKN ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.UNKN ,   Tile.UNKN } ,
					{ Tile.XED  ,   Tile.XED  ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.XED  ,   Tile.XED  } ,
					{ Tile.XED  ,   Tile.XED  ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.XED  ,   Tile.XED  } ,
					{ Tile.XED  ,   Tile.XED  ,   Tile.FILL ,   Tile.XED  ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.XED  ,   Tile.FILL ,   Tile.XED  ,   Tile.XED  } ,
					{ Tile.XED  ,   Tile.FILL ,   Tile.FILL ,   Tile.XED  ,   Tile.XED  ,   Tile.XED  ,   Tile.XED  ,   Tile.FILL ,   Tile.XED  ,   Tile.XED  ,   Tile.XED  ,   Tile.XED  ,   Tile.FILL ,   Tile.FILL ,   Tile.XED  } ,
					{ Tile.XED  ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.XED  ,   Tile.XED  ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.XED  ,   Tile.XED  ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.XED  } ,
					{ Tile.XED  ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.XED  } ,
					{ Tile.XED  ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.XED  } ,
					{ Tile.XED  ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.XED  ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.XED  } ,
					{ Tile.XED  ,   Tile.XED  ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.XED  ,   Tile.XED  ,   Tile.XED  ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.FILL ,   Tile.XED  ,   Tile.XED  } ,};

		for(int i = 0; i < nonogramCopy.length; i++ ) {
			n.setRow(nonogramCopy[i], i);
		}

		System.out.println( n );

		System.out.println();
		
		n.setTileOverride(0, 5, Tile.FILL);
		
		System.out.println( n );

		


	}


}
