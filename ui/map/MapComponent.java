package ui.map;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

/**
 * Klasa reprezentuje komponent wyświetlający mapę.
 * 
 * Mapa składa się z warstw, na których umieszczamy obiekty.
 * Komponent ma wbudowane elementy do powiększania/pomniejszania, oraz
 * legendę i przycisk do jej wyłączenia. Obsługuje przesuwanie myszą.
 */
public class MapComponent extends JComponent implements MouseMotionListener, MouseListener, ActionListener
{
	//  ========================= POLA KLASY ========================
	
	/** Lista warstw */
	public Vector<MapLayer> layers;
	
	/** Kolor tła */
	private Color backgroundColor;
	
	/** Kolor linii siatki */
	private Color gridColor;
	
	/**
	 * Wstrzymuje rysowanie. Przydatne, gdy robione jest wiele zmian na raz i nie potrzeba
	 * odrysowywać mapy po każdej.
	 */
	private boolean repaintLocked;
	
	/** Określa, czy trwa przeciąganie myszą. Jeżeli tak, rysuje tylko siatkę */
	private boolean isDragged;
	
	/** Współrzędna X punktu na środku mapy */
	private float centerX;
	
	/** Współrzędna Y punktu na środku mapy */
	private float centerY;
	
	/** Ilość jednostek na piksel - skala powiększenia */
	private float unitsPerPixel;
	
	/** Minimalny zoom, przy którym pojawia się tekst */
	private float minimalZoomForText;
	
	/** Wielkość oczka siatki w jednostkach mapy */
	private float gridSize;
	
	/** Etykieta z informacjami o statusie. Domyślnie brak */
	private JLabel statusLabel;
	
	/** Przycisk powiększania. Wbudowany */
	private JButton zoomIn;
	
	/** Przycisk pomniejszania. Wbudowany */
	private JButton zoomOut;
	
	/** Przycisk przełączania legendy. Wbudowany */
	private JToggleButton legend;
	
	/** Punkt rozpoczęcia przeciągania myszą */
	private FPoint dragStart;
	
	/** Punkt zakończenia przeciągania myszą */
	private FPoint dragEnd;
	
	//  ========================= KONSTRUKTORY KLASY ========================
	
	/**
	 * Konstruktor - tworzy pustą mapę.
	 */
	public MapComponent()
	{
		layers = new Vector<MapLayer>();
		
		// Inicjalizacja parametrów domyślnych
		backgroundColor = new Color(255, 255, 255);  // Domyślnie biały
		gridColor = new Color(160, 160, 160);  // Domyślnie jasnoszary
		
		centerX = 0.0f;
		centerY = 0.0f;
		unitsPerPixel = 1;
		gridSize = 20;
		
		repaintLocked = false;
		isDragged = false;
		
		statusLabel = null;
		
		// Aktywacja zdarzeń myszy
		addMouseListener(this);
		addMouseMotionListener(this);
		
		// Przyciski zoomu
		zoomOut = new JButton("-");
		add(zoomOut);
		zoomOut.addActionListener(this);
		zoomOut.setActionCommand("zoomOut");
		zoomOut.setBounds(0, 0, 50, 24);		
		
		zoomIn = new JButton("+");
		add(zoomIn);
		zoomIn.addActionListener(this);
		zoomIn.setActionCommand("zoomIn");
		zoomIn.setBounds(50, 0, 50, 24);
		
		// Przycisk legendy
		legend = new JToggleButton("Legenda");
		add(legend);
		legend.addActionListener(this);
		legend.setActionCommand("legend");
		legend.setSelected(true);
		legend.setBounds(100, 0, 100, 24);
	}
	
	//  ========================= METODY KLASY ========================
	
	/**
	 * Przekształca punkt na mapie na punkt na ekranie
	 * 
	 * @param x	Współrzędna X na mapie
	 * @param y	Współrzędna Y na mapie
	 */
	protected Point mapToPixel(float x, float y)
	{
		// Współrzędne mapy w lewym górnym rogu
		float topLeftX = centerX - (float) ((int) getWidth() / 2) * unitsPerPixel;
		float topLeftY = centerY - (float) ((int) getHeight() / 2) * unitsPerPixel;
		
		return new Point((int) ((x - topLeftX) / unitsPerPixel),
						(int) ((y - topLeftY) / unitsPerPixel));
	}
	
