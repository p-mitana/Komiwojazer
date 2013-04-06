package ui.map;

import java.awt.*;
import java.util.*;

/**
 * Klasa reprezentuje warstwę na mapie.
 * 
 * Warstwa przechowuje obiekty oraz informacje o ich wyglądzie.
 * warswty można selektywnie pokazywać i ukrywać.
 */
class MapLayer
{
	//  ========================= POLA KLASY ========================
	
	/** Nazwa warstwy. */
	private String name;
	
	/** Określa, czy warstwa jest widoczna */
	private boolean enabled;
	
	/** Określa, czy obramowanie jest ukryte */
	private boolean borderless;
	
	/** Określa, czy tekst jest widoczny */
	private boolean textVisible;
	
	/** Kolor obramowania elementów */
	private Color drawColor;
	
	/** Kolor tekstu */
	private Color textColor;
	
	/** Lista kolorów tła w zależności od wysokości */
	private Vector<Color> fillColors;
	
	/** Początek skali wysokości na mapie */
	private float heightMinimum;
	
	/** Co ile rozstawione są kolory w zależności od wysokości */
	private float heightInterval;
	
	/** Promień obiektu o jednym punkcie składowym */
	private float spotRadius;
	
	/** Wektor obiektów na tej warstwie */
	Vector<MapObject> objects;
	
	//  ========================= KONSTRUKTORY KLASY ========================
	
	/**
	 * Konstruuje pustą warstwę z domyślnymi parametrami.
	 * 
	 * Domyślne parametry:
	 * - Warstwa widoczna, obramowania widoczne, bez tekstu
	 * - Kolor obramowania i tekstu: czarny
	 * - Kolor wypełnienia: biały dla każdej wysokości
	 * - Wysokości: minimum - 0, rozstawienie co 1
	 * - promień punktu: 0.25
	 * 
	 * @param name	Nazwa warstwy
	 */
	MapLayer(String name)
	{
		this.name = name;
		textVisible = false;
		drawColor = new Color(0, 0, 0);
		fillColors = new Vector<Color>();
		fillColors.add(new Color(255, 255, 255));
		textColor = new Color(0, 0, 0);
		heightMinimum = 0;
		heightInterval = 1;
		enabled = true;
		borderless = false;
		spotRadius = 0.25f;
		objects = new Vector<MapObject>();
	}
	
	//  ========================= METODY KLASY ========================
	
	/**
	 * Sprawdza wysokość punktu na tej warstwie
	 * 
	 * Wysokość punktu nienależącego do żadnego z obiektów jest zerowa.
	 * 
	 * @param fp	Punkt
	 * 
	 * @return	Wysokość
	 */
	float heightOf(FPoint fp)
	{
		for(int i = 0; i < objects.size(); i++)
		{
			if(objects.get(i).contains(fp))
			{
				return objects.get(i).height;
			}
		}
		
		return 0.0f;
	}
	
	/**
	 * Przełącza wyświetlanie warstwy
	 * 
	 * @param b	Czy warstwa ma być wyświetlana
	 */
	void setEnabled(boolean b)
	{
		enabled = b;
	}
	
	/**
	 * Zwraca, czy warstwa jest wyświetlana
	 * 
	 * @return	Czy warstwa jest wyświetlana
	 */
	boolean getEnabled()
	{
		return enabled;
	}
	
	/**
	 * Przełącza ukrywanie obramowania
	 * 
	 * @param b	Czy warstwa być bez obramowania
	 */
	void setBorderless(boolean b)
	{
		borderless = b;
	}
	
	/**
	 * Zwraca, czy warstwa jest bez obramowania
	 * 
	 * @return	Czy warstwa jest bez obramowania
	 */
	boolean getBorderless()
	{
		return borderless;
	}
	
	/**
	 * Ustawia nazwę warstwy
	 * 
	 * @param name	Nazwa
	 */
	void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Zwraca nazwę warstwy
	 * 
	 * @return	Nazwa
	 */
	String getName()
	{
		return name;
	}
	
	/**
	 * Ustawia promień obiektów punktowych
	 * 
	 * @param r	Promień
	 */
	void setSpotRadius(float r)
	{
		spotRadius = r;
	}
	
	/**
	 * Zwraca promień obiektów punktowych
	 * 
	 * @return	Promień
	 */
	float getSpotRadius()
	{
		return spotRadius;
	}
	
	/**
	 * Ustawia wysokość, od której zacznie się skala kolorów
	 * 
	 * @param h	Wysokość początkowa
	 */
	void setHeightMinimum(float h)
	{
		heightMinimum = h;
	}
	
	/**
	 * Zwraca wysokość, od której zacznie się skala kolorów
	 * 
	 * @return	Wysokość początkowa
	 */
	float getHeightMinimum()
	{
		return heightMinimum;
	}
	
	/**
	 * Ustawia różnicę wysokości między kolorami na skali
	 * 
	 * @param h	Wysokość
	 */
	void setHeightInterval(float h)
	{
		heightInterval = h;
	}
	
	/**
	 * Zwraca różnicę wysokości między kolorami na skali
	 * 
	 * @return	Wysokość
	 */
	float getHeightInterval()
	{
		return heightInterval;
	}
	
	/**
	 * Ustawia kolor obramowania - czarny, gdy przekażemy null
	 * 
	 * @param color	Kolor
	 */
	void setDrawColor(Color color)
	{
		if(color == null)
		{
			drawColor = new Color(0, 0, 0);
		}
		else
		{
			drawColor = color;
		}
	}
	
	/**
	 * Zwraca kolor obramowania
	 * 
	 * @return	Kolor
	 */
	Color getDrawColor()
	{
		return drawColor;
	}
	
	/**
	 * Ustawia kolor tekstu - czarny, gdy przekażemy null
	 * 
	 * @param color	Kolor
	 */
	void setTextColor(Color color)
	{
		if(color == null)
		{
			textColor = new Color(0, 0, 0);
		}
		else
		{
			textColor = color;
		}
	}
	
	/**
	 * Zwraca kolor tekstu
	 * 
	 * @return	Kolor
	 */
	Color getTextColor()
	{
		return textColor;
	}
	
	/**
	 * Ustawia listę kolorów tła
	 * 
	 * @param colors	Kolory
	 */
	void setFillColors(Vector<Color> colors)
	{
		fillColors = colors;
	}
	
	/**
	 * Zwraca listę kolorów tła
	 * 
	 * @return	Kolory
	 */
	Vector<Color> getFillColors()
	{
		return fillColors;
	}
	
	/**
	 * Ustawia widoczność tekstu
	 * 
	 * @param b	Widoczność tekstu
	 */
	void setTextVisible(boolean b)
	{
		textVisible = b;
	}
	
	/**
	 * Zwraca widoczność tekstu
	 * 
	 * @return	Widoczność tekstu
	 */
	boolean getTextVisible()
	{
		return textVisible;
	}
}
