
/*
* A program to take make a text file of motor operations 
* for each of the motors contained in the file given. The program stops when it
*  writes the text file. 
*  
*  for CMPRE 212, by Garrett Richardson, Student Number: 10170498
*/
import java.io.*;

public class Assn2_14gsr2 {

	private static String createFile = "motorData.txt"; // Names the text file
														// that the program will
														// create

	// This method checks to see if a motor's current ran too high and it
	// also calculates the average current of the motor when it is on
	public static void getAverage(double sum, int startTime, int endTime, int Motor, int ctr, boolean Exceed) {
		// If current is greater than 8 at any second
		// it will print to the text file
		if (Exceed) {
			getFile("***CURRENT EXCEEDED!", true);
		} // end if
		double avg = Math.round((sum / (ctr)) * 1000.0d) / 1000d; // calculates
																	// average
																	// current
		getFile("Ran " + avg + " amps, starting at " + startTime + " seconds, to " + endTime + "\n", true);
	} // end method

	// This method will overwrite a file if the file already exists
	public static void overWrite() {
		File file = new File(createFile); // creates file
		// checks if a file already exists
		if (file.exists()) {
			getFile("", false);
			System.out.println("File already exists, overwritten previous file");
		} // end if
	} // end method

	// this method creates a text file
	public static void getFile(String text, boolean check) {

		File file = new File(createFile); //
		// checks if file does not exist
		if (!file.exists()) {
			try {
				file.createNewFile(); // creates file
				System.out.println("File Created");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} // end if
		try (FileWriter writer = new FileWriter(file, check);) {
			BufferedWriter buffer = new BufferedWriter(writer);
			PrintWriter fileWriter = new PrintWriter(buffer);
			fileWriter.println(text);
			fileWriter.close();
		} catch (IOException e) {

		}
	} // end method

	// This method runs through the data of each motor at for each second
	public static void getData(double[][] ArrayVals) {

		double sum = 0; // holds the sum of the current when a motor is on
		int startTime = 0, endTime = 0; // holds the start and end times of the
										// motor
		int sec = 0; // holds the time the motor was on for in seconds
		// loop for each motor
		for (int i = 0; i < 7; i++) {
			int num = i + 1;
			Boolean On = false; // holds boolean to check if motor ran
			Boolean Exceed = false; // holds boolean to check if current
									// exceeded
			getFile("Motor: " + num + "\n", true);
			// loop for each second
			for (int row = 0; row < 1000; row++) {
				Exceed = false; // resets exceed for each second
				// checks if current was > 8
				if (ArrayVals[row][i] > 8) {
					Exceed = true;
				} // end if
					// checks to see if motor is on
				if (ArrayVals[row][i] >= 1) {
					sum += ArrayVals[row][i]; // sums each second the motor is
												// on
					On = true; // shows motor ran
					sec++; // adds to the seconds
					// marks start time
					if (ArrayVals[row - 1][i] < 1) {
						startTime = row;
						// end nested if
					} // end if
						// checks to see if motor is still on for the next
						// second
					if (ArrayVals[row + 1][i] < 1) {
						endTime = row;
						getAverage(sum, startTime, endTime, i, sec, Exceed);
						sum = 0; // resets sum back to 0
						sec = 0; // resets seconds back to 0
					} // end if
				} // end if
			} // end for loop
				// checks to see if a motor was on
			if (!On) {
				getFile("Did not run! \n", true);
			} // end if
		} // end for loop
	}// end method

	// this method takes a 1-D string and turns it into a 2-D array
	public static double[][] LoadData(String csvFile) {

		String line = ""; // how to format csv
		String csvSplitBy = ","; // how to format csv
		String[] motorData = line.split(csvSplitBy); // splits up commas
		double[][] ArrayVals = new double[1000][7]; // holds 2-D array of size
													// 1000 rows by 7 coloumns
		Double Obj; // holds double array from string

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			int j = 0; // holds number of rows
			// while reads line as a string
			while ((line = br.readLine()) != null) {
				// use comma as separator
				motorData = line.split(csvSplitBy); // splits up commas
				// loops through each motor
				for (int i = 1; i < motorData.length; i++) {
					Obj = Double.parseDouble(motorData[i]); // changes string to
															// double
					ArrayVals[j][i - 1] = Obj;
				} // end for loop
				j++; // increment row

			} // end while loop
		} catch (IOException e) {
			e.printStackTrace();
		}
		getFile("Check Data, Motor 2, after 2 seconds: " + ArrayVals[1][1] + "\n", true);
		return ArrayVals;
	} // end method

	public static void main(String[] args) {
		// checks to see if file already exists
		overWrite();
		// holds motor data given
		String csvFile = "Logger.csv";
		// creates a 2-D array from data given
		double[][] ArrayVals = LoadData(csvFile);

		getFile("Motor Use Summary: \n", true);
		// runs through the data of the 2-D array
		getData(ArrayVals);
	} // end main method

}