	/**
	 * Przekształca punkt na ekranie na punkt na mapie
	 * 
	 * @param x	Współrzędna X na ekranie
	 * @param y	Współrzędna Y na ekranie
	 */
	protected FPoint pixelToMap(int x, int y)
	{
		// Współrzędne mapy w lewym górnym rogu
		float topLeftX = centerX - (float) ((int) getWidth() / 2) * unitsPerPixel;
		float topLeftY = centerY - (float) ((int) getHeight() / 2) * unitsPerPixel;
		
		return new FPoint(topLeftX + x * unitsPerPixel, topLeftY + y * unitsPerPixel);
	}
	
	/**
	 * Rysuje mapę.
	 * 
	 * @param g	Obiekt rysujący
	 */
	protected void paintComponent(Graphics gfx)
	{
		Graphics2D g = (Graphics2D) gfx;
		
		// Sprawdć blokadę rysowania
		if(repaintLocked)
		{
			return;
		}
		
		// Parametry czcionki
		FontMetrics metrics = getFontMetrics(g.getFont());
		
		// Obliczenie współrzędnych mapy w rogach
		int centerPixelX = (int) getWidth() / 2;
		int centerPixelY = (int) getHeight() / 2;
		
		// Współrzędne mapy w lewym górnym rogu
		float leftX = centerX - (float) centerPixelX * unitsPerPixel;
		float topY = centerY - (float) centerPixelY * unitsPerPixel;
		
		// Współrzędne mapy w prawym dolnym rogu
		float rightX = centerX + (float) centerPixelX * unitsPerPixel;
		float bottomY = centerY + (float) centerPixelY * unitsPerPixel;
		
		// Malowanie tła
		g.setColor(backgroundColor);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		// Malowanie siatki
		g.setColor(gridColor);
		
		// Dziwna pętla, ale upraszcza życie
		for(float x = leftX - (leftX % gridSize); x <= rightX; x += gridSize)  
		{
			int xPixel = mapToPixel(x, 0).x;
			g.drawLine(xPixel, 0, xPixel, getHeight());
		}
		
		for(float y = topY - (topY % gridSize); y <= bottomY; y += gridSize)
		{
			int yPixel = mapToPixel(0, y).y;
			g.drawLine(0, yPixel, getWidth(), yPixel);
		}
		
		// Jeśli trwa przeciąganie, nie rysuj obiektów
		if(isDragged)
		{
			paintLegend(g);
			return;
		}
		
		// Malowanie obiektów
		for(MapLayer layer : layers)  // Warstwa 0 jest warstwą na spodzie
		{
			g.setStroke(new BasicStroke(layer.getLineWidth()));
			
			if(!layer.getEnabled())
			{
				continue;
			}
			
			for(MapObject object : layer.objects)
			{
				int size = object.coords.size();
				
				int[] xcoords = new int[size];
				int[] ycoords = new int[size];
				
				/*
				 * Sprawdzanie, czy obiekt może znajdować się na widocznej
				 * części mapy. Jeśli nie, nie będzie rysowany
				 */
				boolean needsDrawing = false;
				
				float minX = 0, maxX = 0, minY = 0, maxY = 0;
				
				if(object.coords.size() > 0)
				{
					minX = object.coords.get(0).x;
					maxX = object.coords.get(0).x;
					minY = object.coords.get(0).y;
					maxY = object.coords.get(0).y;
				
					Point p = mapToPixel(object.coords.get(0).x, object.coords.get(0).y);
					xcoords[0] = p.x;
					ycoords[0] = p.y;
					
					for(int i = 1; i < object.coords.size(); i++)
					{
						FPoint fp = object.coords.get(i);
						minX = fp.x < minX ? fp.x : minX;
						maxX = fp.x > maxX ? fp.x : maxX;
						minY = fp.y < minY ? fp.y : minY;
						maxY = fp.y > maxY ? fp.y : maxY;
						
						p = mapToPixel(object.coords.get(i).x, object.coords.get(i).y);
						xcoords[i] = p.x;
						ycoords[i] = p.y;
					}
					
					needsDrawing = true;
					
					if((minX < leftX && maxX < leftX) || (minX > rightX && maxX > rightX) ||
						(minY < topY && maxY < topY) || (minY > bottomY && maxY > bottomY))
					{
						needsDrawing = false;
					}
				}
				
				// Rysowanie, jeśli trzeba
				if(needsDrawing)
				{
					// Określenie koloru
					int colorIndex = (int) ((object.height - layer.getHeightMinimum()) / layer.getHeightInterval());
					colorIndex = colorIndex < 0 ? 0 : colorIndex;
					colorIndex = colorIndex >= layer.getFillColors().size() ? layer.getFillColors().size()-1 : colorIndex;
					Color fillColor = layer.getFillColors().get(colorIndex);
					g.setColor(fillColor);
					
					// Narysuj wielokąt
					if(object.coords.size() > 1)
					{
						g.fillPolygon(xcoords, ycoords, size);
						
						if(!layer.getBorderless())
						{
							g.setColor(layer.getDrawColor());
							g.drawPolygon(xcoords, ycoords, size);
						}
					}
					
					// Jeden punkt - obrazek, kropka lub kółeczko
					else
					{
						// Symbol
						if(object.symbol != null)
						{
							g.setColor(fillColor);
							Point p = mapToPixel(object.coords.get(0).x, object.coords.get(0).y);
							
							g.drawImage(object.symbol, p.x - object.symbol.getWidth(this)/2, p.y - object.symbol.getHeight(this)/2, this);
						}
						
						// Kropka lub kółeczko
						else
						{
							Point p = mapToPixel(object.coords.get(0).x - layer.getSpotRadius(), object.coords.get(0).y - layer.getSpotRadius());
							
							// Oblicz promień punktu w pikselach
							int pointRadius = (int) (layer.getSpotRadius() / unitsPerPixel);
							pointRadius = pointRadius < 1 ? 1 : pointRadius;
							
							// Kropka - za małe na kółeczko
							if(pointRadius <= 2)
							{
								g.fillRect(p.x, p.y, 2*pointRadius, 2*pointRadius);
							}
							
							// Narysuj kółeczko
							if(!layer.getBorderless() && pointRadius > 2)
							{
								g.fillOval(p.x, p.y, 2*pointRadius, 2*pointRadius);
								g.setColor(layer.getDrawColor());
								g.drawOval(p.x, p.y, 2*pointRadius, 2*pointRadius);
							}
						}
					}
					
					// Tekst rysuj tylko przy dużym powiększeniu
					if(unitsPerPixel <= minimalZoomForText && layer.getTextVisible())
					{
						// Liczba linii
						int lines = object.text.split("\r\n|\r|\n").length;
						
						// Wymiary tekstu
						int textWidth = metrics.stringWidth(object.text);
						
						// Liczba linii * wysokość wiersza
						int textHeight = metrics.getHeight()*lines - metrics.getDescent();
						
						// Określenie środkowego punktu budynku
						int centerWidth = 0;
						int centerHeight = 0;
						
						for(int i = 0; i < xcoords.length; i++)
						{
							centerWidth += xcoords[i];
							centerHeight += ycoords[i];
						}
						
						centerWidth /= xcoords.length;
						centerHeight /= ycoords.length;
						
						// Jest punkt. Dlatego, jak jest obrazek lub zbyt małe kółeczko, trzeba odsunąć tekst.
						if(object.coords.size() == 1)
						{
							if(object.symbol != null)
							{
								centerHeight += object.symbol.getHeight(this)/2 + 5;  // Tekst będzie pod symbolem
							}
							else
							{
								int pointRadius = (int) (layer.getSpotRadius() / unitsPerPixel);
								
								// Nie mieści się w kwadracie wpisanym w kółeczko
								if(textWidth > 1.41*pointRadius || textHeight > 1.41*pointRadius)
								{
									centerHeight += pointRadius + textHeight;  // Tekst będzie pod punktem
								}
							}
						}
						
						// Narysowanie tekstu
						g.setColor(layer.getTextColor());
						g.drawString(object.text, centerWidth-(textWidth/2), centerHeight+(textHeight/2));
					}
				}
			}
		}
		
		// Namaluj legendę, jeżeli trzeba
		if(legend.isSelected())
		{
			paintLegend(g);
		}
	}
	
