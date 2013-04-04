package alg;

/**
 * Wyjątek wyrzucany przez klasę Ecosystem w przypadku np. błędnych
 * parametrów ekosystemu.
 * 
 * @see FullGraph
 */
public class EcosystemException extends Exception
{
	//  ========================= POLA KLASY =========================
	
	/** Wiadomość */
	String message;
	
	//  ========================= KONSTRUKTORY =========================
	
	/** Konstruktor wyjątku
	 *
	 * @param msg	Wiadomość
	 */
	public EcosystemException(String msg)
	{
		this.message = msg;
	}
	
	//  ========================= METODY KLASY =========================
	
	/**
	 * Zwraca reprezentację tekstową wyjątku
	 */
	public String toString()
	{
		return message;
	}
}
