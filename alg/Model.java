package alg;

import model.*;

import java.util.*;

/**
 * Ta klasa jest modelem, czyli obiektem pośredniczącym między interfejsem,
 * a algorytmem. Model nie może przyjmować jako argumentów obiektów klas
 * powiązanych z algorytmem, dzięki czemu interfejs i algorytm mogą być od
 * siebie niezależne - wymiana algorytmu nie wymusza modyfikacji interfejsu
 * i odwrotnie, o ile jedno i drugie zachowuje zgodność z modelem. 
 */
public class Model extends AbstractModel
{
	//  ========================= KLASY WEWNĘTRZNE =========================
	
	/**
	 * Klasa będąca wątkiem symulującym.
	 */
	class Simulator extends Thread
	{
		/** Ekosystem */
		private Ecosystem ecosystem;
		
		/** Słuchacz */
		ModelProgressListener listener;
		
		/** Ilość pokoleń - można zmieniać bezpośrednio */
		int generationCount = 1;
		
		/**
		 * Konstruktor klasy
		 */
		public Simulator(Ecosystem ecosystem, ModelProgressListener listener)
		{
			this.ecosystem = ecosystem;
			this.listener = listener;
		}
		
		/**
		
		/**
		 * Metoda uruchomieniowa
		 */
		public void run()
		{
			for(int i = 0; i < generationCount; i++)
			{
				try
				{
					ecosystem.nextGeneration();
				}
				catch(EvolutionException ex)  // Błąd - trzeba od razu przechwycić
				{
					ex.printStackTrace();
					System.exit(0);
				}
				
				if(listener != null)
				{
					listener.progressUpdate(((double) i) / ((double) generationCount));
				}
			}
		}
	}
	
	//  ========================= POLA KLASY =========================
	
	/** Wątek symulujący */
	private Simulator simulator;
	
	// ------ Parametry środowiska ------
	
	/** Ekosystem */
	private Ecosystem ecosystem;
	
	/** Graf pełny */
	private FullGraph graph;
	
	/** Rodzice pierwszego pokolenia */
	private ArrayList<Hamilton> firstParents;
	
	/** Limit rodziców w każdym pokoleniu */
	private int parentCount;
	
	/** Limit dzieci w każdym pokoleniu */
	private int childCount;
	
	/** Prawdopodobieństwo zaistnienia mutacji */
	private double mutationFactor;
	
	// ------ Parametry symulacji ------
	
	/** Ilość pokoleń, które już minęły */
	private int generationsPassed;
	
	//  ========================= KONSTRUKTORY KLASY =========================
	
	/**
	 * Konstruktor klasy.
	 */
	public Model()
	{
		firstParents = new ArrayList<Hamilton>();
	}
	
	//  ========================= METODY KLASY =========================
	
	// ------ Przygotowanie środowiska do symulacji ------
	
	/**
	 * Przygotowuje środowisko do symulacji. Wszystkie dane muszą być
	 * ustawione przed wywołaniem tej metody.
	 */
	public void initialize() throws ModelException
	{
		ecosystem = new Ecosystem(graph, parentCount, childCount, firstParents.toArray(new Hamilton[firstParents.size()]), mutationFactor);
		simulator = new Simulator(ecosystem, listener);
	}
	
	// ------ Parametry środowiska ------
	
	/**
	 * Tworzy graf na podstawie tablicy odległości
	 * 
	 * @param dist	Tablica odległości
	 */
	public void createGraph(double[][] dist)
	{
		graph = new FullGraph(dist.length);
		
		for(int i = 0; i < dist.length; i++)
		{
			for(int j = 0; j < i; j++)
			{
				try
				{
					graph.setDistance(i, j, dist[i][j]);
				}
				catch(DistanceFromSelfRequestException ex){}  // Po prostu pominie i=j
			}
		}
	}
	
	/**
	 * Tworzy graf na podstawie współrzędnych wierzchołków.
	 * 
	 * @param x	Tablica współrzędnych X wierzchołków
	 * @param y	Tablica współrzędnych Y wierzchołków
	 */
	public void createPlanarGraph(double[] x, double[] y)
	{
		Point[] points = new Point[x.length];
		
		for(int i = 0; i < points.length; i++)
		{
			points[i] = new Point(x[i], y[i]);
		}
		
		graph = new FullGraph(points);
	}
	
