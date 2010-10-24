package no.f12.agiledeploy.deployer;

import org.springframework.dao.DataAccessException;

public class DatabaseInspectionException extends DataAccessException {

	private static final long serialVersionUID = 1L;

	public DatabaseInspectionException(String msg) {
		super(msg);
	}

	public DatabaseInspectionException(String msg, Exception e) {
		super(msg, e);
	}

}
