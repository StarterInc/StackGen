package io.starter.ignite.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * data methods
 *  
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 *
 */
public abstract class DataModelBase implements DataModelObject {
	protected static final Logger logger = LoggerFactory.getLogger(DataModelBase.class);

	/**
	 * fetch a list of this item from cache or persistence
	 */
	public List<DataModelBase> getList() {
		logger.error("NOT IMPLEMENTED LIST: " + this);
		return null;
	}

	/**
	 * fetch a DataModelBase item from cache or persistence
	 */
	public DataModelBase get() {
		logger.error("NOT IMPLEMENTED GET: " + this);
		return null;
	}

	/**
	 * save a DataModelBase item to cache or persistence
	 */
	public DataModelBase persist() {

		logger.error("NOT IMPLEMENTED PERSISTING: " + this);

		return null;
	}

	@Override
	public DataModelObject getDelegate() {
		logger.error("NOT IMPLEMENTED GETDELEGATE: " + this);
		return null;
	}

}
