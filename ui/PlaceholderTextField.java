package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Klasa reprezentuje pole tekstowe z placeholderem
 */
public class PlaceholderTextField extends JTextField
{
	//  ========================= POLA KLASY ========================
	
	/** Tekst placeholdera */
	String placeholder = "";
	
	//  ========================= METODY KLASY ========================
	
	/**
	 * Ustawia placeholder
	 * 
	 * @param text	Placeholder
	 */
	public void setPlaceholder(String text)
	{
		placeholder = text;
	}
	
	/**
	 * Zwraca placeholer
	 * 
	 * @return	Placeholder
	 */
	public String getPlaceholder()
	{
		return placeholder;
	}
	
	/**
	 * Metoda rysująca
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		if(getText().equals(""))
		{
			FontMetrics metrics = getFontMetrics(getFont().deriveFont(Font.ITALIC));
			
			Graphics2D g2 = (Graphics2D)g.create();
		    g2.setColor(Color.GRAY);
		    g2.setFont(getFont().deriveFont(Font.ITALIC));
		    g2.drawString(placeholder, 3, (getHeight() + metrics.getHeight()) / 2 - metrics.getDescent());
		    g2.dispose();
		}
	}
}
