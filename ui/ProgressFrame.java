package ui;

import java.awt.*;
import javax.swing.*;

/**
 * Klasa wyświetla okienko z informacją o postępie
 */
public class ProgressFrame extends JFrame
{
	//  ========================= POLA KLASY ========================
	
	/** Ikona postępu */
	private JLabel progressLabel;
	
	/** Pasek postępu */
	private JProgressBar progressBar;
	
	/** Aktualny postęp */
	private int progress;
	
	//  ========================= KONSTRUKTORY KLASY ========================
	/**
	 * Konstruktor klasy
	 */
	 public ProgressFrame()
	 {
	 	setTitle("Obliczanie...");
		setLayout(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		
		progressLabel = new JLabel();
		progressLabel.setVisible(true);
		progressLabel.setBounds(10, 10, 200, 30);
		add(progressLabel);
		
		progressBar = new JProgressBar();
		progressBar.setVisible(true);
		progressBar.setBounds(10, 40, 200, 5);
		progressBar.setMinimum(0);
		progressBar.setMaximum(1000);
		add(progressBar);
		
		getContentPane().setPreferredSize(new Dimension(220, 55));
		pack();
		setLocationRelativeTo(null);
		setVisible(false);
	}
	
	//  ========================= METODY KLASY ========================
	/**
	 * Wyświetla i zeruje
	 */
	public void start()
	{
		progress = 0;
		progressBar.setValue(0);
		progressLabel.setText("Postęp obliczeń: 0.0%");
		setVisible(true);
	}
	
	/**
	 * Metoda aktualizująca
	 * 
	 * @param progress	Postęp w promilach (1/1000)
	 */
	public void update(int progress)
	{
		this.progress = progress;
		progressBar.setValue(progress);
		progressLabel.setText(String.format("Postęp obliczeń: %.1f%%", (double) progress / 10.0));
		
		if(progress == 1000)
		{
			setVisible(false);
		}
	}
	
	/**
	 * Pobiera aktualny postęp
	 * 
	 * @return	Postęp jako liczba 1-1000
	 */
	public int getProgress()
	{
		return progress;
	}
}
