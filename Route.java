/**
 * Klasa reprezentująca pojedyńczą trasę - reprezentacja ścieżkowa tzn. że z miejsca znajdującego się na 0 pozyjcji przechodzimy do miasta
 * znajdującego się na 1 pozycji w tablicy, z 1 do znajdującego się na 2 itd...
 * @author Klaudia
 *
 */
public class Route {
	
	
	// ========================= POLA KLASY =========================
	/**
	 * Tablica miast - reprezentująca pojedyńczą trasę - jeden chromosom. 
	 */
	private City[] route;
	
	// ========================= KONSTRUKTORY =========================
	public Route(City[] route) {
		this.route = new City[route.length];
		this.route = route;
	}
	// ========================= GETTERY I SETTERY =========================
	public City[] getRoute() {
		return route;
	}
	
	public void setRoute(City[] route) {
		if (route.length==this.route.length)
			this.route = route;
	}
	
	//  ========================= METODY KLASY =========================
	public double weightRoute() {
		double weight = 0;
		
		for(int i=0; i<route.length - 1; i++) {
			weight += City.distance(route[i], route[i + 1]);
		}
		
		weight += City.distance(route[0], route[route.length - 1]);
		
		return weight;
	}
	
	public String toString() {
		String r = new String();
		for(int i=0; i<route.length; i++) {
			r += route[i].toString();
		}
		
		return r;
	}
	

}
