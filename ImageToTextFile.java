// Code written by Ryan WH Thomas
// Last updated: 2/9/2019

package nonogramProjectv1;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import nonogramProjectv1.Nonogram.Tile;


public class ImageToTextFile {

	private static boolean invert = false;

	public static void main(String[] args) throws IOException {

		String filepath = FileFinder.getImageFile();

		String filename =  filepath.substring( filepath.lastIndexOf("\\"),
				filepath.length()-4 );
		
		BufferedImage image =
				ImageIO.read( new File( filepath ) );

		int width = image.getWidth();
		int height = image.getHeight();

		Tile[][] head = new Tile[height][width];

		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {

				//System.out.println( image.getRGB(col, row) );
				int x = image.getRGB(col, row);
				boolean temp = invert? x == 1: x !=-16777216;

				head[row][col] = temp ? Tile.XED: Tile.FILL;
			}
		}

		String baseFolder = System.getProperty("user.dir")+"\\textfiles";
		
		PrintWriter writer = new PrintWriter(baseFolder+
				"\\"+filename+".txt", "UTF-8");
		
		writer.println("X" );
		for(int i = 0; i < width; i++) {
			Tile[] temp = new Tile[height];
			for(int j = 0; j < height; j++)
				temp[j] = head[j][i];

			writer.println( parseID( temp ) );
		}


		writer.println("Y" );
		for(int i = 0; i < height; i++)
			writer.println( parseID( head[i] ) );

		writer.close();
		System.out.println( "File successfully written." );
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


}