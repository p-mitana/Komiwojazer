package alg;

import model.*;

/**
 * Wyjątek wyrzucany przez Hamilton, gdy próbujemy stworzyć
 * mniej niż trzyelementowy cykl.
 * 
 * @see FullGraph
 */
public class EvolutionException extends ModelException
{
	//  ========================= KONSTRUKTORY KLASY =========================
	
	/**
	 * Konstruuje wyjątek z wiadomością.
	 * 
	 * @param message	Wiadomość
	 */
	public EvolutionException(String message)
	{
		super(message);
		this.message = message;
	}
}
