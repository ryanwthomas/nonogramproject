// Code written by Ryan WH Thomas
// Last updated: 2/8/2019

package nonogramProjectv1;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class FileFinder {

	// keeps track of how many files are in a given folder
	private HashMap<String, Integer> folderToFiles = new HashMap<>();

	public static void main(String[] args) {

		System.out.println(  getTextFile() );

	}

	public static String getImageFile() {
		return (new FileFinder()).getFileLocation("images"); }

	public static String getTextFile() {
		return (new FileFinder()).getFileLocation("textfiles"); }


	private String getFileLocation(String subfolder){
		String baseFolder = System.getProperty("user.dir")+"\\";
		String filename = null;

		if( countFiles(subfolder) == 0) {
			throw new RuntimeException("Can not retrieve files because "
					+ "there no files in \""+subfolder+"\"."); }

		System.out.println();

		Scanner sc = new Scanner(System.in);

		boolean loopFinished = false;

		while( !loopFinished ) {
			System.out.println( "You are currently at "+subfolder+" ("+folderToFiles.get(subfolder)+")" );

			String[] filesArr = (new File( baseFolder + subfolder )).list(new FilenameFilter() {
				@Override
				public boolean accept(File current, String name) {
					return new File(current, name).isFile();
				}
			});

			String[] foldersArr = (new File( baseFolder + subfolder  )).list(new FilenameFilter() {
				@Override
				public boolean accept(File current, String name) {
					return new File(current, name).isDirectory();
				}
			});


			if( filesArr.length > 0 && foldersArr.length > 0) {
				// ask user if they want to view files
				System.out.println( "\nWould you like to see files in current location?" );

				String bravo = sc.nextLine().toLowerCase();
				if( bravo.equals("y") || bravo.contains("yes") || bravo.contains("yeah") || bravo.contains("sure") || bravo.contains("ok")) {
					System.out.println("\nThere are " + filesArr.length + " files." );
					System.out.println( formatArray( filesArr ) );
				}

				// ask user if they want to view folders
				System.out.println( "\nWould you like to see folders in current location?" );

				String charlie = sc.nextLine().toLowerCase();
				if( charlie.equals("y") || charlie.contains("yes") || charlie.contains("yeah") || charlie.contains("sure") || charlie.contains("ok")) {
					System.out.println("\nThere are " + foldersArr.length + " folders." );
					System.out.println( formatArray( foldersArr ) );
				}
			}

			Boolean selectedFile;
			if( foldersArr.length == 0 )
				selectedFile = true;
			else if( filesArr.length == 0)
				selectedFile = false;
			else
				selectedFile = null;

			while( selectedFile == null ) {
				System.out.println( "\nWould you like to select a FILES or enter a FOLDER?" );

				String alpha = sc.nextLine().toLowerCase();
				// chose file
				if( alpha.contains("file") || alpha.contains("files") || alpha.contains("select"))
					selectedFile = true;
				// enter folder
				else if ( alpha.contains("folder") || alpha.contains("enter") )
					selectedFile = false;
				// get new input
				else
					System.out.println( "Enter \"files\" to select a file.\nEnter \"folder\" to enter a folder." );
			}


			if( selectedFile ) {
				// ask user which files they want to select

				int index;

				if( filesArr.length > 1 ) {
					String toPrint = "";
					for(int i = 0; i < filesArr.length; i++) {
						toPrint += "("+(i+1)+")"+filesArr[i]+"\n";
					}

					System.out.println( toPrint );
					System.out.println( "Which file do you want to select?" );

					index = sc.nextInt()-1;


					while( 0 > index && index >= filesArr.length ) {
						System.out.println( "\nEnter a number between 1 and "+(filesArr.length)+"." );
						index = sc.nextInt()-1;
					}
				}else {
					System.out.println( "Only one file in subfolder. File chosen automatically." );
					index = 0;
				}

				filename = filesArr[index];
				System.out.println( "File \""+filename + "\" at "+subfolder+" was chosen.");
			}
			else{
				// ask user which folder they want to enter

				int index;

				if( foldersArr.length > 1 ) {
					String toPrint = "";
					for(int i = 0; i < foldersArr.length; i++) {
						toPrint += "("+(i+1)+")"+foldersArr[i]+"\n";
					}

					System.out.println( toPrint );
					System.out.println( "Which folder do you want to enter?" );

					index = sc.nextInt()-1;

					while( 0 > index && index >= foldersArr.length ) {
						System.out.println( "\nEnter a number between 1 and "+(foldersArr.length)+"." );
						index = sc.nextInt()-1;
					}
				}else {
					System.out.println( "Only one folder in subfolder. Folder chosen automatically." );
					index = 0;
				}

				subfolder += "\\"+foldersArr[index];

			}

			loopFinished |= selectedFile;
		}

		sc.close();

		return baseFolder+subfolder+"\\"+filename;
	}

	public static String formatArray(String[] arr) {
		return  Arrays.toString( arr ).replaceAll(", ", "\n").replace("[", "").
				replace("]", "");
	}


	// recursively count files in a folder and its subfolders; stores info in hashmap
	private int countFiles(String subfolder){
		String baseFolder = System.getProperty("user.dir")+"\\";

		String[] imageFiles = (new File( baseFolder + subfolder )).list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isFile();
			}
		});

		String[] imageFolders = (new File( baseFolder + subfolder  )).list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});

		int sum = imageFiles.length;

		for(String folder : imageFolders) {
			sum += countFiles( subfolder+"\\"+folder );
		}

		folderToFiles.put( subfolder, sum );

		return sum;
	}

}


