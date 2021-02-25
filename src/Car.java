import java.util.ArrayList;

public class Car {
	public int streetNum;
	public ArrayList<Street> streetsToVisit;
	
	public int timeToDrive;
	
	public Car(int num, ArrayList<Street> streets) {
		streetsToVisit = streets;
		streetNum = num;
	}
}
