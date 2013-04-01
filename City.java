

/**
 * Ta klasa reprezentuje punkt o zmiennoprzecinkowych współrzędnych w najprostszej
 * postaci.
 * Umożliwia też obliczenie odległości między dwoma punktami.
 */
public class City
{
	//  ========================= POLA KLASY =========================
	
	/** Współrzędna x */
	double x;
	
	/** Współrzędna y */
	double y;
	
	/** Nazwa miasta */
	String name;
	
	//  ========================= KONSTRUKTORY =========================
	
	/**
	 * Tworzy punkt na podstawie jego współrzędnych.
	 *
	 * @param x     Wspórzędna x.
	 * @param y     Współrzędna y.
	 */
	public City(double x, double y, String name)
	{
		this.x = x;
		this.y = y;
		this.name = name;
	}
	
	//  ========================= METODY KLASY =========================
	
	/**
	 * Oblicza odległość dwóch zadanych punktów.
	 * 
	 * @param p1     Punkt 1
	 * @param p2     Punkt 2
	 * @return       Odległość między punktami
	 */
	public static double distance(City p1, City p2)
	{
		return p1.distance(p2);
	}
	
	/**
	 * Oblicza odległość od innego punktu
	 *
	 * @param point     Punkt, od którego obliczamy odległość
	 * @return          Odległość między punktami
	 */
	public double distance(City point)
	{
		double x = Math.abs(this.x - point.getX());
		double y = Math.abs(this.y - point.getY());
		
		return Math.sqrt(x*x + y*y);
	}
	
	//  ========================= GETTERY I SETTERY =========================
	
	/**
	 * Zwraca współrzędną x.
	 *
	 * @return     Współrzędna x.
	 */
	public double getX()
	{
		return x;
	}
	
	/**
	 * Ustawia współrzędną x.
	 *
	 * @param x     Współrzędna x.
	 */
	public void setX(double x)
	{
		this.x = x;
	}
	
	/**
	 * Zwraca współrzędną y.
	 *
	 * @return     Współrzędna y.
	 */
	public double getY()
	{
		return y;
	}
	
	/**
	 * Zwraca nazwę miasta.
	 *
	 * @return     nazwa miasta
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Ustawia współrzędną y.
	 *
	 * @param y     Współrzędna y.
	 */
	public void setY(double y)
	{
		this.y = y;
	}
	
	/**
	 * Nadpisanie metody toString
	 */
	public String toString()
	{
		return "Nazwa: " + name + " Współrzędne: " + x + ", " + y + "\n";
		
	}
}
