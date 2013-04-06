package ui;

import alg.*;
import ui.map.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

/**
 * Główna klasa programu. Zawiera okno główne i cały interfejs.
 */
public class Main extends JFrame
{
	//  ========================= KLASY WEWNĘTRZNE ========================
	
	/**
	 * Pomocnicza klasa wewnętrzna, reprezentuje miasto
	 */
	class City
	{
		/** Nazwa miasta */
		String name;
		
		/** Współrzędne miasta */
		FPoint point;
	}
	
	//  ========================= POLA KLASY ========================
		
	/** Komponent z mapą */
	MapComponent map;
	
	/** Warstwa z miastami */
	MapLayer cityLayer;
	
	/** Lista nazw miast */
	ArrayList<City> cities;
	
	//  ========================= KONSTRUKTORY KLASY ========================
	
	/**
	 * Konstruktor klasy - tworzy cały interfejs programu.
	 */
	public Main()
	{
		// -------- Obiekty pomocnicze --------
		Image symbol = null;
		try
		{
			symbol = ImageIO.read(new File("data/city.png"));
		}
		catch(Exception ex){}
		
		// -------- Tworzenie interfejsu --------
		
		// Tworzenie układów
		Box mainLineBox = new Box(BoxLayout.LINE_AXIS);
		
		// Tworzenie komponentów
		map = new MapComponent();
		JPanel rightPanel = new JPanel();
		
		// Konfiguracja komponentów
		MapLayer cityLayer = new MapLayer("Miasta");
		Vector<Color> colors = new Vector<Color>();
		colors.add(Color.BLUE);
		cityLayer.setFillColors(colors);
		cityLayer.setTextVisible(true);
		cityLayer.setSpotRadius(100);
		map.layers.add(cityLayer);
		
		map.setGridSize(10000);
		map.setMinimalZoomForText(500);
		map.setUnitsPerPixel(500);
		// Mapa
		
		// Okno
		setTitle("Komiwojażer - Kołdarz, Komnata, Mitana");
		setSize(1024, 768);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
		// Panel po prawej stronie
		rightPanel.setMinimumSize(new Dimension(300, 0));
		rightPanel.setPreferredSize(new Dimension(300, 300));
		rightPanel.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
		
		// Składanie interfejsu
		add(mainLineBox);
		mainLineBox.add(map);
		mainLineBox.add(rightPanel);
		
		// -------- Wczytywanie danych --------
		cities = new ArrayList<City>();
		
		try
		{
			BufferedReader brin = new BufferedReader(new FileReader("data/data.txt"));
			
			String line;
			
			int lines = 0;
			
			// Obliczę środek geograficzny
			double centerX = 0.0, centerY = 0.0;
			
			while((line = brin.readLine()) != null)
			{
				String[] words = line.split("\t");
				
				City c = new City();
				c.name = words[0];
				
				float geoY = Float.parseFloat(words[1]);  // Najpierw Y!!! (na północ)
				float geoX = Float.parseFloat(words[2]);
				c.point = new FPoint(geoX, geoY);
				
				centerY += Double.parseDouble(words[1]);
				centerX += Double.parseDouble(words[2]);
				lines++;
				
				cities.add(c);
			}
			
			centerX /= (double) lines;
			centerY /= (double) lines;
			
			float centerXRad = (float) (centerX * Math.PI / 180);
			float centerYRad = (float) (centerY * Math.PI / 180);
			
			float newYMax = 0.0f;
			
			// Wyliczenie odległości od środka w metrach i dodanie na mapę
			for(int i = 0; i < cities.size(); i++)
			{
				City city = cities.get(i);
				
				// Współrzędne punktu w radianach
				float xRad = (float) (city.point.x * Math.PI / 180);
				float yRad = (float) (city.point.y * Math.PI / 180);
				
				// Długość równoleżnika w środku dla współrzędnej Y
				float centerLength = (float) (Math.cos(centerYRad) * 2 * Math.PI * 6371);
				
				// Odległość X
				city.point.x = (float) (centerLength * (xRad - centerXRad) * 1000 / (2*Math.PI));
				
				// Odległość Y - oś odwrócona, mniejsze na dole
				float centralHeight = 20003.93f;
				city.point.y = (float) (centralHeight * (centerYRad - yRad) * 1000 / (Math.PI));
				
				// Dodanie miasta na mapę
				MapObject obj = new MapObject();
				obj.coords = new Vector<FPoint>();
				obj.coords.add(city.point);
				obj.text = city.name;
				obj.symbol = symbol;
				cityLayer.objects.add(obj);
			}
		}
		catch(IOException ex)
		{
			JOptionPane.showMessageDialog(this, "Błąd odczytu pliku.", "Błąd", JOptionPane.ERROR_MESSAGE);
		}
		
		// -------- Ustawienie parametrów okna i wyświetlenie --------
		setVisible(true);
	}
	
	//  ========================= METODY KLASY ========================
	
	/**
	 * Metoda uruchamiająca program.
	 * 
	 * @param args	Argumenty wywołania
	 */
	public static void main(String[] args)
	{
		new Main();
	}
}
