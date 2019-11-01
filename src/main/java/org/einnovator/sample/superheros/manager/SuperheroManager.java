/**
 * 
 */
package org.einnovator.sample.superheros.manager;

import java.util.List;

import org.einnovator.jpa.manager.ManagerBase2;
import org.einnovator.sample.superheros.model.Superhero;
import org.einnovator.sample.superheros.modelx.SuperheroFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author support@einnovator.org
 *
 */
public interface SuperheroManager extends ManagerBase2<Superhero, Long> {

	Page<Superhero> findAll(SuperheroFilter filter, Pageable pageable);

	Superhero findOneByName(String name);

	void createOrUpdate(List<Superhero> superheros);

	void populate(boolean force);

}
