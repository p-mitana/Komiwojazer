package ui.map;

/**
 * Interfejs pozwalający na wykrywanie kliknięć na mapie.
 */
public interface MapListener
{
	/**
	 * Zdarzenie kliknięcia mapy
	 * 
	 * @return	Gdy zwrócone zostanie true, mapa rozpoczyna zaznaczanie oknem
	 */
	public boolean mapClicked();
	
	/**
	 * Zwraca zaznaczone okno
	 * 
	 * @param start	Punkt początkowy
	 * @param end	Punkt końcowy
	 */
	public void mapWindowFinished(FPoint start, FPoint end);
}
