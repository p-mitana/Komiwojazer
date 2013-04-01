import java.util.Random;

/**
 * Klasa reprezentująca Scramble Mutation; 
 * Mutacja:
 * Wybierz losowo podzbiór k pozycji w chromosomie.
 * Przestaw losowo wierzchołki przechowywane na tych pozycjach.
 * Implementacja
 * Wylosuj numery pozycji i1,i2,...,ik
 * Dla każdego wylosowanego numeru ij zamień wierzchołek z innym
 *	losowo wybranym spośród pozycji i1,i2,...,ik.
 *Liczba k wylosowanych pozycji może być stała, albo zmniejszana wraz z postępem algorytmu.
 * 
 * @author Klaudia
 *
 */

// stałe k
public class ScrambleMutation {
	public Route scramleMutation(Route r) {
		
		Random rand = new Random();
		Route route = r;
		
		int k = 5;
		int[] position = new int[k];
		for (int i=0; i<position.length; i++) {
			position[i] = rand.nextInt(r.getRoute().length);
		}
		
		for (int i=0; i<position.length; i++) {
			System.out.println("Pozycje: " + position[i]);
		}
		
		for (int i=0; i<position.length; i++) {
			
			int temp = rand.nextInt(position.length);
			City c = route.getRoute()[position[i]];
			route.getRoute()[position[i]] = route.getRoute()[position[temp]];
			route.getRoute()[position[temp]] = c;
			
		}
		
		return route;
	}
	
	
}
