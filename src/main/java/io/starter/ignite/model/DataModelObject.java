package io.starter.ignite.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Ensure the most basic operation of all Data objects
 * 
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */
public interface DataModelObject extends java.io.Serializable  {
	@JsonIgnore
	public Object getDelegate();
	public String toJSON();
}
