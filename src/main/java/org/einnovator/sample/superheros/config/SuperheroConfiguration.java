package org.einnovator.sample.superheros.config;

import org.einnovator.util.model.ObjectBase;
import org.einnovator.util.model.ToStringCreator;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("movies")
public class SuperheroConfiguration extends ObjectBase {

	public SuperheroConfiguration() {
	}
	


	@Override
	public ToStringCreator toString(ToStringCreator creator) {
		return creator;
	}

}
