package optimisation.combinatoire;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Optimisation {

	public static String LISTE_BASE_PATH = "Scenarii/Liste Bases/Liste Bases";
	public static String LISTE_ENTREPRISE_PATH = "Scenarii/Liste Entreprises/Liste Ent";
	public static String EXTENSION_TXT = ".txt";

	private static Map<Integer, Base> mapBases = new HashMap<Integer, Base>();

	private List<String> entreprises = null;

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
		int sum = 0;
		for (final Integer idBase : retour) {
			final Base base = mapBases.get(idBase);
			retourBase.add(base);
			sum += base.getCoutBase();
		}
		System.out.println(sum);
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

				final Problem p = new Problem(entrepriseATrouver, baseTestee);
				final Problem retour = solveBruteForce(p, baseRestantes);
				if (retour != null && retour.cout() < coutMax) {
					best = retour;
				}
			}
			return best;
		}
	}

	public List<Base> solveBAndB() {
		// TODO
		return null;
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
		if (args.length != 2) {
			System.out
					.println("Argument 1 : numéro de la liste des bases utilisée (compris entre 1 et 3)");
			System.out
					.println("Argument 2 : numéro de la liste des entreprises utilisée (compris entre 1 et 3)");
			return;
		}
		final Optimisation optimisation = new Optimisation(Integer.parseInt(args[0]),
				Integer.parseInt(args[1]));
		System.out.println(optimisation);
		System.out.println("Solution avec l'aglo glouton : "+optimisation.solveGlouton());
		System.out.println("Solution avec l'algo brute force : "+optimisation.solveBruteForce());
	}
}
