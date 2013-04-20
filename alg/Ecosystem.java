package alg;

import java.util.*;
import java.util.concurrent.*;

/**
 * Ta klasa reprezentuje ekosystem.
 * 
 * Ekosystem składa się z:
 * - Środowiska, czyli grafu
 * - Zbioru osobników, czyli cykli Hamiltona dla tego grafu
 * - Funkcji oceniającej przystosowanie
 * 
 * Zbiór osobników dzieli się na dwie tablice - rodziców i dzieci. Co
 * pokolenie każde dziecko jest testowane, a następnie najsilniejsza
 * grupa dzieci staje się rodzicami następnego pokolenia.
 */
public class Ecosystem
{
	//  ========================= KLASY WEWNĘTRZNE =========================
	
	/**
	 * Klasa umożliwia obliczenia wielowątkowe
	 */
	class ThreadProcessor extends Thread
	{
		/** Lista indeksów i */
		ArrayList<Hamilton> list1;
	
		/** Lista indeksów j */
		ArrayList<Hamilton> list2;
		
		/** Lista pozycji docelowych */
		ArrayList<Integer> targetIndexList;
		
		/** Lista pozycji docelowych */
		Hamilton[] targetList;
		
		/** Obiekt losujący */
		Random random;
		
		/** Obiekt do obudzenia */
		Object notifyObject;
		
		/**
		 * Konstruktor klasy
		 * 
		 * @param notifyObject	Obiekt do obudzenia po zakończeniu działania
		 */
		public ThreadProcessor(Object notifyObject)
		{
			list1 = new ArrayList<Hamilton>();
			list2 = new ArrayList<Hamilton>();
			targetIndexList = new ArrayList<Integer>();
			random = new Random();
			this.notifyObject = notifyObject;
		}
		
		/**
		 * Metoda główna wątku
		 */
		public void run()
		{
			targetList = new Hamilton[targetIndexList.size()];
			
			for(int i = 0; i < targetIndexList.size(); i++)
			{
				try
				{
					Hamilton ham = Hamilton.crossing(list1.get(i), list2.get(i));
					
					// Mutacja
					int x = random.nextInt(1000000);
					if((double) x < (mutationFactor * 1000000))
					{
						ham.mutate();
					}
					
					synchronized(this)
					{
						children[targetIndexList.get(i)] = ham;
					}
				}
				catch(EvolutionException ex)
				{
					ex.printStackTrace();
				}
			}
			
			latch.countDown();
		}
	}
	
	//  ========================= POLA KLASY =========================
	
	/** Graf pełny. */
	FullGraph graph;
	
	/** Tablica rodziców */
	Hamilton[] parents;
	
	/** Tablica dzieci */
	Hamilton[] children;
	
	/** Szansa zaistnienia mutacji */
	double mutationFactor;
	
	/** Ilość wątków */
	ThreadProcessor[] threads;
	
	/** Liczba wątków */
	int threadCount = 8;
	
	/** Bramka czekająca */
	CountDownLatch latch;
	
	//  ========================= KONSTRUKTORY KLASY =========================
	
	/**
	 * Konstruktor klasy.
	 * 
	 * @param graph	Graf pełny
	 * @param parentCount	Rozmiar tablicy rodziców
	 * @param childCount	Rozmiar tablicy dzieci - >= parentCount
	 * @param parents	Tablica pierwszego pokolenia
	 * @param mutationFactor	Szansa zaistnienia mutacji
	 */
	public Ecosystem(FullGraph graph, int parentCount, int childCount, Hamilton[] parents, double mutationFactor) throws EcosystemException
	{
		// Sprawdzenie danych
		if(graph == null)
		{
			throw new EcosystemException("Błąd tworzenia ekosystemu: graf niezainicjalizowany.");
		}
		
		if(childCount < parentCount)
		{
			throw new EcosystemException("Błąd tworzenia ekosystemu: ilość dzieci musi być większa od ilości rodziców.");
		}
		
		if(mutationFactor < 0.0 || mutationFactor > 1.0)
		{
			throw new EcosystemException("Błąd tworzenia ekosystemu: nieprawidłowe prawdopodobieństwo mutacji.");
		}
		
		// Inicjalizacja pól
		this.graph = graph;
		this.parents = new Hamilton[parentCount];
		this.children = new Hamilton[childCount];
		this.mutationFactor = mutationFactor;
		
		this.threads = new ThreadProcessor[threadCount];
		// Wybranie najlepszych rodziców
		sortArray(parents);
		for(int i = 0; i < this.parents.length; i++)
		{
			this.parents[i] = (i < parents.length) ? parents[i] : null;  // Operator ? : ochroni mnie przed błędem ArrayIndex...
		}
		
		latch = new CountDownLatch(threads.length);
	}
	
