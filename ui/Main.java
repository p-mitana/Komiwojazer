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
	
	/** Warstwa z miastami zaznaczonymi */
	MapLayer selectedCityLayer;
	
	/** Warstwa na cykle */
	MapLayer cycleLayer;
	
	/** Lista nazw miast */
	ArrayList<City> cities;
	
	/** Symbol zaznaczonego miasta */
	Image selectedSymbol;
	
	/** Zaznaczone miasta */
	ArrayList<City> selectedCities;
	
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
		
		selectedSymbol = null;
		try
		{
			selectedSymbol = ImageIO.read(new File("data/selectedCity.png"));
		}
		catch(Exception ex){}
		
		// -------- Tworzenie interfejsu --------
		
		// Tworzenie układów
		Box mainLineBox = new Box(BoxLayout.LINE_AXIS);
		
		// Tworzenie komponentów
		map = new MapComponent();
		JPanel rightPanel = new JPanel();
		
		// Konfiguracja komponentów
		cycleLayer = new MapLayer("Cykle");
		Vector<Color> colors = new Vector<Color>();
		cycleLayer.setDrawColor(Color.RED);
		colors.add(new Color(0, 0, 0, 0));
		cycleLayer.setFillColors(colors);
		cycleLayer.setSpotRadius(100);
		cycleLayer.setLineWidth(4);
		map.layers.add(cycleLayer);
		
		cityLayer = new MapLayer("Miasta");
		colors = new Vector<Color>();
		colors.add(Color.BLUE);
		cityLayer.setFillColors(colors);
		cityLayer.setTextVisible(true);
		cityLayer.setSpotRadius(100);
		map.layers.add(cityLayer);
		
		selectedCityLayer = new MapLayer("Miasta w grafie");
		colors = new Vector<Color>();
		colors.add(Color.RED);
		selectedCityLayer.setFillColors(colors);
		selectedCityLayer.setTextVisible(false);
		selectedCityLayer.setSpotRadius(100);
		map.layers.add(selectedCityLayer);
		
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
		selectedCities = new ArrayList<City>();
		
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
	 * Zaznaczenie miasta o danej nazwie i odrysowanie mapy
	 * 
	 * @param name	Nazwa miasta
	 */
	public void selectCity(String name)
	{
		selectCity(name, true);
	}
	
	/**
	 * Zaznaczenie miasta o danej nazwie
	 * 
	 * @param name	Nazwa miasta
	 * @param repaint	Czy odrysować?
	 */
	public void selectCity(String name, boolean repaint)
	{
		for(City city : cities)
		{
			if(!city.name.equals(name))
			{
				continue;
			}
			
			for(MapObject object : cityLayer.objects)
			{
				if(object.text.equals(name))
				{
					MapObject selected = new MapObject(object);
					selected.symbol = selectedSymbol;
					selectedCityLayer.objects.add(selected);
					
					break;
				}
			}
			
			break;
		}
		
		if(repaint)
		{
			map.repaint();
		}
	}
	
	/**
	 * Odaznaczenie miasta o danej nazwie i odrysowanie mapy
	 * 
	 * @param name	Nazwa miasta
	 */
	public void deselectCity(String name)
	{
		deselectCity(name, true);
	}
	
	/**
	 * Odzaznaczenie miasta o danej nazwie
	 * 
	 * @param name	Nazwa miasta
	 * @param repaint	Czy odrysować?
	 */
	public void deselectCity(String name, boolean repaint)
	{
		for(MapObject object : selectedCityLayer.objects)
		{
			if(object.text.equals(name))
			{
				selectedCityLayer.objects.remove(object);
				
				break;
			}
		}
		
		if(repaint)
		{
			map.repaint();
		}
	}
	
	/**
	 * Pobiera punkt, w którym leży miasto o danej nazwie
	 * 
	 * @param name	Nazwa miasta
	 * @return	Punkt
	 */
	FPoint getCityPoint(String name)
	{
		for(City city : cities)
		{
			if(city.name.equals(name))
			{
				return city.point;
			}
		}
		
		return null;
	}
	
	/**
	 * Metoda uruchamiająca program.
	 * 
	 * @param args	Argumenty wywołania
	 */
	public static void main(String[] args)
	{
		Main main = new Main();
	}
}
