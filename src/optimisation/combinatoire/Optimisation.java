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
//Solution Glouton methode 1 : [16, 1] pour un cout de 140 trouvé en 2ms
//Solution Glouton méthode 2 : [7, 13, 23] pour un cout de 99 trouvé en 10ms
//Solution Brute Force : Trop long pour arriver à terme
//Solution Branch and Bound : [10, 23] pour un cout de 74 trouvé en 90ms
//
//Liste Bases 1 et Liste Ent 2
//Solution Glouton methode 1 : [2, 1] pour un cout de 113 trouvé en 1ms
//Solution Glouton méthode 2 : [2, 10] pour un cout de 57 trouvé en 1ms
//Solution Brute Force : Trop long pour arriver à terme
//Solution Branch and Bound : [2, 10] pour un cout de 57 trouvé en 27ms
//
//Liste Bases 1 et Liste Ent 3
//Solution Glouton methode 1 : [1, 2, 10] pour un cout de 144 trouvé en 1ms
//Solution Glouton méthode 2 : [14, 7, 13, 19] pour un cout de 107 trouvé en 1ms
//Solution Brute Force : Trop long pour arriver à terme
//Solution Branch and Bound : [7, 13, 14, 19] pour un cout de 107 trouvé en 121ms
//
//Liste Bases 2 et Liste Ent 1
//Solution Glouton methode 1 : [16, 1] pour un cout de 140 trouvé en 1ms
//Solution Glouton méthode 2 : [7, 13, 16] pour un cout de 109 trouvé en 12ms
//Solution Brute Force : Trop long pour arriver à terme
//Solution Branch and Bound : [10, 16] pour un cout de 84 trouvé en 78ms
//
//Liste Bases 2 et Liste Ent 2
//Solution Glouton methode 1 : [2, 1] pour un cout de 113 trouvé en 1ms
//Solution Glouton méthode 2 : [2, 10] pour un cout de 57 trouvé en 1ms
//Solution Brute Force : Trop long pour arriver à terme
//Solution Branch and Bound : [2, 10] pour un cout de 57 trouvé en 23ms
//
//Liste Bases 2 et Liste Ent 3
//Solution Glouton methode 1 : [1, 2, 10] pour un cout de 144 trouvé en 1ms
//Solution Glouton méthode 2 : [14, 7, 13, 19] pour un cout de 107 trouvé en 1ms
//Solution Brute Force : Trop long pour arriver à terme
//Solution Branch and Bound : [7, 13, 14, 19] pour un cout de 107 trouvé en 86ms
//
//Liste Bases 3 et Liste Ent 1
//Solution Glouton methode 1 : [16, 1] pour un cout de 140 trouvé en 1ms
//Solution Glouton méthode 2 : [7, 13, 16] pour un cout de 109 trouvé en 7ms
//Solution Brute Force : [16, 10] pour un cout de 84 trouvé en 10141ms
//Solution Branch and Bound : [16, 10] pour un cout de 84 trouvé en 5ms
//
//Liste Bases 3 et Liste Ent 2
//Solution Glouton methode 1 : [2, 1] pour un cout de 113 trouvé en 0ms
//Solution Glouton méthode 2 : [2, 10] pour un cout de 57 trouvé en 1ms
//Solution Brute Force : [2, 10] pour un cout de 57 trouvé en 63896ms
//Solution Branch and Bound : [2, 10] pour un cout de 57 trouvé en 5ms
//
//Liste Bases 3 et Liste Ent 3
//Solution Glouton methode 1 : [1, 2, 22] pour un cout de 201 trouvé en 1ms
//Solution Glouton méthode 2 : [14, 7, 13, 19] pour un cout de 107 trouvé en 1ms
//Solution Brute Force : [19, 7, 13, 14] pour un cout de 107 trouvé en 4376ms
//Solution Branch and Bound : [19, 7, 13, 14] pour un cout de 107 trouvé en 21ms
public class Optimisation {

	public static String LISTE_BASE_PATH = "Scenarii/Liste Bases/Liste Bases";
	public static String LISTE_ENTREPRISE_PATH = "Scenarii/Liste Entreprises/Liste Ent";
	public static String EXTENSION_TXT = ".txt";

	private static Map<Integer, Base> mapBases = new HashMap<Integer, Base>();

	private List<String> entreprises = null;
	private Integer bestCoutMin;