	/**
	 * Rysuje legendę
	 * 
	 * @param gfx	Obiekt rysujący
	 */
	protected void paintLegend(Graphics gfx)
	{
		Graphics2D g = (Graphics2D) gfx;
		
		// Parametry czcionki
		FontMetrics metrics = getFontMetrics(g.getFont());
		
		// Ilość wierszy (po dwie warstwy na wiersz)
		int rows = (layers.size()+1) / 2;
		
		// To znaczy, że przy dzieleniu była reszta.
		if((layers.size()+1) / 2 < (layers.size()+2) / 2) 
		{
			rows++;  // Jeden wiersz więcej
		}
		
		// Szerokość 2*250 + 2*10 marginesów + 10 rozdziału kolumn + 10 odstępu z prawej
		int legendLeftX = getWidth() - 540;
		
		// 2*10 - marginesy i 10 - odstęp od dołu
		int legendTopY = getHeight() - 30 - 20*(rows);
		
		g.setColor(Color.WHITE);
		g.fillRect(legendLeftX, legendTopY, 530, 20*(rows)+20);
		
		g.setColor(Color.BLACK);
		g.drawRect(legendLeftX, legendTopY, 530, 20*(rows)+20);
		
		int layerX = 10;
		int layerY = 10;
		
			// Odległości
		g.drawLine(legendLeftX + layerX, legendTopY + layerY + 5, legendLeftX + layerX, legendTopY + layerY + 15);
		g.drawLine(legendLeftX + layerX + 30, legendTopY + layerY + 5, legendLeftX + layerX + 30, legendTopY + layerY + 15);
		g.drawLine(legendLeftX + layerX, legendTopY + layerY + 10, legendLeftX + layerX + 30, legendTopY + layerY + 10);
		
		if(30*unitsPerPixel > 2000.0f)
		{
			g.drawString(String.format("30 pikseli = %.2f km", 0.03f*unitsPerPixel),
						legendLeftX + layerX + 40, 
						legendTopY + layerY + 10 + (metrics.getAscent() + metrics.getLeading())/2);
		}
		
		else if(30*unitsPerPixel > 2.0f)
		{
			g.drawString(String.format("30 pikseli = %.2f m", 30.0f*unitsPerPixel),
						legendLeftX + layerX + 40,
						legendTopY + layerY + 10 + (metrics.getAscent() + metrics.getLeading())/2);
		}
		
		else if(30*unitsPerPixel > 0.02f)
		{
			g.drawString(String.format("30 pikseli = %.2f cm", 3000.0f*unitsPerPixel),
						legendLeftX + layerX + 40,
						legendTopY + layerY + 10 + (metrics.getAscent() + metrics.getLeading())/2);
		}
		
		else
		{
			g.drawString(String.format("30 pikseli = %.2f mm", 30000.0f*unitsPerPixel),
						legendLeftX + layerX + 40,
						legendTopY + layerY + 10 + (metrics.getAscent() + metrics.getLeading())/2);
		}
					
		// Opisy warstw
		for(int i = 0; i < layers.size(); i++)
		{
			g.setStroke(new BasicStroke(layers.get(i).getLineWidth()));
			
			layerY += 20;
			
			// Do drugiej kolumny. Wcześniej narysowano odległość, więc narysowano już jeden wskaźnik więcej.
			if(i+1 == rows)
			{
				layerX += 260;
				layerY = 10;
			}
			
			if(layers.get(i).getFillColors().size() == 1)
			{
				if(layers.get(i).getFillColors().get(0).equals(new Color(0, 0, 0, 0)))
				{
					g.setColor(layers.get(i).getDrawColor());
					g.drawLine(legendLeftX + layerX, legendTopY + layerY + 2, legendLeftX + layerX + 16, legendTopY + layerY + 18);
				}
				
				else
				{
					g.setColor(layers.get(i).getFillColors().get(0));
					g.fillRect(legendLeftX + layerX, legendTopY + layerY + 2, 16, 16);
					g.setColor(layers.get(i).getDrawColor());
					g.drawRect(legendLeftX + layerX, legendTopY + layerY + 2, 16, 16);
				}
				
				g.setColor(layers.get(i).getTextColor());
				g.drawString(layers.get(i).getName(), legendLeftX + layerX + 20,
							legendTopY + layerY + 10 + (metrics.getAscent() + metrics.getLeading())/2);
			}
			else
			{
				for(int j = 0; j < layers.get(i).getFillColors().size(); j++)
				{
					int steps = layers.get(i).getFillColors().size();
					g.setColor(layers.get(i).getFillColors().get(j));
					g.fillRect(legendLeftX + layerX + j*250/steps, legendTopY + layerY, 250/steps, 4);
					g.setColor(Color.WHITE);
					g.drawLine(legendLeftX + layerX + j*250/steps, legendTopY + layerY,
								legendLeftX + layerX + j*250/steps, legendTopY + layerY+3);
					
					g.setColor(Color.BLACK);
					
					// Wysokość początkowa
					if(layers.get(i).getHeightMinimum() > 2000.0f || layers.get(i).getHeightMinimum() < -2000.0f)
					{
						g.drawString(String.format("%.2f km", layers.get(i).getHeightMinimum()*1000.0f),
									legendLeftX + layerX,
									legendTopY + layerY + 10 + (metrics.getAscent() + metrics.getLeading())/2);
					}
					
					else if(layers.get(i).getHeightMinimum() > 2.0f || layers.get(i).getHeightMinimum() < -2.0f)
					{
						g.drawString(String.format("%.2f m", layers.get(i).getHeightMinimum()),
									legendLeftX + layerX,
									legendTopY + layerY + 10 + (metrics.getAscent() + metrics.getLeading())/2);
					}
					
					else if(layers.get(i).getHeightMinimum() > 0.02f || layers.get(i).getHeightMinimum() < -0.02f)
					{
						g.drawString(String.format("%.2f cm", layers.get(i).getHeightMinimum()/100.0f),
									legendLeftX + layerX,
									legendTopY + layerY + 10 + (metrics.getAscent() + metrics.getLeading())/2);
					}
					
					else if(layers.get(i).getHeightMinimum() >= 0.001f || layers.get(i).getHeightMinimum() <= -0.001f)
					{
						g.drawString(String.format("%.2f mm", layers.get(i).getHeightMinimum()/1000.0f),
									legendLeftX + layerX,
									legendTopY + layerY + 10 + (metrics.getAscent() + metrics.getLeading())/2);
					}
					
					else  // < 1mm. Czyli pewnie 0, tylko są błędy zmiennoprzecinkowe
					{
						g.drawString("0 m", legendLeftX + layerX,
									legendTopY + layerY + 10 + (metrics.getAscent() + metrics.getLeading())/2);
					}
					
					// Wysokość końcowa
					float maxHeight = layers.get(i).getHeightMinimum() + layers.get(i).getHeightInterval() * (layers.get(i).getFillColors().size()-1);
					String text;
					
					if(maxHeight > 2000.0f || maxHeight < -2000.0f)
						text = String.format("%.2f km", maxHeight*1000.0f);
						
					else if(maxHeight > 2.0f || maxHeight < -2.0f)
						text = String.format("%.2f m", maxHeight);
						
					else if(maxHeight > 0.02f || maxHeight < -0.02f)
						text = String.format("%.2f cm", maxHeight/100.0f);
						
					else if(maxHeight >= 0.001f || maxHeight <= -0.001f)
						text = String.format("%.2f mm", maxHeight/1000.0f);
						
					else  // < 1mm. Czyli pewnie 0, tylko są błędy zmiennoprzecinkowe
						text = "0 m";
					
					int textWidth = metrics.stringWidth(text);
					g.drawString(text, legendLeftX + layerX + 250 - textWidth, legendTopY + layerY + 10 + (metrics.getAscent() + metrics.getLeading())/2);
					
					// Nazwa warstwy
					g.setColor(layers.get(i).getTextColor());
					g.drawString(layers.get(i).getName(), legendLeftX + layerX + 90, legendTopY + layerY + 10 + (metrics.getAscent() + metrics.getLeading())/2);
				}
			}
		}
	}
	
