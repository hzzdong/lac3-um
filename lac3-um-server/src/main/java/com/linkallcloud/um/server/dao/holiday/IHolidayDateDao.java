package com.linkallcloud.um.server.dao.holiday;

import java.util.List;

import com.linkallcloud.core.dao.IDao;
import com.linkallcloud.um.domain.holiday.HolidayDate;

public interface IHolidayDateDao extends IDao<HolidayDate> {

	/**
	 * Insert holiday date int.
	 *
	 * @param object the object
	 * @return the int
	 */
	int insertHolidayDate(HolidayDate object);

	/**
	 * Batch insert holiday date int.
	 *
	 * @param list the list
	 * @return the int
	 */
	int batchInsertHolidayDate(List<HolidayDate> list);

	/**
	 * Update holiday date int.
	 *
	 * @param object the object
	 * @return the int
	 */
	int updateHolidayDate(HolidayDate object);

	/**
	 * Query holiday date list.
	 *
	 * @param object the object
	 * @return the list
	 */
	List<HolidayDate> queryHolidayDate(HolidayDate object);

	/**
	 * Query holiday date limit 1 holiday date.
	 *
	 * @param object the object
	 * @return the holiday date
	 */
	HolidayDate queryHolidayDateLimit1(HolidayDate object);

}
