package optimisation.combinatoire;

import java.util.ArrayList;
import java.util.List;

public class Base {

	private Integer idBase;
	private Integer coutBase;
	private List<String> listeEntreprise;
	
	public Base(){
		idBase = null;
		coutBase = null;
		listeEntreprise = new ArrayList<String>();
	}
	
	public Base(final Integer idBase, final Integer coutBase, final List<String> listeEntreprise){
		this.idBase = idBase;
		this.coutBase = coutBase;
		this.listeEntreprise = listeEntreprise;
	}
	
	public boolean contains(final String nomEntreprise){
		return listeEntreprise.contains(nomEntreprise);
	}
	
	public int countContains(final List<String> liste){
		Integer count = 0;
		for(String nom : liste){
			if(contains(nom)){
				count++;
			}
		}
		return count;
	}
	
	public void addEntreprise(final String nomEntreprise){
		listeEntreprise.add(nomEntreprise);
	}

	public Integer getIdBase() {
		return idBase;
	}

	public void setIdBase(Integer idBase) {
		this.idBase = idBase;
	}

	public Integer getCoutBase() {
		return coutBase;
	}

	public void setCoutBase(Integer coutBase) {
		this.coutBase = coutBase;
	}

	public List<String> getListeEntreprise() {
		return listeEntreprise;
	}

	public void setListeEntreprise(List<String> listeEntreprise) {
		this.listeEntreprise = listeEntreprise;
	}
	
	public String toString(){
		return idBase+":"+coutBase+">"+listeEntreprise;
	}
}
