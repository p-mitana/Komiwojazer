package alg;

/**
 * Wyjątek wyrzucany przez Hamilton, gdy próbujemy stworzyć
 * mniej niż trzyelementowy cykl.
 * 
 * @see FullGraph
 */
public class EvolutionException extends Exception
{
	//  ========================= POLA KLASY =========================
	
	/** Wiadomość o błędzie */
	String message;
	
	//  ========================= KONSTRUKTORY KLASY =========================
	
	/**
	 * Konstruuje wyjątek z wiadomością.
	 * 
	 * @param message	Wiadomość
	 */
	public EvolutionException(String message)
	{
		this.message = message;
	}
	
	//  ========================= METODY KLASY =========================
	
	/**
	 * Zwraca reprezentację tekstową wyjątku
	 */
	public String toString()
	{
		return "Błąd ewolucji: " + message;
	}
}