	/**
	 * Mysz wchodzi na komponent
	 * 
	 * @param evt	Zdarzenie
	 */
	public void mouseEntered(MouseEvent evt){}
	
	/**
	 * Mysz wychodzi poza komponent
	 * 
	 * @param evt	Zdarzenie
	 */
	public void mouseExited(MouseEvent evt)
	{
		if(statusLabel != null)
		{
			statusLabel.setText(" ");
		}
	}
	
	/**
	 * Naciśnięcie przycisku myszy
	 * 
	 * @param evt	Zdarzenie
	 */
	public void mousePressed(MouseEvent evt)
	{
		// Wyłączyłem rysowanie podczas przeciągania - nie ma potrzeby rysować wszystkiego.
//		isDragged = true;
		dragStart = pixelToMap(evt.getX(), evt.getY());
	}
	
	/**
	 * Puszczenie myszy
	 * 
	 * @param evt	Zdarzenie
	 */
	public void mouseReleased(MouseEvent evt)
	{
		// Odblokowanie i odrysowanie
		isDragged = false;
		repaint();
	}
	
	/**
	 * Kliknięcie myszą
	 * 
	 * @param evt	Zdarzenie
	 */
	public void mouseClicked(MouseEvent evt){}
	
	/**
	 * Przeciągnięcie myszą
	 * 
	 * @param evt	Zdarzenie
	 */
	public void mouseDragged(MouseEvent evt)
	{
		// Przesunięcie
		dragEnd = pixelToMap(evt.getX(), evt.getY());
		centerX -= dragEnd.x - dragStart.x;
		centerY -= dragEnd.y - dragStart.y;
		
		// Ustawienie tekstu etykiety statusu
		if(statusLabel != null)
		{
			FPoint fpoint = pixelToMap(evt.getX(), evt.getY());
			statusLabel.setText("x: " + fpoint.x + " y: " + fpoint.y);
		}
		
		repaint();
		
		try
		{
			Thread.currentThread().sleep(10);  // To żeby nie odrysowywać zbyt często.
		}
		catch(Exception ex){}
	}
	
	
	/**
	 * Ruch myszy
	 * 
	 * @param evt	Zdarzenie
	 */
	public void mouseMoved(MouseEvent evt)
	{
		if(statusLabel != null)
		{
			FPoint fpoint = pixelToMap(evt.getX(), evt.getY());
			statusLabel.setText("x: " + Math.round(fpoint.x*10) / 10.0 +
								" y: " + Math.round(fpoint.y*10) / 10.0);
		}
	}
	
	
	/**
	 * Obsługa zadarzeń ActionListener
	 * 
	 * @param evt	Zdarzenie
	 */
	public void actionPerformed(ActionEvent evt)
	{
		// Powiększanie
		if(evt.getActionCommand() == "zoomIn")
		{
			setUnitsPerPixel(unitsPerPixel / 1.2f);
		}
		
		// Pomniejszanie
		if(evt.getActionCommand() == "zoomOut")
		{
			setUnitsPerPixel(unitsPerPixel * 1.2f);
		}
		
		// Przełączanie legendę
		if(evt.getActionCommand() == "legend")
		{
			repaint();  // paintComponent sprawdzi, czy przycisk wciśnięty
		}
	}

