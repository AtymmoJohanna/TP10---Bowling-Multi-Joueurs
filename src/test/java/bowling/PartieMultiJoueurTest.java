package bowling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PartieMultiJoueurTest {
	private PartieMultiJoueur partie;

	@BeforeEach
	void setUp() {
		partie = new PartieMultiJoueur();
	}

	@Test
	void testDemarrerNouvellePartieAvecDeuxJoueurs() {
		String message = partie.demarreNouvellePartie(new String[]{"Pierre", "Paul"});
		assertEquals("Prochain tir : Pierre , tour n° 1, boule 1° ", message);
	}

	@Test
	void testDemarrerNouvellePartieSansJoueur() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			partie.demarreNouvellePartie(new String[]{});
		});
		assertEquals("Il doit y avoir au moins un joueur.", exception.getMessage());
	}

	@Test
	void testDemarrerNouvellePartieAvecNull() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			partie.demarreNouvellePartie(null);
		});
		assertEquals("Il doit y avoir au moins un joueur.", exception.getMessage());
	}

	@Test
	void testEnregistrerLancerSimple() {
		partie.demarreNouvellePartie(new String[]{"Pierre", "Paul"});

		String message1 = partie.enregistreLancer(5);
		assertEquals("Prochain tir : joueur Pierre, tour n° 1, boule n° 2", message1);

		String message2 = partie.enregistreLancer(4);
		assertEquals("Prochain tir : joueur Paul, tour n° 1, boule n° 1", message2);

		String message3 = partie.enregistreLancer(10);
		assertEquals("Prochain tir : joueur Pierre, tour n° 2, boule n° 1", message3);
	}

	@Test
	void testEnregistrerLancerPartieTerminee() {
		partie.demarreNouvellePartie(new String[]{"Pierre"});
		for (int i = 0; i < 20; i++) { // Maximum de lancers pour un joueur sans strike
			partie.enregistreLancer(0);
		}
		String result = partie.enregistreLancer(0);
		assertEquals("Partie terminée", result);
	}

	@Test
	void testScorePourJoueurExistant() {
		partie.demarreNouvellePartie(new String[]{"Pierre", "Paul"});

		partie.enregistreLancer(5);
		partie.enregistreLancer(4); // Pierre : score 9
		partie.enregistreLancer(10); // Paul : strike

		assertEquals(9, partie.scorePour("Pierre"));
		assertEquals(10, partie.scorePour("Paul"));
	}
	

	@Test
	void testEnregistrerLancerExceptionSansDemarrage() {
		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
			partie.enregistreLancer(5);
		});
		assertEquals("La partie n'a pas été démarrée.", exception.getMessage());
	}

	@Test
	void testPartieCompleteAvecDeuxJoueurs() {
		partie.demarreNouvellePartie(new String[]{"Alice", "Bob"});

		// Tour 1 : Alice joue
		assertEquals("Prochain tir : joueur Alice, tour n° 1, boule n° 1", partie.enregistreLancer(5));
		assertEquals("Prochain tir : joueur Alice, tour n° 1, boule n° 2", partie.enregistreLancer(3));

		// Tour 1 : Bob joue
		assertEquals("Prochain tir : joueur Bob, tour n° 1, boule n° 1", partie.enregistreLancer(7));
		assertEquals("Prochain tir : joueur Bob, tour n° 1, boule n° 2", partie.enregistreLancer(2));

		// Tour 2 : Alice joue
		assertEquals("Prochain tir : joueur Alice, tour n° 2, boule n° 1", partie.enregistreLancer(10)); // Strike

		// Tour 2 : Bob joue
		assertEquals("Prochain tir : joueur Bob, tour n° 2, boule n° 1", partie.enregistreLancer(4));
		assertEquals("Prochain tir : joueur Bob, tour n° 2, boule n° 2", partie.enregistreLancer(5));

		// Vérifier les scores intermédiaires
		assertEquals(18, partie.scorePour("Alice")); // 5+3+(10) -> Strike dans le tour 2
		assertEquals(18, partie.scorePour("Bob"));   // 7+2+4+5 -> Bob n'a pas de strike ou spare
	}
}
