
public class Street {
	public int fromInter;
	public int toInter;
	public String name;
	public int travelTime;
	
	public int usageWithoutLight;
	
	public int startCars;
	
	public Street(int a, int b, String name, int time) {
		this.fromInter = a;
		this.toInter = b;
		this.name = name;
		this.travelTime = time;
		usageWithoutLight = 0;
	}
	
	public void incrUsage() {
		usageWithoutLight++;
	}
	
	public void decrUsage() {
		usageWithoutLight--;
	}
	
	public void inctStart() {
		startCars++;
	}
	
	public void decrStart() {
		startCars--;
	}
}
