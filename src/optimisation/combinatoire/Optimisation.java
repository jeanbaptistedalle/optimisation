package optimisation.combinatoire;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import optimisation.combinatoire.Base;


public class Optimisation {

	public static String LISTE_BASE_PATH = "Scenarii/Liste Bases/Liste Bases";
	public static String LISTE_ENTREPRISE_PATH = "Scenarii/Liste Entreprises/Liste Ent";
	public static String EXTENSION_TXT = ".txt";
	
	private List<Base> bases = null;
	private List<String> entreprises = null;

	public Optimisation(final Integer iBase, final Integer iEntreprise) {

		try {
			final File listeBases = new File(LISTE_BASE_PATH + iBase + EXTENSION_TXT);
			final File listeEntreprise = new File(LISTE_ENTREPRISE_PATH + iEntreprise + EXTENSION_TXT);
			BufferedReader inputListeBases = null;
			BufferedReader inputEntreprise = null;
			bases = new ArrayList<Base>();
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
				final Integer idBase = Integer.parseInt(line.charAt(5)+"");
				base.setIdBase(idBase);
				
				BufferedReader inputBases = new BufferedReader(new InputStreamReader(
						new FileInputStream(baseFile)));
				String lineBase = inputBases.readLine();
				final Integer coutBase = Integer.parseInt(lineBase);
				base.setCoutBase(coutBase);
				
				
				while(lineBase != null){
					base.addEntreprise(lineBase);
					lineBase = inputBases.readLine();
				}
				bases.add(base);
				inputBases.close();
				line = inputListeBases.readLine();
			}
			inputListeBases.close();

			//On passe la premiere ligne de count
			inputEntreprise.readLine();
			
			line = inputEntreprise.readLine();
			while(line != null && !line.isEmpty()){
				entreprises.add(line);
				line = inputEntreprise.readLine();
			}
			inputEntreprise.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Base> solve(){
		final List<Base> retour = new ArrayList<Base>();
		
		return retour;
	}

	public String toString(){
		return bases+"\n"+entreprises;
	}
	
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out
					.println("Argument 1 : numéro de la liste des bases utilisée (compris entre 1 et 3)");
			System.out
					.println("Argument 2 : numéro de la liste des entreprises utilisée (compris entre 1 et 3)");
			return;
		}
		final Optimisation optimisation = new Optimisation(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		System.out.println(optimisation);
	}
}