	/**
	 * Ustawia kolor tła
	 * 
	 * @param c	Kolor
	 */
	public void setBackground(Color c)
	{
		backgroundColor = c;
		repaint();
	}
	
	/**
	 * Zwraca kolor tła
	 * 
	 * @return	Kolor
	 */
	public Color getBackground()
	{
		return backgroundColor;
	}
	
	/**
	 * Przesuwa mapę w poziomie tak, by podana wartość x była w środku
	 * 
	 * @param px	Współrzędna X
	 */
	public void setCenterX(float px)
	{
		centerX = px;
		repaint();
	}
	
	/**
	 * Zwraca wartość x w środku mapy
	 * 
	 * @return	Współrzędna x
	 */
	public float getCenterX()
	{
		return centerX;
	}
	
	/**
	 * Przesuwa mapę w pionie tak, by podana wartość y była w środku
	 * 
	 * @param py	Współrzędna Y
	 */
	public void setCenterY(float py)
	{
		centerY = py;
		repaint();
	}
	
	/**
	 * Zwraca wartość y w środku mapy
	 * 
	 * @return	Współrzędna y
	 */
	public float getCenterY()
	{
		return centerY;
	}
	
	/**
	 * Przesuwa mapę tak, by punkt o podanych współrzędnych był w środku
	 * 
	 * @param px	Współrzędna X
	 * @param py	Współrzędna Y
	 */
	public void setCenterPoint(float px, float py)
	{
		centerX = px;
		centerY = py;
		repaint();
	}
	
