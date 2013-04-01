import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

/**
 * Klasa reprezentująca całkowicie losowe tworzenie chromosomu (trasy)
 * @author Klaudia
 *
 */
public class RandomRoute {
	
//  ========================= METODY KLASY =========================
	/**
	 * Metoda zwracająca losową trasę
	 * @param g
	 * @return 
	 */
	public Route randomRoute(Graph g) {
		Random rand = new Random();
		
		LinkedList<City> c = (LinkedList<City>)g.getCities().clone();
		City[] t = new City[c.size()];
		int index = 0;
		int i = 0;
		
		while(c.isEmpty()!=true) {
			index = rand.nextInt(c.size());
			t[i] = c.get(index);
			c.remove(index);
			i++;
		}
		
		Route r = new Route(t);
		 //System.out.println(r.toString());
		
		return r;
		
	}
	
	public static void main(String[] args) {
		
		Graph g;
		try {
			g = new Graph(10, "C:\\Users\\Klaudia\\Desktop\\miasta.txt");
			RandomRoute rr = new RandomRoute();
			
			Route r1 = rr.randomRoute(g);
			System.out.println(r1.toString());
			System.out.println(r1.weightRoute());
			System.out.println();
			
			ScrambleMutation sp = new ScrambleMutation();
			Route rm = sp.scramleMutation(r1);
			System.out.println(rm.toString());
			System.out.println(rm.weightRoute());
			System.out.println();

			

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
