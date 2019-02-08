package NonogramProject5;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;
import NonogramProject5.Nonogram.Tile;


public class BinaryImageToBinary {

	private static boolean invert = false;

	public static void main(String[] args) throws IOException {

		Scanner sc = new Scanner(System.in);
		
		String temp = sc.nextLine();
		
		BufferedImage hugeImage =
				ImageIO.read(BinaryImageToBinary.class.getResource(
						temp+".png"));

		System.out.println("Testing convertTo2DUsingGetRGB:");
		for (int i = 9; i < 10; i++) {
			long startTime = System.nanoTime();
			int[][] result = convertTo2DUsingGetRGB(hugeImage);

		}

		System.out.println("");

	}

	private static int[][] convertTo2DUsingGetRGB(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int[][] result = new int[height][width];

		System.out.println( width );
		System.out.println( height );

		Tile[][] head = new Tile[height][width];

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {

				//System.out.println( image.getRGB(col, row) );
				int x = image.getRGB(col, row);
				boolean temp = invert? x == 1: x !=-16777216;
				
				head[row][col] = temp ? Tile.XED: Tile.FILL;
			}
		}

		System.out.println("Rows" );
		for(int i = 0; i < height; i++)
			System.out.println( parseID( head[i] ) );

		
		System.out.println();
		System.out.println("Columns" );
		for(int i = 0; i < width; i++) {
			Tile[] temp = new Tile[height];
			for(int j = 0; j < height; j++)
				temp[j] = head[j][i];
			
			System.out.println( parseID( temp ) );
		}
		
		

		int[] parsed = new int[width];
		int index = 0;

		return result;
	}

	private static String parseID(Tile[] arr) {
		ArrayList<Integer> arrL = new ArrayList<>();
		arrL.add(0);

		int index = 0;

		for( int i = 0; i < arr.length; i ++ ) {
			if( arr[i] == Tile.FILL )
				arrL.set(index, arrL.get(index)+1 );
			else if ( i-1 >= 0 &&
					arr[i-1] == Tile.FILL ) {
				index++;
				arrL.add(0);
			}
		}

		if( arrL.get(arrL.size()-1) == 0 )
			arrL.remove( arrL.size()-1 );

		String toReturn = arrL.toString();
		
		return toReturn.replaceAll(",", "").replaceAll("\\[", "").replaceAll("\\]","");
	}

	private static String toString(long nanoSecs) {
		int minutes    = (int) (nanoSecs / 60000000000.0);
		int seconds    = (int) (nanoSecs / 1000000000.0)  - (minutes * 60);
		int millisecs  = (int) ( ((nanoSecs / 1000000000.0) - (seconds + minutes * 60)) * 1000);


		if (minutes == 0 && seconds == 0)
			return millisecs + "ms";
		else if (minutes == 0 && millisecs == 0)
			return seconds + "s";
		else if (seconds == 0 && millisecs == 0)
			return minutes + "min";
		else if (minutes == 0)
			return seconds + "s " + millisecs + "ms";
		else if (seconds == 0)
			return minutes + "min " + millisecs + "ms";
		else if (millisecs == 0)
			return minutes + "min " + seconds + "s";

		return minutes + "min " + seconds + "s " + millisecs + "ms";
	}
}