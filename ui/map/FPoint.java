package ui.map;

/**
 * Klasa reprezentuje punkt na mapie.
 */
class FPoint
{
	//  ========================= POLA KLASY ========================
	
	/** Współrzędna X */
	public float x;
	
	/** Współrzędna Y */
	public float y;
	
	//  ========================= KONSTRUKTORY KLASY ========================
	
	/**
	 * Konstruktor klasy.
	 * 
	 * @param px	Współrzędna X
	 * @param py	Współrzędna Y
	 */
	FPoint(float px, float py)
	{
		x = px;
		y = py;
	}
}
