package ui;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
/**
 * Klasa wyświetla okienko z informacją o postępie
 */
public class ProgressFrame extends JFrame implements ActionListener
{
	//  ========================= POLA KLASY ========================
	
	/** Ikona postępu */
	private JLabel progressLabel;
	
	/** Etykieta pozostałego czasu */
	private JLabel remainingLabel;
	
	/** Pasek postępu */
	private JProgressBar progressBar;
	
	/** Aktualny postęp */
	private double progress;
	
	/** Czas ostatniej aktualizacji */
	private long lastUpdate = -1;
	
	/** Pozostały do końca czas */
	private String dateString;
	
	/** Postęp przy ostatniej aktualizacji czasu */
	private double lastProgress;
	
	/** Sumaryczny czas od ostatniej "policzonej" aktualizacji - do policzenia średniej */
	private long timeToAverage;
	
	/** Liczba aktualizacji postępu w ostatnim czasie - do policzenia średniej */
	private long updateCount;
	
	/** Timer aktualizujący etykietę daty */
	private Timer updateTimer;
	
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
		progressLabel.setBounds(10, 10, 400, 20);
		add(progressLabel);
		
		remainingLabel = new JLabel();
		remainingLabel.setVisible(true);
		remainingLabel.setBounds(10, 30, 400, 20);
		add(remainingLabel);
		
		progressBar = new JProgressBar();
		progressBar.setVisible(true);
		progressBar.setBounds(10, 50, 400, 5);
		progressBar.setMinimum(0);
		progressBar.setMaximum(1000);
		add(progressBar);
		
		updateTimer = new Timer(500, this);
		updateTimer.setRepeats(true);
		
		getContentPane().setPreferredSize(new Dimension(420, 65));
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
		progress = 0.0;
		lastProgress = 0.0;
		progressBar.setValue(0);
		timeToAverage = 0;
		updateCount = 0;
		lastUpdate = System.currentTimeMillis();
		progressLabel.setText("Postęp obliczeń: 0.0%");
		remainingLabel.setText("Pozostało: ?");
		dateString = "?";
		setVisible(true);
		updateTimer.start();
	}
	
	/**
	 * Metoda aktualizująca
	 * 
	 * @param progress	Postęp w promilach (1/1000)
	 */
	public void update(double progress)
	{
		this.progress = progress;
		progressBar.setValue((int) progress);
		
		dateString = "?";
		long now = System.currentTimeMillis();
		
		if(lastUpdate > 0)
		{
			long res = now - lastUpdate;
			timeToAverage += res;  // Dodanie czasu od ostatniej aktualizacji
			updateCount++;
			
			res = (timeToAverage / updateCount);  // Średni czas między kolejnymi aktualizacjami
			long updatesLeft = (long) ((updateCount * (1000-progress)) / (progress-lastProgress));
			
			res *= updatesLeft;  // Pozostały do końca czas
			
			if(res < 0)  // Czas jeszcze nieznany
			{
				dateString = "?";
			}
			
			else if(res < 1000)  // < 1s
			{
				dateString = res + " ms";
			}
			
			else if((res /= 1000) < 60)  // < 1min - obcinamy milisekundy
			{
				dateString = res + " s";
			}
			
			else if(res < 3600)  // < 1h
			{
				dateString = res/60 + " min, " + res%60 + " s";
			}
			
			else if((res /= 60) < 24*60)  // < 1 dzień - obcinamy sekundy
			{
				dateString = res/60 + " h, " + res%60 + " min";
			}
			
			else if((res /= 60) < 7*24)  // < 1 tydzień - obcinamy minuty
			{
				dateString = res/24 + " dni, " + res%24 + " h";
			}
			
			else if((res /= 24) < 365)  // < 1 rok - obcinamy godziny
			{
				dateString = res + " dni";
			}
			
			else if(res < 365*4+1)  // < 4 lata + 1 dzień na rok przestępny
			{
				dateString = res/365 + " lat, " + res + " dni";
			}
			
			else
			{
				long years = 4*(res/(365*4+1));
				res /= 365*4+1;
				years += res/365;
				
				dateString = res/365 + " lat";
			}
		}
		else
		{
			dateString = "?";
		}
		
		progressLabel.setText(String.format("Postęp obliczeń: %.1f%%", (double) progress / 10.0));
		
		if(progress >= 1000.0)
		{
			updateTimer.stop();
			setVisible(false);
		}
		
		lastUpdate = now;
	}
	
	/**
	 * Metoda listenera, aktualizuje timer
	 */
	public void actionPerformed(ActionEvent e)
	{
		// Resetowanie okresu zbierania danych czasowych
		updateCount = 0;
		timeToAverage = 0;
		lastProgress = progress;
		
		// Aktualizacja etykiety
		remainingLabel.setText(String.format("Pozostało: %s", dateString));
	}
	
	/**
	 * Pobiera aktualny postęp
	 * 
	 * @return	Postęp jako liczba 1-1000
	 */
	public double getProgress()
	{
		return progress;
	}
}
