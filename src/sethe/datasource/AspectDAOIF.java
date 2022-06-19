package sethe.datasource;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import sethe.model.Message;

public interface AspectDAOIF {
	/**
	 * Aspects columns pk
	 * @return
	 */
	public abstract String[] columnsAspectsId();

	/**
	 * Aspects values in the same order of the columnsAspectsId() function return
	 * @param ps
	 * @param parameterIndex
	 * @param m
	 * @throws SQLException 
	 */
	public abstract void putAspectsValues(PreparedStatement ps, int parameterIndex, Message m) throws SQLException;
}
