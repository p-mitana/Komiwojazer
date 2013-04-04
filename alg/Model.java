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
public abstract class Model extends AbstractModel
{
	//  ========================= POLA KLASY =========================
	
	// ------ Parametry środowiska ------
	
	/** Ekosystem */
	private Ecosystem ecosystem;
	
	/** Graf pełny */
	private FullGraph graph;
	
	/** Rodzice pierwszego pokolenia */
	private Hamilton[] firstParents;
	
	/** Limit rodziców w każdym pokoleniu */
	private int parentCount;
	
	/** Limit dzieci w każdym pokoleniu */
	private int childCount;
	
	/** Prawdopodobieństwo zaistnienia mutacji */
	private double mutationFactor;
	
	// ------ Parametry symulacji ------
	
	/** Ilość pokoleń, które już minęły */
	private int generationsPassed;
	
	/** Aktualnie najkrótszy cykl  */
	private Hamilton currentBest;
	
	//  ========================= KONSTRUKTORY KLASY =========================
	
	/**
	 * Pusty konstruktor klasy.
	 */
//	public Model(){}
	
	//  ========================= METODY KLASY =========================
	

}