	//  ========================= METODY KLASY =========================
	
	/**
	 * Funkcja realizuje jedno pokolenie symulacji
	 * 
	 * Krzyżowanie preferuje najlepszych rodziców.
	 */
	public void nextGeneration() throws EvolutionException
	{
		Random random = new Random();
		
		// Proces krzyżowania
		int childrenCreated = 0;
		
		// Indeks wątku
		int threadIndex = 0;
		
		// Stworzenie wątków
		for(int i = 0; i < threads.length; i++)
		{
			threads[i] = new ThreadProcessor(this);
		}

		label:
		for(int i = 0; i < parents.length; i++)
		{
			for(int j = 0; j < i; j++)
			{
				if(i == j)
				{
					continue;
				}
				
				if(parents[i] == null || parents[j] == null)
				{
					break label;
				}
				
				// Ustalenie wątku
				ThreadProcessor thread = threads[threadIndex];
				threadIndex = (threadIndex == threads.length-1) ? 0 : threadIndex+1;
				
				// Zlecenie obliczeń wątkowi
				thread.list1.add(new Hamilton(parents[i]));
				thread.list2.add(new Hamilton(parents[j]));
				thread.targetIndexList.add(childrenCreated);
				
				// Zwiększenie zmiennej i sprawdzenie kontynuacji
				childrenCreated++;
				
				// Weryfikacja kontynuacji pętli
				if(childrenCreated == children.length)
				{
					break label;
				}
			}
		}
		
		// Uruchomienie wątków i odczekanie na ich wykonanie
		for(int i = 0; i < threads.length; i++)
		{
			threads[i].start();
		}
		
		// Przespanie czasu obliczeń tamtych wątków
		
		try
		{
			latch.await();
		}
		catch(InterruptedException ex)
		{
			ex.printStackTrace();
		}
		
		synchronized(this)
		{
			boolean anyAlive = true;
			
			while(anyAlive)
			{
				anyAlive = false;
				for(int i = 0; i < threads.length; i++)
				{
					anyAlive = anyAlive || threads[i].isAlive();
				}
			}
		}
		
		// Proces odrzucania najsłabszych osobników i dorastania najsilniejszych
		sortArray(children);
		
		for(int i = 0; i < parents.length; i++)
		{
			parents[i] = children[i];
		}
		
		for(int i = 0; i < children.length; i++)
		{
			children[i] = null;
		}
	}
	
	/**
	 * Funkcja sortująca tablicę według przystosowania w miejscu
	 * 
	 * @param array	Tablica
	 */
	private void sortArray(Hamilton[] array)
	{
		double[] vals = new double[array.length];
		final int validCount;  // Liczba osobników w tej tablicy (tablica nie musi być pełna)
		
		// Ocenienie osobników
		int i = 0;
		for(; i < vals.length; i++)
		{
			if(array[i] != null)
			{
				vals[i] = mark(array[i]);
			}
			else break;
		}
		
		validCount = i;
		
		// Wypełnienie pozostałych wartości NaN, jeżeli osobników jest mnie, niż miejsc
		for(; i < vals.length; i++)
		{
			vals[i] = Double.NaN;
		}
		
		// Sortowanie według przystosowania
		for(i = 1; i < validCount; i++)
		{
			double keyVal = vals[i];
			Hamilton keyHam = array[i];
			
			int j;
			
			for(j = i-1; (j >= 0) && vals[j] < keyVal; j--)
			{
				vals[j+1] = vals[j];
				array[j+1] = array[j];
			}
			
			vals[j+1] = keyVal;
			array[j+1] = keyHam;
		}
	}
	
	/**
	 * Zwraca kopię tablicy rodziców
	 *
	 * @return	Kopia tablicy rodziców
	 */
	public Hamilton[] getParentArrayCopy()
	{
		Hamilton[] res = new Hamilton[parents.length];
		
		for(int i = 0; i < res.length; i++)
		{
			res[i] = new Hamilton(parents[i]);
		}
		
		return res;
	}
	
	/**
	 * Zwraca kopię tablicy dzieci
	 *
	 * @return	Kopia tablicy dzieci
	 */
	public Hamilton[] getChildArrayCopy()
	{
		Hamilton[] res = new Hamilton[children.length];
		
		for(int i = 0; i < res.length; i++)
		{
			res[i] = new Hamilton(children[i]);
		}
		
		return res;
	}
	
	/**
	 * Funkcja sprawdzająca przystosowanie - w tym wypadku minus długość cyklu
	 *
	 * @param ham	Cykl do sprawdzenia
	 * @return	Wskaźnik przystosowania
	 */
	public double mark(Hamilton ham)
	{
		return -1*graph.getPathLength(ham.getFenotype());
	}
	 
}