	/**
	 * Dodaje rodzica (zapisanego w postaci kolejnych wierzchołków)
	 * do tablicy pierwszych rodziców
	 * 
	 * @param parent	Rodzic
	 */
	public void addParent(int[] parent) throws ModelException
	{
		Hamilton hamilton = new Hamilton(parent);
		firstParents.add(hamilton);
	}
	
	/**
	 * Usuwa rodzica na określonym miejscu na liście
	 * 
	 * @param index	Indeks rodzica na liście
	 */
	public void removeParentAt(int index) throws ArrayIndexOutOfBoundsException
	{
		if(index < 0 || index > firstParents.size())
		{
			throw new ArrayIndexOutOfBoundsException(index);
		}
		
		firstParents.remove(index);
	}
	
	/**
	 * Zwraca rodzica na określonym miejscu na liście
	 * 
	 * @param index	Indeks rodzica na liście
	 * 
	 * @return	Rodzic
	 */
	public int[] getParentAt(int index) throws ArrayIndexOutOfBoundsException
	{
		if(index < 0 || index > firstParents.size())
		{
			throw new ArrayIndexOutOfBoundsException(index);
		}
		
		return firstParents.get(index).getFenotype();
	}
	
	/**
	 * Ustawia limit ilości rodziców
	 * 
	 * @param parentLimit	Limit rodziców
	 */
	public void setParentLimit(int parentLimit)
	{
		parentCount = parentLimit;
	}
	
	/**
	 * Zwraca limit ilości rodziców
	 * 
	 * @return	Limit rodziców
	 */
	public int getParentLimit()
	{
		return parentCount;
	}
	
	/**
	 * Ustawia limit ilości dzieci
	 * 
	 * @param childLimit	Limit dzieci
	 */
	public void setChildLimit(int childLimit)
	{
		childCount = childLimit;
	}
	
	/**
	 * Zwraca limit ilości dzieci
	 * 
	 * @return	Limit dzieci
	 */
	public int getChildLimit()
	{
		return childCount;
	}
	
	/**
	 * Ustawia prawdopodobieństwo mutacji
	 * 
	 * @param mutationFactor	Prawdopodobieństwo mutacji
	 */
	public void setMutationFactor(double mutationFactor)
	{
		this.mutationFactor = mutationFactor;
	}
	
	/**
	 * Zwraca prawdopodobieństwo mutacji
	 * 
	 * @return	Prawdopodobieństwo mutacji
	 */
	public double getMutationFactor()
	{
		return mutationFactor;
	}
	
	// ------ Parametry symulacji ------
	
	/**
	 * Zwraca ilość pokoleń, która minęła
	 *
	 * @return	Ilość pokoleń, która minęła
	 */
	public int getGenerationsPaseed()
	{
		return generationsPassed;
	}
	
	/**
	 * Zwraca aktualnie najkrótszy cykl
	 * 
	 * @return Najkrótszy cykl
	 */
	public int[] getCurrentBest()
	{
		return getCurrentParents()[0];
	}
	
	// ------ Metody kontrolujące symulację ------
	
	/**
	 * Przeprowadza symulację danej ilości pokoleń
	 * 
	 * @param generations	Liczba pokoleń
	 */
	public void simulate(int generations)
	{
		simulator.generationCount = generations;
		
		simulator.start();
	}
	
	// ------ Wyniki symulacji ------
	
	/**
	 * Zwraca aktualną tablicę rodziców
	 *
	 * @return	Aktualna tablica rodziców
	 */
	public int[][] getCurrentParents()
	{
		Hamilton[] parents = ecosystem.getParentArrayCopy();
		int[][] res = new int[parents.length][];
		
		for(int i = 0; i < res.length; i++)
		{
			res[i] = parents[i].getFenotype();
		}
		
		return res;
	}
	
	/**
	 * Zwraca aktualną tablicę rodziców
	 *
	 * @return	Aktualna tablica rodziców
	 */
	public int[][] getCurrentChildren()
	{
		Hamilton[] children = ecosystem.getChildArrayCopy();
		int[][] res = new int[children.length][];
		
		for(int i = 0; i < res.length; i++)
		{
			res[i] = children[i].getFenotype();
		}
		
		return res;
	}
}

