package alg;

import model.*;

/**
 * Wyjątek wyrzucany przez Hamilton, gdy próbujemy stworzyć
 * mniej niż trzyelementowy cykl.
 * 
 * @see FullGraph
 */
public class TooLowCountException extends ModelException
{
	//  ========================= POLA KLASY =========================
	
	/** Liczba elementów */
	int count;
	
	//  ========================= KONSTRUKTORY =========================
	
	/** Konstruktor wyjątku
	 *
	 * @param count	Liczba elementów
	 */
	public TooLowCountException(int count)
	{
		super(String.format("Za którtki cykl Hamiltona: %d!", count));
		
		this.count = count;
	}
	
	//  ========================= METODY KLASY =========================
	
	/**
	 * Zwraca błędną liczbę elementów, którą próbowano przypisać cyklowi
	 * 
	 * @return	Liczba elementów
	 */
	public int getPointIndex()
	{
		return count;
	}
	
	/**
	 * Zwraca reprezentację tekstową wyjątku
	 */
	public String toString()
	{
		return String.format("Za którtki cykl Hamiltona: %d!", count);
	}
}
