package ui.map;

import java.awt.*;
import java.util.*;

/**
 * Klasa reprezentuje obiekt na mapie.
 * 
 * Obiekt może być wielokątem lub punktem. Jeżeli jest punktem, można przypisać
 * mu symbol obrazkowy, lub zostawić bez niego - wtedy zostanie narysowane kółeczko
 * lub kropka, w zależności od przybliżenia mapy.
 */
public class MapObject
{
	//  ========================= POLA KLASY ========================
	
	/** Wierzchołki wielokąta. Dla obiektów punktowych wektor zawiera jeden element. */
	public Vector<FPoint> coords;
	
	/** Wysokość obiektu */
	public float height;
	
	/** Etykieta obiektu */
	public String text;
	
	/** Symbol obiektu */
	public Image symbol;
	
	//  ========================= KONSTRUKTORY KLASY ========================
	
	/**
	 * Tworzy pusty obiekt bez współrzędnych.
	 */
	public MapObject()
	{
		text = "";
		coords = new Vector<FPoint>();
	}
	
	/**
	 * Konstruktor kopiujący. Nie kopiuje obiektów składowych, tylko referencje.
	 */
	public MapObject(MapObject o)
	{
		coords = o.coords;
		height = o.height;
		text = o.text;
		symbol = o.symbol;
	}
	//  ========================= METODY KLASY ========================
	
	/**
	 * Sprawdza, czy obiekt zawiera dany punkt.
	 * 
	 * Obiekt zawiera punkt, jeżeli przecina półprostą wychodzącą z tego punktu
	 * nieparzystą liczbę razy.
	 */
	public boolean contains(FPoint fp)
	{
		if(coords.size() == 1)
		{
			return fp.x == coords.get(0).x && fp.y == coords.get(0).y;
		}
		
		else
		{
			int crosses = 0;
			
			// Sprawdzamy, ile boków wielokąta przetnie się z półprostą wychodzącą z punktu w górę
			for(int i = 0; i < coords.size(); i++)
			{
				FPoint p1 = coords.get(i);
				
				// Jeżeli ma wyjść poza tablicę, to weź bok łączący ostatni punkt z pierwszym
				FPoint p2 = coords.get(i == coords.size()-1 ? 0 : i+1);
				
				/*
				 * Jeżeli punkt leży na którymś z wierzchołków, to należy do obiektu.
				 * Ze względu na błędy zmiennoprzecinkowe zaokrąglam do centymetrów.
				 */
				if(Math.round(fp.x*100) == Math.round(p1.x*100) &&
					Math.round(fp.y*100) == Math.round(p1.y*100))
				{
					return true;
				}
				
				// Jak oba końce odcinka leżą po tej samej stronie punktu, to się nie przecina
				if((p1.x < fp.x && p2.x < fp.x) || (p1.x > fp.x && p2.x > fp.x))
				{
					continue;
				}
				
				float k = (p2.x - p1.x) / (fp.x - p1.x);
				float cy = p1.y + k*(p2.y - p1.y);
				
				if(cy <= fp.y)
				{
					crosses++;
				}
			}
			
			// Obiekt zawiera punkt, jeżeli przecina półprostą nieparzystą ilość razy
			return crosses % 2 == 1;
		}
	}
}
