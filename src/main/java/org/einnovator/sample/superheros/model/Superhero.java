package org.einnovator.sample.superheros.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

import org.einnovator.jpa.model.EntityBase2;
import org.einnovator.social.client.model.Channel;
import org.einnovator.social.client.model.ChannelType;
import org.einnovator.util.model.Ref;
import org.einnovator.util.model.RefBuilder;
import org.einnovator.util.model.ToStringCreator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Superhero extends EntityBase2<Long> {

	@Column(length=1024)
	private String name;

	@Enumerated(EnumType.STRING)
	private Squad squad;

	private Boolean villain;

	@Column(length=255)
	private String avatar;


	@Lob
	private String description;
	
	@Column(length=128)
	private String channelId;

	public Superhero() {
	}
	
	//
	// Getters and Setters
	//
	
	/**
	 * Get the value of property {@code name}.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the value of property {@code name}.
	 *
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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

	/**
	 * Get the value of property {@code avatar}.
	 *
	 * @return the avatar
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * Set the value of property {@code avatar}.
	 *
	 * @param avatar the avatar to set
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/**
	 * Get the value of property {@code description}.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the value of property {@code description}.
	 *
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get the value of property {@code channelId}.
	 *
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}

	/**
	 * Set the value of property {@code channelId}.
	 *
	 * @param channelId the channelId to set
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public ToStringCreator toString1(ToStringCreator creator) {
		return super.toString1(creator)
				.append("name", name)
				.append("squad", squad)
				.append("villain", villain)
				.append("avatar", avatar)				
				.append("description", description)
				;
	}
	
	public Channel makeChannel(String baseUri) {
		return (Channel)new Channel()
				.withName(name)
				.withPurpose("Discussion about " + (Boolean.TRUE.equals(villain) ? "villain " : "") + name + " from " + squad)
				.withImg(avatar)
				.withThumbnail(avatar)
				.withType(ChannelType.COMMENTS)
				.withRef(makeRef(baseUri))
				.withUuid(channelId);
	}
	
	public Ref makeRef(String baseUri) {
		return new RefBuilder()
				.id(uuid)
				.type(getClass().getSimpleName())
				.name(name)
				.img(avatar)
				.thumbnail(avatar)
				.redirectUri(baseUri + "/superhero/" + uuid)
				.pingUri(baseUri + "/api/superhero/" + uuid)
				.build();
	}

	
}
