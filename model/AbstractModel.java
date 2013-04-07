package model;

import java.util.*;

/**
 * Ta klasa jest modelem, czyli obiektem pośredniczącym między interfejsem,
 * a algorytmem. Model nie może przyjmować jako argumentów obiektów klas
 * powiązanych z algorytmem, dzięki czemu interfejs i algorytm mogą być od
 * siebie niezależne - wymiana algorytmu nie wymusza modyfikacji interfejsu
 * i odwrotnie, o ile jedno i drugie zachowuje zgodność z modelem. 
 */
public abstract class AbstractModel
{
	//  ========================= POLA KLASY ========================
	
	/** Słuchacz postępu */
	public ModelProgressListener listener;
	
	//  ========================= METODY KLASY =========================
	
	// ------ Przygotowanie środowiska do symulacji ------
	
	/**
	 * Przygotowuje środowisko do symulacji. Wszystkie dane muszą być
	 * ustawione przed wywołaniem tej metody.
	 */
	public abstract void initialize() throws ModelException;
	
	// ------ Parametry środowiska ------
	
	/**
	 * Tworzy graf na podstawie tablicy odległości
	 * 
	 * @param dist	Tablica odległości
	 */
	public abstract void createGraph(double[][] dist);
	
	/**
	 * Tworzy graf na podstawie współrzędnych wierzchołków.
	 * 
	 * @param x	Tablica współrzędnych X wierzchołków
	 * @param y	Tablica współrzędnych Y wierzchołków
	 */
	public abstract void createPlanarGraph(double[] x, double[] y);
	
	/**
	 * Dodaje rodzica (zapisanego w postaci kolejnych wierzchołków)
	 * do tablicy pierwszych rodziców
	 * 
	 * @param parent	Rodzic
	 */
	public abstract void addParent(int[] parent) throws ModelException;
	
	/**
	 * Usuwa rodzica na określonym miejscu na liście
	 * 
	 * @param index	Indeks rodzica na liście
	 */
	public abstract void removeParentAt(int index) throws ArrayIndexOutOfBoundsException;
	
	/**
	 * Zwraca rodzica na określonym miejscu na liście
	 * 
	 * @param index	Indeks rodzica na liście
	 * 
	 * @return	Rodzic
	 */
	public abstract int[] getParentAt(int index) throws ArrayIndexOutOfBoundsException;
	
	/**
	 * Ustawia limit ilości rodziców
	 * 
	 * @param parentLimit	Limit rodziców
	 */
	public abstract void setParentLimit(int parentLimit);
	
	/**
	 * Zwraca limit ilości rodziców
	 * 
	 * @return	Limit rodziców
	 */
	public abstract int getParentLimit();
	
	/**
	 * Ustawia limit ilości dzieci
	 * 
	 * @param childLimit	Limit dzieci
	 */
	public abstract void setChildLimit(int childLimit);
	
	/**
	 * Zwraca limit ilości dzieci
	 * 
	 * @return	Limit dzieci
	 */
	public abstract int getChildLimit();
	
	/**
	 * Ustawia prawdopodobieństwo mutacji
	 * 
	 * @param mutationFactor	Prawdopodobieństwo mutacji
	 */
	public abstract void setMutationFactor(double mutationFactor);
	
	/**
	 * Zwraca prawdopodobieństwo mutacji
	 * 
	 * @return	Prawdopodobieństwo mutacji
	 */
	public abstract double getMutationFactor();
	
	// ------ Parametry symulacji ------
	
	/**
	 * Zwraca ilość pokoleń, która minęła
	 *
	 * @return	Ilość pokoleń, która minęła
	 */
	public abstract int getGenerationsPaseed();
	
	/**
	 * Zwraca aktualnie najkrótszy cykl
	 * 
	 * @return Najkrótszy cykl
	 */
	public abstract int[] getCurrentBest();
	
	// ------ Metody kontrolujące symulację ------
	
	/**
	 * Przeprowadza symulację danej ilości pokoleń
	 * 
	 * @param generations	Liczba pokoleń
	 */
	public abstract void simulate(int generations);
	
	// ------ Wyniki symulacji ------
	
	/**
	 * Zwraca aktualną tablicę rodziców
	 *
	 * @return	Aktualna tablica rodziców
	 */
	public abstract int[][] getCurrentParents();
	
	
	/**
	 * Zwraca aktualną tablicę rodziców
	 *
	 * @return	Aktualna tablica rodziców
	 */
	public abstract int[][] getCurrentChildren();
	
	// ------ Postęp symulacji ------
	
	/**
	 * Ustawia słuchacza postępu
	 *
	 * @param listener	Słuchacz
	 */
	public void setModelProgessListener(ModelProgressListener listener)
	{
		this.listener = listener;
	}
}

