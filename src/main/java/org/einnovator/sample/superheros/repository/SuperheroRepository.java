package org.einnovator.sample.superheros.repository;

import java.util.Collection;
import java.util.Optional;

import org.einnovator.jpa.repository.RepositoryBase2;
import org.einnovator.sample.superheros.model.Squad;
import org.einnovator.sample.superheros.model.Superhero;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SuperheroRepository extends RepositoryBase2<Superhero, Long> {

	Page<Superhero> findAllByNameLike(String q, Pageable pageable);

	Page<Superhero> findAllByNameLikeAndSquadIn(String q, Collection<Squad> squads, Pageable pageable);

	Page<Superhero> findAllByNameLikeAndSquadInAndByVillain(String q, Collection<Squad> squads, boolean villain, Pageable pageable);

	Optional<Superhero> findOneByName(String name);

}
