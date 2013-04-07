package alg;

import model.*;

/**
 * Wyjątek wyrzucany przez klasę Ecosystem w przypadku np. błędnych
 * parametrów ekosystemu.
 * 
 * @see FullGraph
 */
public class EcosystemException extends ModelException
{
	//  ========================= KONSTRUKTORY =========================
	
	/** Konstruktor wyjątku
	 *
	 * @param msg	Wiadomość
	 */
	public EcosystemException(String msg)
	{
		super(msg);
		
		this.message = msg;
	}
}
