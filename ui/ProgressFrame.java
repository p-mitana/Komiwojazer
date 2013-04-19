package ui;

import java.awt.*;
import javax.swing.*;

/**
 * Klasa wyświetla okienko z informacją o postępie
 */
public class ProgressFrame extends JFrame
{
	/** Ikona postępu */
	JLabel progressLabel;
	
	/** Pasek postępu */
	JProgressBar progressBar;
	
	/**
	 * Konstruktor klasy
	 */
	 public ProgressFrame()
	 {
	 	setTitle("Obliczanie...");
		setLayout(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
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
	
	/**
	 * Wyświetla i zeruje
	 */
	public void start()
	{
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
		progressBar.setValue(progress);
		progressLabel.setText(String.format("Postęp obliczeń: %.1f%%", (double) progress / 10.0));
		
		if(progress == 1000)
		{
			setVisible(false);
		}
	}
}