	public Optimisation(final Integer iBase, final Integer iEntreprise) {

		try {
			final File listeBases = new File(LISTE_BASE_PATH + iBase
					+ EXTENSION_TXT);
			final File listeEntreprise = new File(LISTE_ENTREPRISE_PATH
					+ iEntreprise + EXTENSION_TXT);
			BufferedReader inputListeBases = null;
			BufferedReader inputEntreprise = null;
			entreprises = new ArrayList<String>();

			inputListeBases = new BufferedReader(new InputStreamReader(
					new FileInputStream(listeBases)));
			inputEntreprise = new BufferedReader(new InputStreamReader(
					new FileInputStream(listeEntreprise)));

			// permet de passer la ligne count
			inputListeBases.readLine();

			String line = inputListeBases.readLine();
			while (line != null && !line.isEmpty()) {
				final File baseFile = new File("Bases/" + line);
				final Base base = new Base();
				final Integer idBase = Integer.parseInt(line.substring(5,
						line.indexOf('.'))
						+ "");
				base.setIdBase(idBase);

				BufferedReader inputBases = new BufferedReader(
						new InputStreamReader(new FileInputStream(baseFile)));
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

	/**
	 * L'algorithme glouton a deux conditions possible : on choisit la base avec
	 * le plus d'entreprise à trouver (méthode 1) ou on choisit la base avec le
	 * meilleur rapport qualité/prix (méthode 2)
	 * 
	 * @param method
	 * @return
	 */
	public List<Base> solveGlouton(Integer method) {
		if (method == null || method > 2) {
			method = 1;
		}
		final List<Integer> retour = new ArrayList<Integer>();
		final List<Integer> baseRestantes = new ArrayList<Integer>(
				mapBases.keySet());
		final List<String> entrepriseAtrouver = new ArrayList<String>(
				entreprises);
		while (!entrepriseAtrouver.isEmpty()) {
			float bestCount = 0;
			Base bestBase = null;
			for (final Integer idBase : baseRestantes) {
				final Base base = mapBases.get(idBase);
				float count;
				if (method == 1) {
					count = base.countContains(entrepriseAtrouver);
				} else {
					count = (float) base.countContains(entrepriseAtrouver)
							/ (float) base.getCoutBase();
				}
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
		final List<String> entrepriseAtrouver = new ArrayList<String>(
				entreprises);
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

	public Problem solveBruteForce(final Problem actual,
			final List<Integer> bases) {
		if (actual.entrepriseTrouvees()) {
			return actual;
		} else {
			int coutMax = Integer.MAX_VALUE;
			Problem best = null;
			for (final Integer base : bases) {
				final List<Integer> baseTestee = new ArrayList<Integer>(
						actual.getBaseChoisies());
				baseTestee.add(base);
				final List<Integer> baseRestantes = new ArrayList<Integer>(
						bases);
				baseRestantes.remove(base);
				final List<String> entrepriseATrouver = new ArrayList<String>(
						actual.getEntrepriseATrouver());
				entrepriseATrouver.removeAll(mapBases.get(base)
						.getListeEntreprise());

				final Problem next = new Problem(entrepriseATrouver, baseTestee);
				final Problem retour = solveBruteForce(next, baseRestantes);
				if (retour != null && retour.cout() < coutMax) {
					best = retour;
				}
			}
			return best;
		}
	}

	public List<Base> solveBAndB() {
		bestCoutMin = Integer.MAX_VALUE;
		final List<Integer> bases = new ArrayList<Integer>(mapBases.keySet());
		final List<String> entrepriseAtrouver = new ArrayList<String>(
				entreprises);
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

	public Problem solveBAndB(final Problem actual, final List<Integer> bases,
			final boolean first) {
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
					final List<Integer> baseRestantes = new ArrayList<Integer>(
							bases);
					baseRestantes.remove(base);
					final List<String> entrepriseATrouver = new ArrayList<String>(
							actual.getEntrepriseATrouver());
					entrepriseATrouver.removeAll(mapBases.get(base)
							.getListeEntreprise());

					final Problem next = new Problem(entrepriseATrouver,
							baseTestee);
					final Problem retour = solveBAndB(next, baseRestantes,
							false);
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

	public List<Base> solveBF() {
		final List<Integer> idBases = new ArrayList<Integer>(mapBases.keySet());
		final List<String> entrepriseAtrouver = new ArrayList<String>(
				entreprises);
		final Problem solution = recurBF(entrepriseAtrouver,
				new ArrayList<Integer>(), idBases);
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

	public Problem recurBF(final List<String> paramEntreprises,
			final List<Integer> paramIdBaseTeste,
			final List<Integer> paramIdBases) {
		if (paramEntreprises.size() == 0) {
			return new Problem(paramEntreprises, paramIdBaseTeste);
		} else {
			int coutMax = Integer.MAX_VALUE;
			Problem best = null;
			List<Integer> idBaseTeste;
			List<String> entrepriseATrouver;
			List<Integer> idBaseRestante;
			for (final Integer idBase : paramIdBases) {

				idBaseTeste = new ArrayList<Integer>(paramIdBaseTeste);
				entrepriseATrouver = new ArrayList<String>(paramEntreprises);
				idBaseRestante = new ArrayList<Integer>(paramIdBases);
				idBaseTeste.add(idBase);
				idBaseRestante.remove(idBase);

				entrepriseATrouver.removeAll(mapBases.get(idBase)
						.getListeEntreprise());

				final Problem retour = recurBF(entrepriseATrouver, idBaseTeste,
						idBaseRestante);
				int coutRetour = retour.coutOpt();
				if (retour != null && coutRetour < coutMax) {
					if (best == null || best.coutOpt() > coutRetour) {
						best = retour;
					}
				}
			}
			return best;
		}
	}

	public static int getCoutOptById(final List<Integer> listIdBase) {
		if (listIdBase != null && listIdBase.size() > 0) {
			int coutTotal = 0;
			for (final Integer idBase : listIdBase) {
				coutTotal += Optimisation.getMapBases().get(idBase)
						.getCoutBase();
			}
			return coutTotal;
		} else {
			return Integer.MAX_VALUE;
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

	public static void main(String[] args) {
		if (args.length != 2 && args.length != 3) {
			System.out
					.println("Argument 1 : numéro de la liste des bases utilisée (compris entre 1 et 3)");
			System.out
					.println("Argument 2 : numéro de la liste des entreprises utilisée (compris entre 1 et 3)");
			System.out
					.println("Arguement 3 : méthode à lancer : g1 > gready méthode 1, g2 > gready méthode 2, bf > brute force, bb > branch and bound, a > all (par défaut), m > tous sauf brute force");
			return;
		}
		int base;
		int entreprise;
		String algo;
		try {
			base = Integer.parseInt(args[0]);
			entreprise = Integer.parseInt(args[1]);
			if (args.length == 3) {
				algo = args[2];
			} else {
				algo = "a";
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		final Optimisation optimisation = new Optimisation(base, entreprise);
		System.out.println(optimisation);
		System.out.println("Liste Bases " + base + " et Liste Ent "
				+ entreprise);
		if (algo.equals("m") || algo.equals("a") || algo.equals("g1")) {
			long startTimeGlout = System.currentTimeMillis();
			final List<Base> solutionGlouton = optimisation.solveGlouton(1);
			long stopTimeGlouton = System.currentTimeMillis();
			long elapsedTimeGlouton = stopTimeGlouton - startTimeGlout;
			if (solutionGlouton != null && solutionGlouton.size() > 0) {
				System.out.println("Solution Glouton methode 1 : "
						+ solutionGlouton + " pour un cout de "
						+ Optimisation.getCout(solutionGlouton) + " trouvé en "
						+ elapsedTimeGlouton + "ms");
			} else {
				System.out
						.println("Solution Glouton méthode 1 : aucune solution");
			}
		}

		if (algo.equals("m") || algo.equals("a") || algo.equals("g2")) {
			long startTimeGlout = System.currentTimeMillis();
			final List<Base> solutionGlouton = optimisation.solveGlouton(2);
			long stopTimeGlouton = System.currentTimeMillis();
			long elapsedTimeGlouton = stopTimeGlouton - startTimeGlout;
			if (solutionGlouton != null && solutionGlouton.size() > 0) {
				System.out.println("Solution Glouton méthode 2 : "
						+ solutionGlouton + " pour un cout de "
						+ Optimisation.getCout(solutionGlouton) + " trouvé en "
						+ elapsedTimeGlouton + "ms");
			} else {
				System.out
						.println("Solution Glouton méthode 2: aucune solution");
			}
		}
		if (algo.equals("a") || algo.equals("bf")) {
			long startTimeBF = System.currentTimeMillis();
			final List<Base> solutionBF = optimisation.solveBF();
			long stopTimeBF = System.currentTimeMillis();
			long elapsedTimeBF = stopTimeBF - startTimeBF;
			if (solutionBF != null && solutionBF.size() > 0) {
				System.out.println("Solution Brute Force : " + solutionBF
						+ " pour un cout de "
						+ Optimisation.getCout(solutionBF) + " trouvé en "
						+ elapsedTimeBF + "ms");
			} else {
				System.out.println("Solution Brute Force : aucune solution");
			}
		}
		if (algo.equals("m") || algo.equals("a") || algo.equals("bb")) {
			long startTimeBAndB = System.currentTimeMillis();
			final List<Base> solutionBAndB = optimisation.solveBAndB();
			long stopTimeBAndB = System.currentTimeMillis();
			long elapsedTimeBAndB = stopTimeBAndB - startTimeBAndB;
			if (solutionBAndB != null && solutionBAndB.size() > 0) {
				System.out.println("Solution Branch and Bound : "
						+ solutionBAndB + " pour un cout de "
						+ Optimisation.getCout(solutionBAndB) + " trouvé en "
						+ elapsedTimeBAndB + "ms");
			} else {
				System.out
						.println("Solution Branch and Bound : aucune solution");
			}
		}
	}
}
