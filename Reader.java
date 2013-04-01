

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;

import java.util.LinkedList;
/**
 * Klasa pobierająca z pliku nazwy miast i ich współrzędne.
 * @author Klaudia
 *
 */
public class Reader {
	
//  ========================= METODY KLASY =========================
	public LinkedList<City> read(String filename) throws IOException {

		LinkedList<City> cities = new LinkedList<City>();

		FileReader fileReader = new FileReader(filename);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		String textLine = bufferedReader.readLine();

		while(textLine != null) {
			String[] splitText = textLine.split("\t");
			cities.add(new City(Integer.parseInt(splitText[1]), Integer
					.parseInt(splitText[2]), splitText[0]));
			textLine = bufferedReader.readLine();
		}
		

		bufferedReader.close();

		return cities;
	}
}
