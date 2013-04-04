package alg;

import java.util.*;

/**
 * Ta klasa reprezentuje cykl Hamiltona w grafie pełnym, czyli osobnika w naszym
 * algoryrmie ewolucyjnym. Posiada operatory krzyżowania oraz mutacji.
 * 
 * Metoda krzyżująca grafy dąży do zbudowania grafu potomnego o krawędziach, które
 * zawierają w rodzicach, jednak dla grafów o więcej niż kilku punktach nie zawsze
 * jest to możliwe, a dla grafów o >40 punktach praktycznie nigdy. Dlatego w przypadku,
 * w którym nie można wybrać krawędzi z rodziców, wierzchołek docelowy jest losowany ze
 * wszystkich możliwych.
 * 
 * Z testów wynika, że ok. 10% krawędzi dziecka pochodzi spoza rodziców.
 * 
 * Dla bardzo małych grafów (<10 wierzchołków) dzieci bywają czasem klonem jednego rodzica.
 * 
 * @see Chromosome
 */
public class Hamilton
{
	//  ========================= POLA KLASY =========================
	
	/**
	 * Genotyp osobnika, będący tablicą chromosomów
	 * 
	 * Dwa ostatnie chromosomy nie niosą nowej informacji (można je określić na podstawie
	 * poprzednich), nazwijmy je chromosomami dopełniającymi. Ich obecność jest jednak
	 * konieczna podczas krzyżowania, gdyż wszystkie chromosomy są traktowane tak samo.
	 * Niemniej jednak podczas krzyżowania chromosomy dopełniające potomka nie mogą
	 * być wylosowane, a muszą być utworzone niezależnie od rodziców na podstawie chromosomów
	 * utworzonych wcześniej.
	 */
	private Chromosome[] chromosomes;
	
	//  ========================= KONSTRUKTORY =========================
	
	/**
	 * Generuje losowy genotyp cyklu Hamiltona grafu o danej ilości wierzchołków
	 *
	 * @param count	Ilość wierzchołków w grafie
	 */
	public Hamilton(int count) throws TooLowCountException
	{
		if(count < 3)
		{
			throw new TooLowCountException(count);
		}
		
		chromosomes = new Chromosome[count];
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		for(int i = 0; i < count; i++)
		{
			list.add(i);
		}
		
		Collections.shuffle(list);
		
		for(int i = 0; i < count; i++)
		{
			/*
			 * Triczek. Jak przekroczy tablicę, wraca do początku
			 */
			int iplus1 = (i+1) % count;
			int iplus2 = (i+2) % count;
			
			Chromosome chromos = new Chromosome(list.get(i), list.get(iplus1), list.get(iplus2));
			chromosomes[i] = chromos;
		}
	}
	
	/**
	 * Tworzy genotyp z podaną tablicą chromosomów.
	 * 
	 * @param chromosomes	Tablica chromosomów
	 */
	private Hamilton(Chromosome[] chromosomes)
	{
		this.chromosomes = chromosomes;
	}
	
	/**
	 * Konstruntkor kopiujący
	 * 
	 * @param ham	Cykl do skopiowania
	 */
	protected Hamilton(Hamilton ham)
	{
		chromosomes = new Chromosome[ham.chromosomes.length];
		
		for(int i = 0; i < chromosomes.length; i++)
		{
			chromosomes[i] = new Chromosome(ham.chromosomes[i]);
		}
	}
	
	//  ========================= METODY KLASY =========================
	
	/**
	 * Metoda mutująca cykl w miejscu. Zamienia dwa losowe wierzchołki ze sobą.
	 */
	public void mutate()
	{
		Random random = new Random();
		int a = random.nextInt(chromosomes.length);
		int b = random.nextInt(chromosomes.length);
		boolean aDone = false;
		boolean bDone = false;
		
		if(a == b)  // "Naciągnę" losowanie, żeby się zmutował.
		{
			b = (b+1) % chromosomes.length;
		}
				
		// Przestawienie
		for(int i = 0; i < chromosomes.length; i++)
		{
			if(chromosomes[i].getMidGene() == a && !aDone)
			{
				aDone = true;
				
				chromosomes[i].setMidGene(b);
				chromosomes[(chromosomes.length + i-1) % chromosomes.length].setEndGene(b);
				chromosomes[(chromosomes.length + i+1) % chromosomes.length].setStartGene(b);
				
				/*
				 * Mały trik - (chromosomes.length + i-1) chromosomes.length - gwarantuje, że
				 * dostaniemy wartość nieujemną i mieszczącą się w przedziale.
				 */
			}
						
			if(chromosomes[i].getMidGene() == b && !bDone)
			{
				bDone = true;
				
				chromosomes[i].setMidGene(a);
				chromosomes[(chromosomes.length + i-1) % chromosomes.length].setEndGene(a);
				chromosomes[(chromosomes.length + i+1) % chromosomes.length].setStartGene(a);
			}
		}
	}
	