	/**
	 * Ustawia powiększenie mapy
	 * 
	 * @param zoom	Powiększenie w jednostkach na piksel
	 */
	public void setUnitsPerPixel(float zoom)
	{
		// Limity powiększenia
		if(unitsPerPixel > 10000.0f || unitsPerPixel < 0.01f)
		{
			return;
		}
		
		unitsPerPixel = zoom;
		repaint();
		
		zoomIn.setEnabled(unitsPerPixel / 1.2 >= 0.01f);
		zoomOut.setEnabled(unitsPerPixel * 1.2 <= 10000f);
	}
	
	/**
	 * Zwraca powiększenie
	 * 
	 * @return	Powiększenie w jednostkach na piksel
	 */
	public float getUnitsPerPixel()
	{
		return unitsPerPixel;
	}
	
	/**
	 * Ustawia minimalne powiększenie, przy którym widać tekst
	 * 
	 * @param zoom	Powiększenie w jednostkach na piksel
	 */
	public void setMinimalZoomForText(float zoom)
	{
		boolean repaint = (zoom > unitsPerPixel && minimalZoomForText < unitsPerPixel) ||
							(zoom < unitsPerPixel && minimalZoomForText > unitsPerPixel);
		
		minimalZoomForText = zoom;
		
		if(repaint)
		{
			repaint();
		}
	}
	
