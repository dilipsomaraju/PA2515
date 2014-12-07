package dao;

import bean.DiskData;

/**
 * @author SuperSun
 * An Interface to access local files
 */
public interface Data {
	/**
	 * Load Data
	 */
	public DiskData loadData();
	
	/**
	 * Save Data
	 */
	public void saveData(DiskData dd);
}
