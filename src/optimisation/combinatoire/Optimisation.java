package optimisation.combinatoire;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Liste Bases 1 et Liste Ent 1
//Solution aglo Glouton : [7, 13, 23] pour un cout de 99
//Solution Branch and Bound : [10, 23] pour un cout de 74
//
//Liste Bases 1 et Liste Ent 2
//Solution aglo Glouton : [2, 10] pour un cout de 57
//Solution Branch and Bound : [2, 10] pour un cout de 57
//
//Liste Bases 1 et Liste Ent 3
//Solution aglo Glouton : [14, 7, 13, 19] pour un cout de 107
//Solution Branch and Bound : [7, 13, 14, 19] pour un cout de 107
//
//Liste Bases 2 et Liste Ent 1
//Solution aglo Glouton : [7, 13, 23] pour un cout de 99
//Solution Branch and Bound : [10, 23] pour un cout de 74
//
//Liste Bases 2 et Liste Ent 2
//Solution aglo Glouton : [2, 10] pour un cout de 57
//Solution Branch and Bound : [2, 10] pour un cout de 57
//
//Liste Bases 2 et Liste Ent 3
//Solution aglo Glouton : [14, 7, 13, 19] pour un cout de 107
//Solution Branch and Bound : [7, 13, 14, 19] pour un cout de 107
//
//Liste Bases 3 et Liste Ent 1
//Solution aglo Glouton : [7, 13, 23] pour un cout de 99
//Solution Branch and Bound : [10, 23] pour un cout de 74
//
//Liste Bases 3 et Liste Ent 2
//Solution aglo Glouton : [2, 10] pour un cout de 57
//Solution Branch and Bound : [2, 10] pour un cout de 57
//
//Liste Bases 3 et Liste Ent 3
//Solution aglo Glouton : [14, 7, 13, 19] pour un cout de 107
//Solution Branch and Bound : [7, 13, 14, 19] pour un cout de 107
public class Optimisation {

	public static String LISTE_BASE_PATH = "Scenarii/Liste Bases/Liste Bases";
	public static String LISTE_ENTREPRISE_PATH = "Scenarii/Liste Entreprises/Liste Ent";
	public static String EXTENSION_TXT = ".txt";

	private static Map<Integer, Base> mapBases = new HashMap<Integer, Base>();

	private List<String> entreprises = null;
	private Integer bestCoutMin;