	/**
	 * Metoda krzyżująca dwa cykle ze sobą.
	 * 
	 * @param h1	Rodzic 1
	 * @param h2	Rodzic 2
	 * @return	Potomek
	 */
	public static Hamilton crossing(Hamilton h1, Hamilton h2) throws EvolutionException
	{
		if(h1.getCount() != h2.getCount())
		{
			throw new EvolutionException("Nie można krzyżować osobników o różnej liczbie chromosomów.");
		}
		
		/*
		 * Tablica indeksów. Na miejsce wykorzystanych będą kopiowane indeksy z końca,
		 * a losowanie będzie się odbywało z określonej ilości początkowych elementów.
		 */
		int[] points = new int[h1.getCount()];
		Chromosome[] childGenotype = new Chromosome[h1.getCount()];  // Genotyp potomka
		ArrayList<Integer> usedGenes = new ArrayList<Integer>();  // Wrzucamy tu wykorzystane indeksy
		Random random = new Random();
		
		for(int i = 0; i < points.length; i++)
		{
			points[i] = i;
		}
		
		// Tworzenie chromosomów potomnych z wyjątkiem dopełniających
		
		for(int i = 0; i < h1.getCount()-2; i++)
		{
			int startGene = -1, midGene = -1, endGene = -1;
			
			if(i == 0)  // W pierwszym chromosomie dwa geny są przepisywane od jednego rodzica, trzeci od drugiego
			{
				int index = random.nextInt(h1.getCount());  // Który rodzic da dwa geny?
				int parent = random.nextInt(2);
				
				// Przepisanie dwóch genów od jednego z rodziców
				startGene = (parent == 1 ? h2 : h1).chromosomes[index].getStartGene();
				midGene = (parent == 1 ? h2 : h1).chromosomes[index].getMidGene();
				
				// Dodanie genów do listy wykorzystanych
				usedGenes.add(startGene);
				usedGenes.add(midGene);
				
				// Przepisanie ostatniego genu od drugiego rodzica
				for(int j = 0; j < h1.chromosomes.length; j++)
				{
					if((parent == 0 ? h2 : h1).chromosomes[j].getMidGene() == midGene)
					{
						Chromosome c = (parent == 0 ? h2 : h1).chromosomes[j];
						
						/*
						 * Dopisanie genu końcowego lub początkowego, jeżeli początkowy się powtórzył.
						 * Niemożliwe jest, żeby powtóryły się obydwa - jak na razie są tylko dwa
						 * zużyte geny, w tym środkowy gen chromosomu.
						 */
						if(!usedGenes.contains(c.getEndGene()))
						{
							endGene = c.getEndGene();
						}
						else
						{
							endGene = c.getStartGene();
						}
					}
				}
				
				// Dodanie genów do listy wykorzystanych
				usedGenes.add(endGene);
				
				// Zastąpienie wykorzystanych już indeksów indeksami z końca
				for(int j = 0; j < 3; j++)
				{
					int toOverride = -1;
					
					for(int k = 0; k < points.length-j-1; k++)
					{
						if(points[k] == startGene || points[k] == midGene || points[k] == endGene)
						{
							toOverride = k;
						}
					}
					
					if(toOverride >= 0)
					{
						points[toOverride] = points[points.length-j-1];
						points[points.length-j-1] = -1;
					}
				}
				
				// Utworzenie chromosomu
				childGenotype[0] = new Chromosome(startGene, midGene, endGene);
			}
			else
			{
				startGene = childGenotype[i-1].getMidGene();
				midGene = childGenotype[i-1].getEndGene();
				
				/*
				 * Do poniższej listy wrzucane są geny, spośród których losowany będzie końcowy
				 * gen chromosomu potomka. Geny w tej tablicy spełniają dwa warunki:
				 * 1. Są genami początkowymi lub końcowymi chromosomu tego samego typu u jednego
				 *    z rodziców (przykład: nowy gen środkowy: 2, chromosomy rodziców:
				 *    [7, 2, 1] oraz [8, 2, 1], w liście będą: 7, 1, 8, 1 - o ile żaden nie został
				 *    już użyty. Geny mogą się powtarzać - wtedy będą losowane z większym
				 *    prawdopodobieństwem.
				 * 2. Jeszcze nie zostały wykorzystane.
				 */
				ArrayList<Integer> possibleEndGenes = new ArrayList<Integer>();
				
				// Wrzucanie genów od rodziców - obu w jednej pętli
				for(int j = 0; j < h1.chromosomes.length; j++)
				{
					if(h1.chromosomes[j].getMidGene() == midGene)
					{
						possibleEndGenes.add(h1.chromosomes[j].getStartGene());
						possibleEndGenes.add(h1.chromosomes[j].getEndGene());
					}
					
					if(h2.chromosomes[j].getMidGene() == midGene)
					{
						possibleEndGenes.add(h2.chromosomes[j].getStartGene());
						possibleEndGenes.add(h2.chromosomes[j].getEndGene());
					}
				}
				
				// Usunięcie wykorzystanych genów, wykorzystując kolekcję usedGenes
				possibleEndGenes.removeAll(usedGenes);
				
				// Wylosowanie genu od rodziców lub innego, jeżeli nie ma
				if(possibleEndGenes.size() > 0)
				{
					endGene = possibleEndGenes.get(random.nextInt(possibleEndGenes.size()));
					
					usedGenes.add(endGene);
					// Zastąpienie użytego genu w tablicy indeksów
					for(int j = 0; j < points.length-i; j++)
					{
						if(points[j] == endGene)
						{
							points[j] = points[points.length-i-3];
							points[points.length-i-3] = -i;
						}
					}
				}
				else
				{
					int randomIndex = random.nextInt(points.length-i-2);
					endGene = points[randomIndex];
					
					usedGenes.add(endGene);
					// Zastąpienie indeksu
					points[randomIndex] = points[points.length-i-3];
				}
			
				childGenotype[i] = new Chromosome(startGene, midGene, endGene);
			}
		}
		
		// Dopisywanie chromosomów dopełniających
		
		childGenotype[h1.getCount()-2] = new Chromosome(childGenotype[h1.getCount()-3].getMidGene(), 
														childGenotype[h1.getCount()-3].getEndGene(),
														childGenotype[0].getStartGene());
		
		childGenotype[h1.getCount()-1] = new Chromosome(childGenotype[h1.getCount()-2].getMidGene(), 
														childGenotype[h1.getCount()-2].getEndGene(),
														childGenotype[1].getStartGene());
		return new Hamilton(childGenotype);
	}
	
	/**
	 * Zwraca reprezentację tekstową osobnika, wypisując jego genotyp.
	 * 
	 * @return	Tekstowa reprezentacja osobnika
	 */
	public String toString()
	{
		String res = String.format("Cykl Hamiltona o długości %d:\n", chromosomes.length);
		
		for(int i = 0; i < chromosomes.length; i++)
		{
			/*
			 * To dziwne po ostanim plusie dodaje łamanie wiersza po co piątym chromosomie.
			 */
			res += chromosomes[i] + "\t" + ((i+1) % 10 == 0 ? "\n" : "");
		}
		
		return res;
	}
	
	/**
	 * Zwraca fenotyp osobnika, czyli tablicę zawierającą kolejne wierzchołki cyklu
	 * 
	 * @return	Fenotyp osobnika
	 */
	public int[] getFenotype()
	{
		int[] res = new int[chromosomes.length];
		
		for(int i = 0; i < chromosomes.length; i++)
		{
			res[i] = chromosomes[i].getMidGene();
		}
		
		return res;
	}
	
	/**
	 * Zwraca ilość wierzchołków w cyklu
	 * 
	 * @return	Liczba wierzchołków
	 */
	public int getCount()
	{
		return chromosomes.length;
	}
}
