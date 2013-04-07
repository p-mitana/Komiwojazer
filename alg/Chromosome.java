package alg;

/**
 * Ta klasa reprezentuje gen. W myśl danego algorytmu gen opisuje "zagięcie"
 * cyklu Hamiltona. Składa się on z trzech liczb (genów), będących indeksami
 * wierzchołków danego grafu. "Typem" chromosomu jest środkowy gen,
 * oznaczający wierzchołek "zagięcia".
 */
public class Chromosome
{
	//  ========================= POLA KLASY =========================
	
	/** Gen początkowy */
	private int startGene;
	
	/** Gen środkowy - determinuje typ chromosomu */
	private int midGene;
	
	/** Gen końcowy */
	private int endGene;
	
	//  ========================= KONSTRUKTORY =========================
	
	/**
	 * Prosty konstruktor klasy, przypisujący wartości pól
	 *
	 * @param startGene	Gen początkowy
	 * @param midGene	Gen środkowy
	 * @param endGene	Gen końcowy
	 */
	public Chromosome(int startGene, int midGene, int endGene)
	{
		this.startGene = startGene;
		this.midGene = midGene;
		this.endGene = endGene;
	}
	
	/**
	 * Konstruktor kopiujący
	 *
	 * @param otherChromosome	Chromosom źródłowy
	 */
	public Chromosome(Chromosome otherChromosome)
	{
		this.startGene = otherChromosome.startGene;
		this.midGene = otherChromosome.midGene;
		this.endGene = otherChromosome.endGene;
	}
	
	//  ========================= METODY KLASY =========================
	
	/**
	 * Zwraca reprezentację tego chromosomu w postaci: "[a, b, c]", gdzie
	 * a, b i c to kolejne geny
	 * 
	 * @return	Tekstowa reprezentacja chromosomu
	 */
	public String toString()
	{
		return String.format("[%d, %d, %d]", startGene, midGene, endGene);
	}
	
	//  ========================= GETTERY =========================
	
	/**
	 * Zwraca gen początkowy
	 * 
	 * @return	Gen początkowy
	 */
	public int getStartGene()
	{
		return startGene;
	}
	
	/**
	 * Zwraca gen środkowy
	 * 
	 * @return	Gen środkowy
	 */
	public int getMidGene()
	{
		return midGene;
	}
	
	/**
	 * Zwraca gen końcowy
	 * 
	 * @return	Gen końcowy
	 */
	public int getEndGene()
	{
		return endGene;
	}
	
	/**
	 * Ustawia gen początkowy
	 * 
	 * @param gene	Gen początkowy
	 */
	public void setStartGene(int gene)
	{
		startGene = gene;
	}
	
	/**
	 * Ustawia gen środkowy
	 * 
	 * @param gene	Gen środkowy
	 */
	public void setMidGene(int gene)
	{
		midGene = gene;
	}
	
	/**
	 * Ustawia gen końcowy
	 * 
	 * @param gene	Gen końcowy
	 */
	public void setEndGene(int gene)
	{
		endGene = gene;
	}
}
