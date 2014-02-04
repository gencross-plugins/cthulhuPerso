package com.mrprez.gencross.impl.cthulhuPerso;

import com.mrprez.gencross.Personnage;
import com.mrprez.gencross.Property;
import com.mrprez.gencross.value.Value;

public class CthulhuPerso extends Personnage {
	
	
	
	
	@Override
	public void calculate() {
		super.calculate();
		if(phase.equals("Métier") || phase.equals("Compétences")){
			calculateCaracteristiqueExtremums();
		}
	}

	private void calculateCaracteristiqueExtremums(){
		int nb=0;
		for(Property carac : getProperty("Caractéristiques").getSubProperties()){
			if(!carac.getName().equals("SAN")){
				if(carac.getValue().getInt()==carac.getMin().getInt() || carac.getValue().getInt()==carac.getMax().getInt()){
					nb++;
					if(nb>1){
						errors.add("Vous ne pouvez avoir qu'un seul extremum parmis les 8 Caractéristiques.");
						return;
					}
				}
			}
		}
	}
	
	public Boolean changeMetierComp(Property property, Value newValue){
		int nb = 0;
		if(newValue.getInt()>property.getMin().getInt()){
			nb++;
		}
		for(Property competence : getProperty("Compétences").getSubProperties()){
			if(competence.getValue().getInt()>competence.getMin().getInt() && competence!=property){
				nb++;
			}
		}
		if(nb>8){
			actionMessage = "Vous ne pouvez avoir plus de 8 compétences métiers";
			return false;
		}
		return true;
	}
	
	public void passToCompetencePhase(){
		getProperty("Compétences").getHistoryFactory().setPointPool("Compétences");
		for(Property competence : getProperty("Compétences").getSubProperties()){
			competence.setMin();
			competence.getHistoryFactory().setPointPool("Compétences");
		}
		getProperty("Compétences").getSubProperties().getDefaultProperty().getHistoryFactory().setPointPool("Compétences");
		this.pointPools.get("Caractéristiques").setToEmpty(true);
		this.pointPools.get("Compétences").setToEmpty(true);
	}

}
