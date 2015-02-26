package optimisation.combinatoire;

import java.util.ArrayList;
import java.util.List;

public class Problem {

	private List<String> entrepriseATrouver;

	private List<Integer> baseChoisies;

	public Problem(final List<String> entrepriseATrouver) {
		this.entrepriseATrouver = new ArrayList<String>(entrepriseATrouver);
		baseChoisies = new ArrayList<Integer>();
	}

	public Problem(final List<String> entrepriseATrouver, final List<Integer> baseChoisies) {
		this.entrepriseATrouver = entrepriseATrouver;
		this.baseChoisies = baseChoisies;
	}

	public Integer cout() {
		Integer coutTotal = 0;
		for (final Integer idBase : baseChoisies) {
			coutTotal += Optimisation.getMapBases().get(idBase).getCoutBase();
		}
		return coutTotal;
	}

	public boolean entrepriseTrouvees() {
		return entrepriseATrouver.isEmpty();
	}

	public List<String> getEntrepriseATrouver() {
		return entrepriseATrouver;
	}

	public void setEntrepriseATrouver(List<String> entrepriseATrouver) {
		this.entrepriseATrouver = entrepriseATrouver;
	}

	public List<Integer> getBaseChoisies() {
		return baseChoisies;
	}

	public void setBaseChoisies(List<Integer> baseChoisies) {
		this.baseChoisies = baseChoisies;
	}
	
	public String toString(){
		return entrepriseATrouver+"\n"+baseChoisies;
	}
}
