package alg;

/**
 * Ta klasa reprezentuje graf pełny z wagami na krawędziach poprzez macierz sąsiedztwa.
 * 
 * Ujemne odległości są obsługiwane. Na przekątnej macierzy leżą zera, ale algorytm
 * NIGDY nie powinien się do nich odwoływać (cykl Hamiltona nie przechodzi dwa razy
 * przez żaden wierzchołek), dlatego też w przypadku zapytania o taką odległość
 * klasa zwróci wyjątek DistanceFromSelfRequestException.
 */
public class FullGraph
{
	//  ========================= POLA KLASY =========================
	
	/** Macierz przechowująca odległości */
	double[][] dist;
	
	//  ========================= KONSTRUKTORY =========================
	
	/**
	 * Tworzy graf o zerowych odległościach między wierzchołkami o zadanej liczbie wierzchołków
	 * 
	 * @param count	Ilość wierzchołków
	 */
	public FullGraph(int count)
	{
		dist = new double[count][];
		
		for(int i = 0; i < count; i++)
		{
			dist[i] = new double[count];  // Java domyślnie wypełnia tablice zerami
			
			for(int j = 0; j < dist[i].length; j++)
			{
				dist[i][j] = Double.POSITIVE_INFINITY;
			}
		}
	}
	
	/**
	 * Tworzy graf na podstawie punktów oraz wylicza odległości między nimi w dwuwymiarowej
	 * przestrzeni euklidesowej
	 *
	 * @param points	Tablica punktów
	 */
	public FullGraph(Point[] points)
	{
		this(points.length);
		
		for(int i = 0; i < points.length; i++)
		{
			for(int j = 0; j < i; j++)
			{
				dist[i][j] = Point.distance(points[i], points[j]);
				dist[j][i] = dist[i][j];
			}
		}
	}
	
	//  ========================= METODY KLASY =========================
	
	/**
	 * Zwraca ilość wierzchołków grafu.
	 *
	 * @return	Ilość wierzchołków
	 */
	public int getCount()
	{
		return dist.length;
	}
	
	/**
	 * Zwraca odległość między dwoma wierzchołkami
	 * 
	 * @param p1	Punkt 1
	 * @param p2	Punkt 2
	 * @return	Odległość
	 */
	public double getDistance(int p1, int p2) throws DistanceFromSelfRequestException
	{
		if(p1 == p2)
		{
			throw new DistanceFromSelfRequestException(this, p1);
		}
		
		return dist[p1][p2];
	}
	
	/**
	 * Ustawia odległość między dwoma wierzchołkami
	 * 
	 * @param p1	Punkt 1
	 * @param p2	Punkt 2
	 * @param distance	Odległość
	 */
	public void setDistance(int p1, int p2, double distance) throws DistanceFromSelfRequestException
	{
		if(p1 == p2)
		{
			throw new DistanceFromSelfRequestException(this, p1);
		}
		
		dist[p1][p2] = distance;
		dist[p2][p1] = distance;
	}
	
	/**
	 * Zwraca długość ścieżki na podstawie tablicy punktów
	 * 
	 * @param pointIndicies	Tablica indeksów punktów
	 * @return	Długość ścieżki
	 */
	public double getPathLength(int[] pointIndicies)
	{
		double res = 0.0;
		
		for(int i = 0; i < pointIndicies.length-1; i++)
		{
			res += dist[pointIndicies[i]][pointIndicies[i+1]];
		}
		
		return res;
	}
}