	/**
	 * Zwraca minimalne powiększenie, przy którym widać tekst
	 * 
	 * @return	Powiększenie w jednostkach na piksel
	 */
	public float getMinimalZoomForText()
	{
		return minimalZoomForText;
	}
	
	/**
	 * Ustawia rozmiar oczka siatki
	 * 
	 * @param size	Rozmiar w jednostkach na mapie
	 */
	public void setGridSize(float size)
	{
		gridSize = size;
		repaint();
	}
	
	/**
	 * Zwraca rozmiar oczka siatki
	 * 
	 * @return	Rozmiar w jednostkach na mapie
	 */
	public float getGridSize()
	{
		return gridSize;
	}
	
	/**
	 * Ustawia etykietę statusu
	 * 
	 * @param l	Etykieta
	 */
	public void setStatusLabel(JLabel l)
	{
		statusLabel = l;
	}
	
	/**
	 * Zwraca etykietę statusu
	 * 
	 * @return	Etykieta
	 */
	public JLabel getStatusLabel()
	{
		return statusLabel;
	}
	
	/**
	 * Przełącza blokowanie odrysowywania
	 * 
	 * @param b	Flaga
	 */
	public void setRepaintLocked(boolean b)
	{
		repaintLocked = b;
	}
	
	/**
	 * Zwraca, czy odrysowywanie jest zablokowane
	 * 
	 * @return	Flaga
	 */
	public boolean getRepaintLocked()
	{
		return repaintLocked;
	}
}
