package model;

/**
 * Wyjątek wyrzucany przez klasę Model przy jakimś błędzie związanym z
 * interfejsem lub symulacją (np. próba rozpoczącia zakończonej już
 * symulacji itp.
 * 
 * @see AbstractModel
 */
public class ModelException extends Exception
{
	//  ========================= POLA KLASY =========================
	
	/** Graf, który rzuci ten wyjątek */
	String message;
	
	//  ========================= KONSTRUKTORY =========================
	
	/** Konstruktor wyjątku
	 *
	 * @param msg	Wiadomość
	 */
	public ModelException(String msg)
	{
		message = msg;
	}
	
	//  ========================= METODY KLASY =========================
	
	/**
	 * Zwraca reprezentację tekstową wyjątku
	 */
	public String toString()
	{
		return "Błąd symulacji: " + message;
	}
}
