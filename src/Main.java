import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javafx.util.Pair;

public class Main {
	private static final String INPUT_FILE_NAME = "f.txt";
	private static final String OUTPUT_FILE_NAME = "Solution.txt";
	private static final int SPLITTED_TIME = 2;

	private static int duration;
	private static int intersectNum;
	private static int streetNum;
	private static int carNum;
	private static int bonusPoints;

	private static ArrayList<Street> streets;
	private static ArrayList<Car> cars;
	private static HashMap<Integer, ArrayList<Street>> intersectionMap;

	public static void main(String[] args) {
		// Read data
		System.out.println("Parsing data");
		readData(INPUT_FILE_NAME);

		// Analize data
		System.out.println("Analize data");

		ArrayList<Solution> solutions = new ArrayList<Solution>();
		Solution solution;
		int id;
		ArrayList<Pair<String, Integer>> result;
		Pair<String, Integer> pairResult;
		ArrayList<Street> streets;
		int timeSum;
		int lightTime;
		for (Map.Entry entry : intersectionMap.entrySet()) {
			id = (int) entry.getKey();
			streets = (ArrayList<Street>) entry.getValue();

			result = new ArrayList<Pair<String, Integer>>();
			
			// Order streets in the right order
			Collections.sort(streets, new Comparator<Street>() {
			    @Override
			    public int compare(Street lhs, Street rhs) {
			        return lhs.startCars > rhs.startCars ? -1 : (lhs.startCars < rhs.startCars) ? 1 : 0;
			    }
			});
			
			// Find sum value of all streets
			timeSum = 0;
			for (Street street : streets) {
				timeSum += street.usageWithoutLight;
			}
			
			for (Street street : streets) {
				if (street.usageWithoutLight != 0) {
					lightTime = (int)(street.usageWithoutLight / timeSum) * SPLITTED_TIME;
					//pairResult = new Pair<String, Integer>(street.name, street.usageWithoutLight);
					if (lightTime != 0) {
						pairResult = new Pair<String, Integer>(street.name, lightTime);
					}else {
						pairResult = new Pair<String, Integer>(street.name, 1);
					}
					
					result.add(pairResult);
				}
			}

			if (result.size() != 0) {
				solution = new Solution(id, result);
				solutions.add(solution);
			}
		}

		// Write result
		System.out.println("Writing result");
		writeData("s" + INPUT_FILE_NAME, solutions);
	}

	private static void readData(String path) {

		File file = new File(path);
		Scanner in = null;
		try {
			in = new Scanner(file);
			in.useDelimiter("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		String firstDataLine = in.next();
		String[] firstData = firstDataLine.split("\\s+");

		System.out.println("Parsing main data");

		duration = Integer.parseInt(firstData[0]);
		intersectNum = Integer.parseInt(firstData[1]);
		streetNum = Integer.parseInt(firstData[2]);
		carNum = Integer.parseInt(firstData[3]);
		bonusPoints = Integer.parseInt(firstData[4]);

		System.out.println("Parsing streets");

		int interID1;
		int interID2;
		int travelTime;
		String streetName;
		String parseString;
		String[] streetStrings;
		Street newStreet = null;
		streets = new ArrayList<Street>();

		intersectionMap = new HashMap<Integer, ArrayList<Street>>();
		ArrayList<Street> hashStreets;
		for (int i = 0; i < streetNum; i++) {
			parseString = in.next();
			streetStrings = parseString.split("\\s+");
			interID1 = Integer.parseInt(streetStrings[0]);
			interID2 = Integer.parseInt(streetStrings[1]);
			streetName = streetStrings[2];
			travelTime = Integer.parseInt(streetStrings[3]);
			newStreet = new Street(interID1, interID2, streetName, travelTime);
			streets.add(newStreet);

			// Also fill hash map with data (intersect with input street)
			if (intersectionMap.containsKey(interID2)) {
				intersectionMap.get(interID2).add(newStreet);
			} else {
				hashStreets = new ArrayList<Street>();
				hashStreets.add(newStreet);
				intersectionMap.put(interID2, hashStreets);
			}
		}

		System.out.println("Parsing cars");

		int streetNum;
		//ArrayList<String> streetVisiting;
		ArrayList<Street> streetVisitinObjs;
		String[] carStrings;
		Car newCar = null;
		cars = new ArrayList<Car>();
		Street streetToAdd;
		int minTime;
		for (int i = 0; i < carNum; i++) {
			minTime = 0;
			//streetVisiting = new ArrayList<String>();
			streetVisitinObjs = new ArrayList<Street>();
			parseString = in.next();
			carStrings = parseString.split("\\s+");
			streetNum = Integer.parseInt(carStrings[0]);

			for (int j = 1; j < carStrings.length; j++) {
				//streetVisiting.add(carStrings[j]);
				streetToAdd = findStreetWithName(carStrings[j]);
				if (j < carStrings.length - 1) {
					streetToAdd.incrUsage();
				}
				streetVisitinObjs.add(streetToAdd);
				minTime += streetToAdd.travelTime;
			}
			
			if (minTime > duration) {
				for (Street street : streetVisitinObjs) {
					street.decrUsage();
				}
			}else {
				newCar = new Car(streetNum, streetVisitinObjs);
				newCar.timeToDrive = minTime;
				
				//Test
				streetVisitinObjs.get(0).inctStart();
				
				cars.add(newCar);
			}
		}

		in.close();
	}

	private static void writeData(String path, ArrayList<Solution> solutions) {
		File file = new File(path);
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);

			// Output number or intersects
			writer.write(solutions.size() + "\n");

			// Main output
			for (Solution solution : solutions) {
				if (solution.result.size() != 0) {
					writer.write(solution.id + "\n");
					writer.write(solution.result.size() + "\n");
				}

				for (Pair<String, Integer> element : solution.result) {
					writer.write(element.getKey() + " " + element.getValue() + "\n");
				}
			}

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Street findStreetWithName(String name) {
		for (Street street : streets) {
			// System.out.println(name + " " + street.name);
			if (name.equals(street.name)) {
				return street;
			}
		}
		return null;
	}
}
