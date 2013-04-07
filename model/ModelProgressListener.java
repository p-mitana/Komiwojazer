package model;

/**
 * Ten interfejs pozwala na przekazywanie informacji o postępie
 * interfejsowi przez model.
 */
public interface ModelProgressListener
{
	/**
	 * Metoda wysyłająca aktualizację postępu. Wywoływana po każdym
	 * pokoleniu.
	 * 
	 * @param progress	Akualny postęp w ułamku (1 - ukończone)
	 */
	public void progressUpdate(double progress);
}
