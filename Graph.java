

import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

/**
 * Ta klasa reprezentuje graf. Zapewnia metody pozwalające wykonywać operacje na
 * tym grafie. Konstruktor tej klasy zajmuje się generowaniem generowaniem grafu
 * planarnego na podstawie punktów podanych lub o losowych współrzędnych.
 */
public class Graph {
	// ========================= POLA KLASY =========================
	/**
	 * Zmienna reprezentująca graf.
	 */
	LinkedList<City> cities;

	// ========================= KONSTRUKTORY =========================

	/**
	 * Generuje graf planarny o zadanym rozmiarze, losowych punktach i losowych
	 * krawędziach.
	 * 
	 * @param size
	 *            Rozmiar grafu.
	 * @throws IOException
	 * @parm filename Ścieżka do pliku z którego czytamy współrzędne miast
	 */
	public Graph(int size, String filename) throws IOException {
		Reader r = new Reader();
		LinkedList<City> c = r.read(filename);


		// DO ZMIANY, tymczasowe rozwiązanie
		if (size > c.size()) {
			System.out.println("za duża liczba");
		} else {
			cities = new LinkedList<City>();

			Random rand = new Random();
			int choice;

			for (int i = 0; i < size; i++) {
				choice = rand.nextInt(c.size());
				cities.add(c.get(choice));
				c.remove(choice);
			}
		}

	}

	/**
	 * Generuje graf planarny o losowych krawędziach i podanych punktach.
	 * 
	 * @param points
	 *            Tablica zawierająca punkty.
	 */
	public Graph(City[] points) {

	}

	// ========================= METODY KLASY =========================

	// ========================= GETTERY I SETTERY =========================
	/**
	 * Metoda zwrcacające tablice miast reprezentująca otrzymany graf
	 * @return City[]
	 */
	public LinkedList<City> getCities() {
		return cities;
	}

//	public static void main(String[] args) {
//		try {
//			new Graph(6, "C:\\Users\\Klaudia\\Desktop\\miasta.txt");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
