package ui.map;

import java.awt.*;
import java.util.*;

/**
 * Klasa reprezentuje warstwę na mapie.
 * 
 * Warstwa przechowuje obiekty oraz informacje o ich wyglądzie.
 * warswty można selektywnie pokazywać i ukrywać.
 */
public class MapLayer
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
	
	/** Określa, czy tekst jest widoczny niezależnie od powiększenia */
	private boolean textForced;
	
	/** Kolor obramowania elementów */
	private Color drawColor;
	
	/** Kolor podświetlenia */
	private Color highlightColor;
	
	/** Kolor tekstu */
	private Color textColor;
	
	/** Grubość linii */
	private int lineWidth;
	
	/** Lista kolorów tła w zależności od wysokości */
	private Vector<Color> fillColors;
	
	/** Początek skali wysokości na mapie */
	private float heightMinimum;
	
	/** Co ile rozstawione są kolory w zależności od wysokości */
	private float heightInterval;
	
	/** Promień obiektu o jednym punkcie składowym */
	private float spotRadius;
	
	/** Podświetlony kursorem obiekt */
	private MapObject highlightedObject;
	
	/** Wektor obiektów na tej warstwie */
	public Vector<MapObject> objects;
	
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
	public MapLayer(String name)
	{
		this.name = name;
		textVisible = false;
		textForced = false;
		drawColor = new Color(0, 0, 0);
		fillColors = new Vector<Color>();
		fillColors.add(new Color(255, 255, 255));
		textColor = new Color(0, 0, 0);
		highlightColor = new Color(255, 255, 255);
		lineWidth = 1;
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
	public float heightOf(FPoint fp)
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
	 * Podświetla najbliższy obiekt
	 * 
	 * @param p	Punkt, wokół którego szukamy
	 * @param d	Odległość, jeżeli obiekt jest punktowy
	 */
	public void highlightNearest(FPoint p, float d)
	{
		MapObject obj = null;
		float distance = Float.POSITIVE_INFINITY;
		
		for(MapObject object : objects)
		{
			if(object.coords.size() > 1)
			{
				if(object.contains(p))  // Obiekt obszarowy musi zawierać punkt
				{
					highlightedObject = object;
					return;  // Nie szukaj drugiego
				}
			}
			
			else
			{
				FPoint point = object.coords.get(0);
				float dist = (float) Math.sqrt(Math.pow(point.x - p.x, 2.0f) + (Math.pow(point.y - p.y, 2.0f)));
				
				if(dist < distance)
				{
					distance = dist;
					obj = object;
				}
			}
		}
		
		if(obj != null && distance < d)
		{
			highlightedObject = obj;
		}
		else
		{
			highlightedObject = null;
		}
	}
	
	/**
	 * Przełącza wyświetlanie warstwy
	 * 
	 * @param b	Czy warstwa ma być wyświetlana
	 */
	public void setEnabled(boolean b)
	{
		enabled = b;
	}
	
	/**
	 * Zwraca, czy warstwa jest wyświetlana
	 * 
	 * @return	Czy warstwa jest wyświetlana
	 */
	public boolean getEnabled()
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
	public boolean getBorderless()
	{
		return borderless;
	}
	
	/**
	 * Ustawia nazwę warstwy
	 * 
	 * @param name	Nazwa
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Zwraca nazwę warstwy
	 * 
	 * @return	Nazwa
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Ustawia promień obiektów punktowych
	 * 
	 * @param r	Promień
	 */
	public void setSpotRadius(float r)
	{
		spotRadius = r;
	}
	
	/**
	 * Zwraca promień obiektów punktowych
	 * 
	 * @return	Promień
	 */
	public float getSpotRadius()
	{
		return spotRadius;
	}
	
	/**
	 * Ustawia wysokość, od której zacznie się skala kolorów
	 * 
	 * @param h	Wysokość początkowa
	 */
	public void setHeightMinimum(float h)
	{
		heightMinimum = h;
	}
	
	/**
	 * Zwraca wysokość, od której zacznie się skala kolorów
	 * 
	 * @return	Wysokość początkowa
	 */
	public float getHeightMinimum()
	{
		return heightMinimum;
	}
	
	/**
	 * Ustawia różnicę wysokości między kolorami na skali
	 * 
	 * @param h	Wysokość
	 */
	public void setHeightInterval(float h)
	{
		heightInterval = h;
	}
	
	/**
	 * Zwraca różnicę wysokości między kolorami na skali
	 * 
	 * @return	Wysokość
	 */
	public float getHeightInterval()
	{
		return heightInterval;
	}
	
	/**
	 * Ustawia kolor obramowania - czarny, gdy przekażemy null
	 * 
	 * @param color	Kolor
	 */
	public void setDrawColor(Color color)
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
	public Color getDrawColor()
	{
		return drawColor;
	}
	
	/**
	 * Ustawia kolor tekstu - czarny, gdy przekażemy null
	 * 
	 * @param color	Kolor
	 */
	public void setTextColor(Color color)
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
	public Color getTextColor()
	{
		return textColor;
	}
	
	/**
	 * Ustawia listę kolorów tła
	 * 
	 * @param colors	Kolory
	 */
	public void setFillColors(Vector<Color> colors)
	{
		fillColors = colors;
	}
	
	/**
	 * Zwraca listę kolorów tła
	 * 
	 * @return	Kolory
	 */
	public Vector<Color> getFillColors()
	{
		return fillColors;
	}
	
	/**
	 * Ustawia kolor podświetlenia - biały, gdy przekażemy null

	 * 
	 * @param color	Kolor
	 */
	public void setHighlightColor(Color color)
	{
		if(color == null)
		{
			highlightColor = new Color(255, 255, 255);
		}
		else
		{
			highlightColor = color;
		}
	}
	
	/**
	 * Zwraca kolor podświetlenia
	 * 
	 * @return	Kolor
	 */
	public Color getHighlightColor()
	{
		return highlightColor;
	}

	/**
	 * Ustawia widoczność tekstu
	 * 
	 * @param b	Widoczność tekstu
	 */
	public void setTextVisible(boolean b)
	{
		textVisible = b;
	}
	
	/**
	 * Zwraca widoczność tekstu
	 * 
	 * @return	Widoczność tekstu
	 */
	public boolean getTextVisible()
	{
		return textVisible;
	}
	
	/**

	 * Ustawia wymuszenie widoczności tekstu (niezależnie od powiększenia)
	 * 
	 * @param b	Wymuszenie widoczności tekstu
	 */
	public void setTextForced(boolean b)
	{
		textForced = b;
	}
	
	/**
	 * Zwraca wymuszenie widoczności tekstu (niezależnie od powiększenia)
	 * 
	 * @return	Wymuszenie widoczności tekstu

	 */
	public boolean getTextForced()
	{
		return textForced;
	}
	
	/**
	 * Ustawia grubość linii
	 * 
	 * @param width	grubość linii
	 */
	public void setLineWidth(int width)
	{
		if(width > 0)
		{
			lineWidth = width;
		}
	}
	
	/**
	 * Zwraca grubość linii
	 * 
	 * @return	Grubość linii
	 */
	public int getLineWidth()
	{
		return lineWidth;
	}
	
	/**
	 * Zwraca podświetlony obiekt
	 * 
	 * @return	Podświetlony obiekt
	 */
	public MapObject getHighlightedObject()
	{
		return highlightedObject;
	}
}
