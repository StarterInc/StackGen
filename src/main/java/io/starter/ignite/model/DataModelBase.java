package io.starter.ignite.model;

import java.util.List;

/**
 * TODO: figure out role for this or remove
 * 
 * @author john
 *
 */
public abstract class DataModelBase implements DataModelObject {

	/**
	 * fetch a list of this item from cache or persistence
	 */
	public List<DataModelBase> getList() {
		return null;
	}

	/**
	 * fetch a DataModelBase item from cache or persistence
	 */
	public DataModelBase get() {
		return null;
	}

	/**
	 * save a DataModelBase item to cache or persistence
	 */
	public DataModelBase persist() {

		System.out.println("PERSISTING: " + this);

		return null;
	}

}
