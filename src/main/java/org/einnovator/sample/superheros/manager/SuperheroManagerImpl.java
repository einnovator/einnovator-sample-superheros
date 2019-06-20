/**
 * 
 */
package org.einnovator.sample.superheros.manager;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.einnovator.common.config.AppConfiguration;
import org.einnovator.common.config.UIConfiguration;
import org.einnovator.jpa.manager.ManagerBaseImpl3;
import org.einnovator.jpa.repository.RepositoryBase2;
import org.einnovator.sample.superheros.model.Squad;
import org.einnovator.sample.superheros.model.Superhero;
import org.einnovator.sample.superheros.modelx.SuperheroFilter;
import org.einnovator.sample.superheros.repository.SuperheroRepository;
import org.einnovator.social.client.manager.ChannelManager;
import org.einnovator.social.client.model.Channel;
import org.einnovator.util.MappingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class SuperheroManagerImpl extends ManagerBaseImpl3<Superhero> implements SuperheroManager {

	private final Log logger = LogFactory.getLog(getClass());

	public static final String SUPERHEROS_RESOURCE_JSON = "data/superheros.json";

	@Autowired
	private SuperheroRepository repository;

	@Autowired
	private ChannelManager channelManager;

	@Autowired
	private UIConfiguration ui;

	@Autowired
	private AppConfiguration app;

	@Override
	protected RepositoryBase2<Superhero, Long> getRepository() {
		return repository;
	}
	
	@Override
	public Superhero findOneByName(String name) {
		Optional<Superhero> superhero = repository.findOneByName(name);
		return superhero.isPresent() ? processAfterLoad(superhero.get(), null) : null;
	}

	@Override
	public Page<Superhero> findAll(SuperheroFilter filter, Pageable pageable) {
		populate();
		Page<Superhero> page = null;
		if (filter!=null) {
			if (filter.getSquad()!=null || filter.getVillain()!=null) {
				String q = filter.getQ()!=null ?  "%" + filter.getQ().trim() + "%" : "%";
				Collection<Squad> squads = filter.getSquad()!=null ? Collections.singleton(filter.getSquad()) : Arrays.asList(Squad.values());
				if (filter.getVillain()!=null) {
					page = repository.findAllByNameLikeAndSquadInAndVillain(q, squads, filter.getVillain(), pageable);
				} else {
					page = repository.findAllByNameLikeAndSquadIn(q, squads, pageable);
				}
			} else if (filter.getQ()!=null){
				String q = "%" + filter.getQ().trim() + "%";
				page = repository.findAllByNameLike(q, pageable);					
			}
		}
		if (page==null) {
			page = repository.findAll(pageable);
		}
		return processAfterLoad(page, null);
	}

	@Override
	public void processAfterPersistence(Superhero superhero) {
		super.processAfterPersistence(superhero);
		Channel channel = superhero.makeChannel(getBaseUri());
		channel = channelManager.createOrUpdateChannel(channel);
		if (channel!=null && superhero.getChannelId()==null) {
			superhero.setChannelId(channel.getUuid());
			repository.save(superhero);			
		}
	}
	

	public String getBaseUri() {
		return ui.getLink(app.getId());
	}
	
	private boolean init;

	public void populate() {
		populate(true);
	}
	
	@Override
	public void populate(boolean force) {
		if (force || !init) {
			init = true;
			if (!force && repository.count()!=0) {
				return;
			}
			logger.info("populate: ");
			Superhero[] superheros = MappingUtils.readJson(new ClassPathResource(SUPERHEROS_RESOURCE_JSON), Superhero[].class);
			createOrUpdate(Arrays.asList(superheros));
		}
		
	}

	@Override
	public void createOrUpdate(List<Superhero> superheros) {
		if (superheros != null) {
			for (Superhero superhero : superheros) {
				if (superhero.getName()==null) {
					logger.warn("createOrUpdate: skipping:" + superhero);
					continue;
				}
				Superhero superhero2 = findOneByName(superhero.getName());
				if (superhero2 != null) {
					superhero.setId(superhero2.getId());
					logger.info("createOrUpdate: update:" + superhero);
					update(superhero);
				} else {
					logger.info("createOrUpdate: create:" + superhero);
					create(superhero);
				}
			}
		}
	}


}
