package io.starter.ignite.model;

/**
 * Ensure the most basic operation of all Data objects
 * 
 * @author john
 *
 */
public interface DataModelObject {
	public DataModelObject getDelegate();
	public String toJSON();
}
