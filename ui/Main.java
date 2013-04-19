package ui;

import alg.*;
import model.*;
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
public class Main extends JFrame implements MapListener, ModelProgressListener
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
	
	/** Symbol podświetlonego miasta */
	Image highlightedSymbol;
	
	/** Symbol zaznaczonego miasta */
	Image selectedSymbol;
	
	/** Zaznaczone miasta */
	ArrayList<City> selectedCities;
	
	/** Okienko postępu */
	ProgressFrame progressFrame;
	
	/** Model */
	Model model;
	
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
		
		highlightedSymbol = null;
		try
		{
			highlightedSymbol = ImageIO.read(new File("data/selectedCity.png"));
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
		progressFrame = new ProgressFrame();
		
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
		map.interactiveLayers.add(cityLayer);
		
		selectedCityLayer = new MapLayer("Miasta w grafie");
		colors = new Vector<Color>();
		colors.add(Color.RED);
		selectedCityLayer.setFillColors(colors);
		selectedCityLayer.setTextVisible(true);
		selectedCityLayer.setTextForced(true);
		selectedCityLayer.setSpotRadius(100);
		map.layers.add(selectedCityLayer);
		
		// Mapa
		map.listener = this;
		map.setGridSize(10000);
		map.setMinimalZoomForText(500);
		map.setUnitsPerPixel(1000);
		
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
				obj.highlightedSymbol = highlightedSymbol;
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
	 * Sprawdza, czy miasto jest zaznaczone
	 * 
	 * @param name	Nazwa miasta
	 * @return	Czy zaznaczone?
	 */
	public boolean isCitySelected(String name)
	{
		for(City city : selectedCities)
		{
			if(city.name.equals(name))
			{
				return true;
			}
		}
		
		return false;
	}
	
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
			else
			{
				selectedCities.add(city);
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
		for(City c : selectedCities)
		{
			if(c.name.equals(name))
			{
				selectedCities.remove(c);
				break;
			}
		}
	
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
	 * Maluje cykl. Usuwa poprzendi, jeżeli był namalowany
	 * 
	 * @param cities	Tablica nazw miast
	 */
	public void paintCycle(String[] names)
	{
		cycleLayer.objects.clear();
		
		Vector<FPoint> coords = new Vector<FPoint>();
		
		for(String name : names)
		{
			coords.add(getCityPoint(name));
		}
		
		MapObject obj = new MapObject();
		obj.coords = coords;
		cycleLayer.objects.add(obj);
		
		map.repaint();
	}
	
	/**
	 * Pobiera długość cyklu
	 * 
	 * @param cities	Tablica nazw miast
	 * @return	Długość cyklu
	 */
	public float getCycleLength(String[] names)
	{
		float res = 0;
		
		Vector<FPoint> coords = new Vector<FPoint>();
		
		for(String name : names)
		{
			coords.add(getCityPoint(name));
		}
		
		for(int i = 0; i < coords.size()-1; i++)
		{
			res += Math.sqrt(Math.pow(coords.get(i+1).x - coords.get(i).x, 2) +
							Math.pow(coords.get(i+1).y - coords.get(i).y, 2));
		}
		
		res += Math.sqrt(Math.pow(coords.get(coords.size()-1).x - coords.get(0).x, 2) +
						Math.pow(coords.get(coords.size()-1).y - coords.get(0).y, 2));
		
		return res;
	}
	
	/**
	 * Wykonaj obliczenia
	 */
	public void process()
	{
		model = new Model();
		model.setModelProgessListener(this);
		
		// Wczytywanie parametrów z komonentów [TODO]
		int parentCount = 100;
		int parentLimit = 100;
		int childLimit = 200;
		double mutationFactor = 0.001;
		int generations = 10000;
		
		double[] x = new double[selectedCities.size()];
		double[] y = new double[selectedCities.size()];
		
		// Tworzenie grafu planarnego
		for(int i = 0; i < selectedCities.size(); i++)
		{
			x[i] = (double) selectedCities.get(i).point.x;
			y[i] = (double) selectedCities.get(i).point.y;
		}
		
		model.createPlanarGraph(x, y);
		
		// Stworzenie listy z kolejnymi liczbami naturalnymi
		ArrayList<Integer> numberList = new ArrayList<Integer>();
		for(int i = 0; i < selectedCities.size(); i++)
		{
			numberList.add(new Integer(i));
		}
		
		// Losowanie rodziców
		for(int i = 0; i < parentCount; i++)
		{
			Collections.shuffle(numberList);
			int[] parent = new int[selectedCities.size()];
			
			for(int j = 0; j < selectedCities.size(); j++)
			{
				parent[j] = numberList.get(j);
			}
			
			try
			{
				model.addParent(parent);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				System.exit(0);
			}
		}
		
		// Ustawianie parametrów
		model.setParentLimit(parentLimit);
		model.setChildLimit(childLimit);
		model.setMutationFactor(mutationFactor);
		
		// Inicjalizacja modelu
		try
		{
			model.initialize();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			System.exit(0);
		}
		
		// Wykonanie symulacji
		model.simulate(generations);
		
		// Blokowanie i otwarcie okna postępu
		//setLocked(true);
		//launchProgressWindow();
	}
	
	/**
	 * Obsługa kliknięcia mapy
	 * 
	 * @return	Określa, czy mapa ma rozpocząć zaznaczanie oknem
	 */
	public boolean mapClicked()
	{
		if(cityLayer.getHighlightedObject() != null)
		{
			String city = cityLayer.getHighlightedObject().text;
			
			if(!isCitySelected(city))
			{
				selectCity(city);
			}
			else
			{
				deselectCity(city);
			}
			
			return false;
		}
		
		else return true;
	}
	
	/**
	 * Obsługuje zaznaczenie oknem
	 */
	public void mapWindowFinished(FPoint start, FPoint end)
	{
		for(City city : cities)
		{
			if(((city.point.x > start.x && city.point.x < end.x) || (city.point.x > end.x && city.point.x < start.x)) &&
				((city.point.y > start.y && city.point.y < end.y) || (city.point.y > end.y && city.point.y < start.y)))
			{
			
				if(!isCitySelected(city.name))
				{
					selectCity(city.name);
				}
				else
				{
					deselectCity(city.name);
				}
			}
		}
		
		process();
	}
	
	/**
	 * Aktualizuje postęp modelu
	 * 
	 * @param progress	Ułamek postępu
	 */
	public void progressUpdate(double progress)
	{
		System.out.println(progress);
		progressFrame.update((int) (progress*1000.0));
		
		// Narysuj cykl, jeżeli zakończono
		if(progress == 1.0)
		{
			int[] best = model.getCurrentBest();
			String[] result = new String[best.length];
		
			for(int i = 0; i < best.length; i++)
			{
				result[i] = selectedCities.get(best[i]).name;
			}
		
			paintCycle(result);
			//lengthLabel.setText(String.format("Długość cyklu: %.3f km", getCycleLength(result) / 1000.0)));
		}
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
