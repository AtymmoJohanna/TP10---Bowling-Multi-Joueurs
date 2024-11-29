package bowling;

import java.util.ArrayList;
import java.util.HashMap;

public class PartieMultiJoueur implements IPartieMultiJoueurs{
	private HashMap<String, PartieMonoJoueur> joueurs;
	private String[] nomsDesJoueurs;
	private String joueurActuel;
	private int indexActuel = 0;

	/**
	 * Démarre une nouvelle partie pour un groupe de joueurs
	 * @param nomsDesJoueurs un tableau des noms de joueurs (il faut au moins un joueur)
	 * @return une chaîne de caractères indiquant le prochain joueur,
	 * de la forme "Prochain tir : joueur Bastide, tour n° 1, boule n° 1"
	 * @throws java.lang.IllegalArgumentException si le tableau est vide ou null
	 */
	public String demarreNouvellePartie(String[] nomsDesJoueurs){
		if (nomsDesJoueurs == null || nomsDesJoueurs.length == 0) {
			throw new IllegalArgumentException("il doit avoir des joueurs");
		}
		joueurs = new HashMap<>();
		this.nomsDesJoueurs = nomsDesJoueurs;
		for(String j: nomsDesJoueurs){
			joueurs.put(j, new PartieMonoJoueur());
		}
		joueurActuel= nomsDesJoueurs[0];
		return "Prochain tir : joueur " + joueurActuel + ", tour n° 1, boule 1° ";
	}

	/**
	 * Enregistre le nombre de quilles abattues pour le joueur courant, dans le tour courant, pour la boule courante
	 * @param nombreDeQuillesAbattues : nombre de quilles abattue à ce lancer
	 * @return une chaîne de caractères indiquant le prochain joueur,
	 * de la forme "Prochain tir : joueur Bastide, tour n° 5, boule n° 2",
	 * ou bien "Partie terminée" si la partie est terminée.
	 * @throws java.lang.IllegalStateException si la partie n'est pas démarrée.
	 */
	public String enregistreLancer(int nombreDeQuillesAbattues) {
		// Récupérer la partie du joueur actuel
		PartieMonoJoueur partieActuelle = joueurs.get(joueurActuel);

		// Enregistrer le lancer pour le joueur actuel
		boolean doitRelancer = partieActuelle.enregistreLancer(nombreDeQuillesAbattues);

		// Si le joueur doit rejouer, retourner directement son prochain tir
		if (doitRelancer) {
			int tour = partieActuelle.numeroTourCourant();
			int boule = partieActuelle.numeroProchainLancer();
			return "Prochain tir : joueur " + joueurActuel + ", tour n° " + tour + ", boule n° " + boule;
		}

		// Si le joueur a terminé son tour, passer au joueur suivant
		if (partieActuelle.estTerminee() || !doitRelancer) {
			// Incrémenter l'indice et passer au joueur suivant
			indexActuel = (indexActuel + 1) % nomsDesJoueurs.length;
			joueurActuel = nomsDesJoueurs[indexActuel];
		}

		// Vérifier si tous les joueurs ont terminé leur partie
		boolean partieTerminee = true;
		for (PartieMonoJoueur p : joueurs.values()) {
			if (!p.estTerminee()) {
				partieTerminee = false;
				break;
			}
		}

		if (partieTerminee) {
			return "Partie terminée";
		}

		// Retourner le prochain joueur
		int tour = joueurs.get(joueurActuel).numeroTourCourant();
		int boule = joueurs.get(joueurActuel).numeroProchainLancer();
		return "Prochain tir : joueur " + joueurActuel + ", tour n° " + tour + ", boule n° " + boule;
	}

	/**
	 * Donne le score pour le joueur playerName
	 * @param nomDuJoueur le nom du joueur recherché
	 * @return le score pour ce joueur
	 * @throws IllegalArgumentException si nomDuJoueur ne joue pas dans cette partie
	 */
	public int scorePour(String nomDuJoueur){
		int i=0;
		for (String s: nomsDesJoueurs){
			if(  s.equals(nomDuJoueur)){
				i=1;
				break;
			}
		}
		if (i == 0) {
			return 0;
		}else{
			return joueurs.get(nomDuJoueur).score();
		}
		
	}



}
