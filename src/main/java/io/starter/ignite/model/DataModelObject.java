package io.starter.ignite.model;

/**
 * Ensure the most basic operation of all Data objects
 * 
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */
public interface DataModelObject extends java.io.Serializable  {
	public Object getDelegate();
	public String toJSON();
}
