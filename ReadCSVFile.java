package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.opencsv.CSVReader;

public class ReadCSVFile {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		//Scanner scan = null;
		//String csvRead = "Telemetry_00010.csv";

		try {
			CSVReader reader = new CSVReader(new FileReader("Telemetry_00010.csv"));
			String[] column = null;
			List<Parameters> paramValues = new ArrayList<Parameters>();
			while ((column = reader.readNext()) != null) {
				Parameters params = new Parameters(column[0], column[1], column[2], column[3], column[4], column[5],
						column[6], column[7], column[8], column[9], column[10], column[11], column[12], column[13],
						column[14], column[15], column[16], column[17], column[18], column[19], column[20], column[21]);

				System.out.println(column[0] + "    " + column[1] + "     " + column[2] + "     " + column[3] + "     "
						+ column[4] + "    " + column[5] + "     " + column[6] + "     " + column[7] + "     "
						+ column[8] + "     " + column[9] + "     " + column[10] + "     " + column[11] + "     "
						+ column[12] + "     " + column[13] + "     " + column[14] + "     " + column[15] + "     "
						+ column[16] + "     " + column[17] + "     " + column[18] + "     " + column[19] + "     "
						+ column[20] + "     " + column[21]);
				paramValues.add(params);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
