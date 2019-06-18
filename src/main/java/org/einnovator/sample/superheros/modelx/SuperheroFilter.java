package org.einnovator.sample.superheros.modelx;

import org.einnovator.sample.superheros.model.Squad;
import org.einnovator.util.model.ToStringCreator;

public class SuperheroFilter extends SuperheroOptions {
	
	private String q;
	
	private Squad squad;

	private Boolean villain;

	public SuperheroFilter() {
	}
	
	/**
	 * Get the value of property {@code q}.
	 *
	 * @return the q
	 */
	public String getQ() {
		return q;
	}

	/**
	 * Set the value of property {@code q}.
	 *
	 * @param q the q to set
	 */
	public void setQ(String q) {
		this.q = q;
	}


	/**
	 * Get the value of property {@code squad}.
	 *
	 * @return the squad
	 */
	public Squad getSquad() {
		return squad;
	}

	/**
	 * Set the value of property {@code squad}.
	 *
	 * @param squad the squad to set
	 */
	public void setSquad(Squad squad) {
		this.squad = squad;
	}

	/**
	 * Get the value of property {@code villain}.
	 *
	 * @return the villain
	 */
	public Boolean getVillain() {
		return villain;
	}

	/**
	 * Set the value of property {@code villain}.
	 *
	 * @param villain the villain to set
	 */
	public void setVillain(Boolean villain) {
		this.villain = villain;
	}

	@Override
	public ToStringCreator toString(ToStringCreator creator) {
		return creator
			.append("q", q)
			.append("squad", squad)
			.append("villain", villain)
			;
	}
	
}
