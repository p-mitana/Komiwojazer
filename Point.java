/**
 * Ta klasa reprezentuje punkt o zmiennoprzecinkowych współrzędnych w najprostszej
 * postaci.
 * Umożliwia też obliczenie odległości między dwoma punktami.
 */
public class Point
{
	//  ========================= POLA KLASY =========================
	
	/** Współrzędna x */
	double x;
	
	/** Współrzędna y */
	double y;
	
	//  ========================= KONSTRUKTORY =========================
	
	/**
	 * Tworzy punkt na podstawie jego współrzędnych.
	 *
	 * @param x     Wspórzędna x.
	 * @param y     Współrzędna y.
	 */
	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	//  ========================= METODY KLASY =========================
	
	/**
	 * Oblicza odległość dwóch zadanych punktów.
	 * 
	 * @param p1     Punkt 1
	 * @param p2     Punkt 2
	 * @return       Odległość między punktami
	 */
	public static double distance(Point p1, Point p2)
	{
		return p1.distance(p2);
	}
	
	/**
	 * Oblicza odległość od innego punktu
	 *
	 * @param point     Punkt, od którego obliczamy odległość
	 * @return          Odległość między punktami
	 */
	public double distance(Point point)
	{
		double x = Math.abs(this.x - p.getX());
		double y = Math.abs(this.y - p.getY());
		
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
	 * Ustawia współrzędną y.
	 *
	 * @param y     Współrzędna y.
	 */
	public void setY(double y)
	{
		this.y = y;
	}
}