	public Optimisation(final Integer iBase, final Integer iEntreprise) {

		try {
			final File listeBases = new File(LISTE_BASE_PATH + iBase + EXTENSION_TXT);
			final File listeEntreprise = new File(LISTE_ENTREPRISE_PATH + iEntreprise
					+ EXTENSION_TXT);
			BufferedReader inputListeBases = null;
			BufferedReader inputEntreprise = null;
			entreprises = new ArrayList<String>();

			inputListeBases = new BufferedReader(new InputStreamReader(new FileInputStream(
					listeBases)));
			inputEntreprise = new BufferedReader(new InputStreamReader(new FileInputStream(
					listeEntreprise)));

			// permet de passer la ligne count
			inputListeBases.readLine();

			String line = inputListeBases.readLine();
			while (line != null && !line.isEmpty()) {
				final File baseFile = new File("Bases/" + line);
				final Base base = new Base();
				final Integer idBase = Integer.parseInt(line.substring(5, line.indexOf('.')) + "");
				base.setIdBase(idBase);

				BufferedReader inputBases = new BufferedReader(new InputStreamReader(
						new FileInputStream(baseFile)));
				String lineBase = inputBases.readLine();
				final Integer coutBase = Integer.parseInt(lineBase);
				base.setCoutBase(coutBase);

				// permet de passer la ligne count
				inputBases.readLine();
				lineBase = inputBases.readLine();

				while (lineBase != null) {
					base.addEntreprise(lineBase);
					lineBase = inputBases.readLine();
				}
				mapBases.put(base.getIdBase(), base);
				inputBases.close();
				line = inputListeBases.readLine();
			}
			inputListeBases.close();

			// On passe la premiere ligne de count
			inputEntreprise.readLine();

			line = inputEntreprise.readLine();
			while (line != null && !line.isEmpty()) {
				entreprises.add(line);
				line = inputEntreprise.readLine();
			}
			inputEntreprise.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public List<Base> solveGlouton() {
		final List<Integer> retour = new ArrayList<Integer>();
		final List<Integer> baseRestantes = new ArrayList<Integer>(mapBases.keySet());
		final List<String> entrepriseAtrouver = new ArrayList<String>(entreprises);
		while (!entrepriseAtrouver.isEmpty()) {
			float bestCount = 0;
			Base bestBase = null;
			for (final Integer idBase : baseRestantes) {
				final Base base = mapBases.get(idBase);
				float count = (float) base.countContains(entrepriseAtrouver)
						/ (float) base.getCoutBase();
				if (count > bestCount) {
					bestBase = base;
					bestCount = count;
				}
			}
			if (bestBase != null) {
				retour.add(bestBase.getIdBase());
				baseRestantes.remove(bestBase);
				entrepriseAtrouver.removeAll(bestBase.getListeEntreprise());
			} else {
				/*
				 * Aucune base ne permet d'améliorer la liste, on ne peux donc
				 * pas trouver de solution complète. Ici, on decide de renvoyer
				 * null pour indiquer qu'il n'y a pas de solution complète mais
				 * on aurait pu renvoyer la solution partielle.
				 */
				return null;
			}
		}
		final List<Base> retourBase = new ArrayList<Base>();
		for (final Integer idBase : retour) {
			final Base base = mapBases.get(idBase);
			retourBase.add(base);
		}
		return retourBase;
	}

	public List<Base> solveBruteForce() {
		final List<Integer> bases = new ArrayList<Integer>(mapBases.keySet());
		final List<String> entrepriseAtrouver = new ArrayList<String>(entreprises);
		final Problem root = new Problem(entrepriseAtrouver);
		final Problem solution = solveBruteForce(root, bases);
		final List<Base> baseChoisies = new ArrayList<Base>();
		if (solution != null) {
			for (final Integer base : solution.getBaseChoisies()) {
				baseChoisies.add(mapBases.get(base));
			}
			return baseChoisies;
		} else {
			return null;
		}
	}

	public Problem solveBruteForce(final Problem actual, final List<Integer> bases) {
		if (actual.entrepriseTrouvees()) {
			return actual;
		} else {
			int coutMax = Integer.MAX_VALUE;
			Problem best = null;
			for (final Integer base : bases) {
				final List<Integer> baseTestee = new ArrayList<Integer>(actual.getBaseChoisies());
				baseTestee.add(base);
				final List<Integer> baseRestantes = new ArrayList<Integer>(bases);
				baseRestantes.remove(base);
				final List<String> entrepriseATrouver = new ArrayList<String>(
						actual.getEntrepriseATrouver());
				entrepriseATrouver.removeAll(mapBases.get(base).getListeEntreprise());

				final Problem next = new Problem(entrepriseATrouver, baseTestee);
				final Problem retour = solveBruteForce(next, baseRestantes);
				if (retour != null && retour.cout() < coutMax) {
					best = retour;
				}
			}
			System.out.println(best);
			return best;
		}
	}

	public List<Base> solveBAndB() {
		bestCoutMin = Integer.MAX_VALUE;
		final List<Integer> bases = new ArrayList<Integer>(mapBases.keySet());
		final List<String> entrepriseAtrouver = new ArrayList<String>(entreprises);
		final Problem root = new Problem(entrepriseAtrouver);
		final Problem solution = solveBAndB(root, bases, true);
		final List<Base> baseChoisies = new ArrayList<Base>();
		if (solution != null) {
			for (final Integer base : solution.getBaseChoisies()) {
				baseChoisies.add(mapBases.get(base));
			}
			return baseChoisies;
		} else {
			return null;
		}
	}

	public Problem solveBAndB(final Problem actual, final List<Integer> bases, final boolean first) {
		final Integer cout = actual.cout();
		if (first || cout < bestCoutMin) {
			if (actual.entrepriseTrouvees()) {
				bestCoutMin = cout;
				return actual;
			} else {
				int coutMax = Integer.MAX_VALUE;
				Problem best = null;
				for (final Integer base : bases) {
					final List<Integer> baseTestee = new ArrayList<Integer>(
							actual.getBaseChoisies());
					baseTestee.add(base);
					final List<Integer> baseRestantes = new ArrayList<Integer>(bases);
					baseRestantes.remove(base);
					final List<String> entrepriseATrouver = new ArrayList<String>(
							actual.getEntrepriseATrouver());
					entrepriseATrouver.removeAll(mapBases.get(base).getListeEntreprise());

					final Problem next = new Problem(entrepriseATrouver, baseTestee);
					final Problem retour = solveBAndB(next, baseRestantes, false);
					if (retour != null && retour.cout() < coutMax) {
						best = retour;
					}
				}
				return best;
			}
		} else {
			return null;
		}
	}

	public static Integer getCoutById(final List<Integer> listIdBase) {
		if (listIdBase != null && listIdBase.size() > 0) {
			final List<Base> listeBase = new ArrayList<Base>();
			for (final Integer idBase : listIdBase) {
				listeBase.add(Optimisation.getMapBases().get(idBase));
			}
			return getCout(listeBase);
		} else {
			return Integer.MAX_VALUE;
		}
	}

	public static Integer getCout(final List<Base> listeBase) {
		if (listeBase != null && listeBase.size() > 0) {
			Integer coutTotal = 0;
			for (final Base base : listeBase) {
				coutTotal += base.getCoutBase();
			}
			return coutTotal;
		} else {
			return Integer.MAX_VALUE;
		}
	}

	public List<String> getEntreprises() {
		return entreprises;
	}

	public void setEntreprises(List<String> entreprises) {
		this.entreprises = entreprises;
	}

	public static Map<Integer, Base> getMapBases() {
		return mapBases;
	}

	public static void setMapBases(Map<Integer, Base> mapBases) {
		Optimisation.mapBases = mapBases;
	}

	public String toString() {
		return mapBases.values() + "\n" + entreprises;
	}

	public static void main2(String[] args) {
		for (int i = 1; i < 4; i++) {
			for (int j = 1; j < 4; j++) {
				String[] args2 = { "" + i, "" + j };
				main2(args2);
				System.out.println();
			}
		}
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out
					.println("Argument 1 : numéro de la liste des bases utilisée (compris entre 1 et 3)");
			System.out
					.println("Argument 2 : numéro de la liste des entreprises utilisée (compris entre 1 et 3)");
			return;
		}
		final Optimisation optimisation = new Optimisation(Integer.parseInt(args[0]),
				Integer.parseInt(args[1]));

		System.out.println("Liste Bases " + args[0] + " et Liste Ent " + args[1]);

		final List<Base> solutionGlouton = optimisation.solveGlouton();
		if (solutionGlouton != null && solutionGlouton.size() > 0) {
			System.out.println("Solution Glouton : " + solutionGlouton + " pour un cout de "
					+ Optimisation.getCout(solutionGlouton));
		} else {
			System.out.println("Solution Glouton : aucune solution");
		}

		final List<Base> solutionBruteForce = optimisation.solveBruteForce();
		if (solutionBruteForce != null && solutionBruteForce.size() > 0) {
			System.out.println("Solution Brute Force : " + solutionBruteForce + " pour un cout de "
					+ Optimisation.getCout(solutionBruteForce));
		} else {
			System.out.println("Solution Brute Force : aucune solution");
		}

		final List<Base> solutionBAndB = optimisation.solveBAndB();
		if (solutionBAndB != null && solutionBAndB.size() > 0) {
			System.out.println("Solution Branch and Bound : " + solutionBAndB + " pour un cout de "
					+ Optimisation.getCout(solutionBAndB));
		} else {
			System.out.println("Solution Branch and Bound : aucune solution");
		}
	}
}
