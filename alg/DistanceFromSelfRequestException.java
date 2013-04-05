package alg;

import model.*;

/**
 * Wyjątek wyrzucany przez klasę FullGraph przy zapytaniu o odległość
 * wierzchołka od siebie samego. Można by zwrócić zero, ale ponieważ
 * takie zapytanie świadczy o błędnym działaniu algorytmu, zdecydowałem
 * się na wyrzucenie wyjątku.
 * 
 * @see FullGraph
 */
public class DistanceFromSelfRequestException extends ModelException
{
	//  ========================= POLA KLASY =========================
	
	/** Graf, który rzuci ten wyjątek */
	FullGraph graph;
	
	/** Indeks punktu, który wywołał błąd */
	int index;
	
	//  ========================= KONSTRUKTORY =========================
	
	/** Konstruktor wyjątku
	 *
	 * @param graph	Graf, który rzuci ten wyjątek
	 * @param index	Indeks punktu, który wywołał błąd
	 */
	public DistanceFromSelfRequestException(FullGraph graph, int index)
	{
		super(String.format("Błędne żądanie odległości punktu %d od siebie!", index));
		
		this.graph = graph;
		this.index = index;
	}
	
	//  ========================= METODY KLASY =========================
	
	/**
	 * Zwraca graf, który rzucił wyjątek
	 * 
	 * @return	Graf, który rzucił wyjątek
	 */
	public FullGraph getGraph()
	{
		return graph;
	}
	
	/**
	 * Zwraca indeks wierzchołka, który wywołał błąd
	 * 
	 * @return	Indeks wierzchołka, który wywołał błąd
	 */
	public int getPointIndex()
	{
		return index;
	}
	
	/**
	 * Zwraca reprezentację tekstową wyjątku
	 */
	public String toString()
	{
		return String.format("Błędne żądanie odległości punktu %d od siebie!", index);
	}
}
