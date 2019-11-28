package io.starter.ignite.model;

/**
 * Ensure the most basic operation of all Data objects
 * 
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */
public interface DataModelObject {
	public DataModelObject getDelegate();
	public String toJSON();
}
