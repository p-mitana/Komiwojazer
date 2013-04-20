package ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Klasa wyświetla panel z miastami.
 */
public class CityPanel extends JComponent implements Scrollable, MouseListener
{
	//  ========================= POLA KLASY ========================
	
	/** Lista nazw miast - referencja */
	private ArrayList<Main.City> cities;
	
	/** Zaznaczone miasta - referencja */
	private ArrayList<Main.City> selectedCities;
	
	/** Obiekt główny */
	public Main mainObject = null;  // Niezbyt eleganckie, ale nie chce mi się pisać Listenera.
	
	/** Lista znalezionych */
	ArrayList<Main.City> matchList;
		
	//  ========================= KONSTRUKTORY KLASY ========================
	
	/**
	 * Konstruktor klasy
	 */
	public CityPanel(ArrayList<Main.City> cities, ArrayList<Main.City> selectedCities)
	{
		this.cities = cities;
		this.selectedCities = selectedCities;
		setBackground(new Color(255, 255, 255));
		setPreferredSize(new Dimension(300, 24));
		
		matchList = new ArrayList<Main.City>();
		
		addMouseListener(this);
	}
	
	//  ========================= METODY KLASY ========================
	
	/**
	 * Znajduje miasta po tekście. Gdy tekst ma <3 znaki, czyści pole.
	 * 
	 * @param text	Tekst
	 */
	public void find(String text)
	{
		matchList.clear();
		
		// Wtedy wyświetla zaznaczone miasta
		if(text.length() == 0)
		{
			for(Main.City c : selectedCities)
			{
				matchList.add(c);
			}
		}
		else
		{
			text = text.toLowerCase();
		
			for(Main.City c : cities)
			{
				if(c.name.toLowerCase().contains(text))
				{
					matchList.add(c);
				}
			}
		}
		
		setPreferredSize(new Dimension(300, 24 * Math.max(matchList.size(), 1)));
		getParent().revalidate();
		repaint();
	}
	
	/**
	 * Metoda rysująca
	 * 
	 * @param g	Obiekt rysujący
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		
		
		if(matchList.size() == 0)
		{
		    g.setColor(Color.GRAY);
		    g.setFont(getFont().deriveFont(Font.ITALIC));
			g.drawString("Brak wyników", 3, 16);
		}
		
		else
		{
			for(int i = 0; i < matchList.size(); i++)
			{
				// Nazwa miasta
				g.setColor(getForeground());
				g.drawString(matchList.get(i).name, 3, 24*i + 16);
			
				// Zaznaczenie
				g.drawRect(getWidth() - 34, 24*i + 5, 12, 12);
				if(selectedCities.contains(matchList.get(i)))
				{
					g.setColor(new Color(0, 90, 146));
					g.fillRect(getWidth() - 32, 24*i + 7, 9, 9);
					g.setColor(getForeground());
				}
			
				// Przycisk ze strzałeczką
				g.drawRect(getWidth() - 17, 24*i + 5, 12, 12);
				g.drawLine(getWidth() - 14, 24*i + 8, getWidth() - 8, 24*i + 14);
				g.drawLine(getWidth() - 12, 24*i + 14, getWidth() - 8, 24*i + 14);
				g.drawLine(getWidth() - 8, 24*i + 14, getWidth() - 8, 24*i + 10);
			
				// Separator
				g.setColor(new Color(160, 160, 160));
				g.drawLine(0, 24*i+23, getWidth(), 24*i+23);
			}
		}
	}
	
	/**
	 * Zdarzenie wejścia kursora
	 * 
	 * @param evt	Zdarzenie
	 */
	public void mouseEntered(MouseEvent evt){}
	
	/**
	 * Zdarzenie wyjścia kursora
	 * 
	 * @param evt	Zdarzenie
	 */
	public void mouseExited(MouseEvent evt){}
	
	/**
	 * Zdarzenie naciśnięcia przycisku myszy
	 * 
	 * @param evt	Zdarzenie
	 */
	public void mousePressed(MouseEvent evt){}
	
	/**
	 * Zdarzenie zwolnienia przycisku myszy
	 * 
	 * @param evt	Zdarzenie
	 */
	public void mouseReleased(MouseEvent evt){}
	
	/**
	 * Zdarzenie kliknięcia
	 * 
	 * @param evt	Zdarzenie
	 */
	public void mouseClicked(MouseEvent evt)
	{
		if(!isEnabled())
		{
			return;
		}
		
		int index = evt.getY() / 24;
		int yPos = evt.getY() % 24;
		
		if(matchList.size() < index)
		{
			return;
		}
		
		// Przycisk zaznaczania
		if(yPos >= 5 && yPos <= 17 && 
			evt.getX() >= getWidth() - 34 && evt.getX() <= getWidth() - 21)
		{
			String name = matchList.get(index).name;
			
			if(mainObject != null)
			{
				if(!mainObject.isCitySelected(name))
				{
					mainObject.selectCity(name);
				}
				else
				{
					mainObject.deselectCity(name);
				}
			}
		}
		
		// Przycisk ze strzałeczką
		if(yPos >= 5 && yPos <= 17 && 
			evt.getX() >= getWidth() - 17 && evt.getX() <= getWidth() - 4)
		{
			String name = matchList.get(index).name;
			
			if(mainObject != null)
			{
				mainObject.localizeCity(name);
			}
		}
	}
	
	/**
	 * Zwraca preferowany wymiar kontenera
	 * 
	 * @return	Preferowany wymiar kontenera
	 */
	public Dimension getPreferredScrollableViewportSize()
	{
		return getPreferredSize();
	}
	
	/**
	 * Zwraca, czy dopasowywać szerokość komponentu do kontenera
	 * 
	 * @return	Dopasowanie. Zawsze true
	 */
	public boolean getScrollableTracksViewportWidth()
	{
		return true;
	}
	
	/**
	 * Zwraca, czy dopasowywać wysokość komponentu do kontenera
	 * 
	 * @return	Dopasowanie. Zawsze false
	 */
	public boolean getScrollableTracksViewportHeight()
	{
		return false;
	}
	
	/**
	 * Zwraca, o ile pikseli trzeba przewinąć celem przewinięcia o blok
	 *
	 * @param visibleRect	Widoczna część okna
	 * @param orientation	Orientacja
	 * @param direction	Kierunek
	 */
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) 
	{
		return 72;
	}
	
	/**
	 * Zwraca, o ile pikseli trzeba przewinąć celem odsłonięcia kolejnej jednostki
	 *
	 * @param visibleRect	Widoczna część okna
	 * @param orientation	Orientacja
	 * @param direction	Kierunek
	 */
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) 
	{
		return 24;
	}
}
