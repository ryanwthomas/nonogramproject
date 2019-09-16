// Code written by Ryan WH Thomas
// Last updated: 2/8/2019

package nonogramProjectv1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import nonogramProjectv1.Nonogram.Tile;

public class NonogramDrawer {

	public static void saveImage(Nonogram n, String name,
			int tempX, int tempY, Color c) {
		new SavePaint(n, name, tempX, tempY, c);
	}

	public static void saveImage(Nonogram n, String name) {
		new SavePaint(n, name, -1, -1, Color.RED);
	}

	private static class SavePaint extends JPanel{

		final int origin = 10;
		final static int squareLength = NonogramDriver.pixelWidth;
		final static int space = NonogramDriver.borderWidth;

		final boolean nameFirst = false;
		
		static int count = 1;

		Nonogram myNonogram;
		int width, height;

		int tempX, tempY;
		Color c = Color.RED;

		public SavePaint(Nonogram n, String name, int tempX, int tempY, Color c) {
			if( count > Math.pow(10, 5) )
				System.exit(0);
			
			myNonogram = n;
			this.width = myNonogram.getWidth();
			this.height = myNonogram.getHeight();

			this.tempX = tempX;
			this.tempY = tempY;
			this.c = c;

			JFrame frame = new JFrame(count+"");
			frame.add(this);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize( 
					19 +
					origin*2 + width*squareLength + (width-1)*space,
					48 + 
					origin*2 + height*squareLength + (height-1)*space);

			frame.setVisible(true);

			try{

				BufferedImage image = new BufferedImage(
						getWidth() + 9,
						getHeight() + 38,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics2D = image.createGraphics();
				frame.paint(graphics2D);	
				
				String curDir = System.getProperty("user.dir");

				ImageIO.write(image,"png", new File(curDir+"\\output\\"
						//+System.currentTimeMillis()
						+"nonogram_"
						+(nameFirst?name:"")
						+"_"+ String.format("%05d", count++)
						+(!nameFirst?("_"+name):"")
						+".png"
						));

				//frame.setVisible(false);
				frame.dispose();
			}
			catch(Exception exception){
				//code
			}
		}



		protected void paintComponent(Graphics g){
			g.setColor( Color.BLACK );

			for(int y = 0; y < height; y++)
				for(int x = 0; x < width; x++)
					if( y == tempY && x == tempX ) {
						g.setColor( c );
						g.fillRect(origin + x*(squareLength+space),
								origin + y*(squareLength+space),
								squareLength,squareLength);
					}
					else if( myNonogram.getTile(x, y) == Tile.FILL ) {
						g.setColor( Color.BLACK );
						g.fillRect(origin + x*(squareLength+space),
								origin + y*(squareLength+space),
								squareLength,squareLength);
					}
					else if( myNonogram.getTile(x,y) == Tile.XED ){
						g.setColor( Color.WHITE );
						g.fillRect(origin + x*(squareLength+space),
								origin + y*(squareLength+space),
								squareLength,squareLength);
					}
					else{
						g.setColor( Color.GRAY );
						g.fillRect(origin + x*(squareLength+space),
								origin + y*(squareLength+space),
								squareLength,squareLength);
					}

		}

	}
}

