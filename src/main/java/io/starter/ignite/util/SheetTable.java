package io.starter.ignite.util;

import java.util.ArrayList;
import java.util.List;

import io.starter.OpenXLS.CellHandle;
import io.starter.OpenXLS.NameHandle;
import io.starter.OpenXLS.RowHandle;
import io.starter.OpenXLS.WorkBookHandle;

/**
 * a hybrid utility class that emulates a recordset, a table, and a spreadsheet
 * as needed
 * 
 * @author John McMahon ~ github: SpaceGhost69 | twitter: @TechnoCharms
 * 
 */
public class SheetTable {

	// 1st row is table headers
	CellHandle[]	headercells	= null;
	List<String>	headers		= new ArrayList<String>();

	RowHandle[]		rows		= null;
	NameHandle		myname		= null;
	WorkBookHandle	mybook		= null;
	boolean			autoCommit	= false;

	/**
	 * @return the headers
	 */
	public List getHeaders() {
		return headers;
	}

	public int getColumnCount() {
		return headercells.length;
	}

	/**
	 * get the number of rows, includes table header row
	 * 
	 * @return
	 */
	public int getRowCount() {
		return rows.length;
	}

	/**
	 * @return the autoCommit
	 */
	public boolean isAutoCommit() {
		return autoCommit;
	}

	/**
	 * @param autoCommit
	 *            the autoCommit to set
	 */
	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	public SheetTable(String name, WorkBookHandle myb) throws Exception {
		mybook = myb;
		myname = myb.getNamedRange(name);
		init();
	}

	private void init() throws Exception {
		rows = myname.getCellRanges()[0].getRows();
		// 1st row is table headers
		headercells = rows[0].getCells();
		for (int t = 0; t < headercells.length; t++) {
			CellHandle cx = headercells[t];
			headers.add(cx.getStringVal().toLowerCase());
		}
	}

	/**
	 * returns a single cell's value as an Object
	 * 
	 * @param rown
	 * @param colname
	 *            case insensitive
	 * @return
	 */
	public Object getValue(int rown, String colname) {
		int cnum = headers.indexOf(colname.toLowerCase());
		CellHandle cx = rows[rown].getCells()[cnum];
		return cx.getVal();
	}

	/**
	 * returns a single cell's value as a String
	 * 
	 * @param rown
	 * @param colname
	 *            case insensitive
	 * @return
	 */
	public String getStringValue(int rown, String colname) {
		int cnum = headers.indexOf(colname.toLowerCase());
		CellHandle cx = rows[rown].getCells()[cnum];
		return cx.getStringVal();
	}

	/**
	 * returns a single cell's formatted String value
	 * 
	 * @param rown
	 * @param colname
	 *            case insensitive
	 * @return
	 */
	public Object getFormattedValue(int rown, String colname) {
		int cnum = headers.indexOf(colname.toLowerCase());
		CellHandle cx = rows[rown].getCells()[cnum];
		return cx.getFormattedStringVal();
	}

	// WRITE BACK TO WORKBOOK

	/**
	 * update single cell value
	 * 
	 * @param rown
	 * @param colname
	 * @param obj
	 */
	public void setValue(int rown, String colname, Object obj) {
		int cnum = headers.indexOf(colname.toLowerCase());
		CellHandle cx = rows[rown].getCells()[cnum];
		cx.setVal(obj);
		// write to book
		commit(false);
	}

	/**
	 * add a row to the end of the name, expand name
	 * 
	 * @param objarr
	 * @throws Exception
	 */
	public void addRow(Object[] objarr) throws Exception {
		myname.addRow(objarr);
		commit(false);
	}

	/**
	 * delete a row from the name, contract name
	 * 
	 * @param rown
	 * @throws Exception
	 */
	public void deleteRow(int rown) throws Exception {
		rows[rown].getWorkSheetHandle().removeRow(rows[rown].getRowNumber());
		commit(false);
	}

	/**
	 * auto-commit
	 * 
	 * @param force
	 */
	public void commit(boolean force) {
		// write to book
		if (this.isAutoCommit() || force) {
			mybook.close();
		} else {
			System.err
					.println("AUTO-COMMIT NOT ENABLED. USE 'FORCE' TO WRITE TO BOOK");
		}
	}

	/**
	 * get a row of raw values
	 * 
	 * @param s
	 * @return
	 */
	public Object[] getRowObjects(int s) {
		CellHandle[] cx = rows[s].getCells();
		Object[] ret = new Object[cx.length];
		for (int t = 0; t < cx.length; t++) {
			ret[t] = cx[t].getVal();
		}
		return ret;
	}
}