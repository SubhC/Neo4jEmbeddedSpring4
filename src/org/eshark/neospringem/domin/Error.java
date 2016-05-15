package org.eshark.neospringem.domin;

public class Error {
	
	private Exception exception;

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}
	
	public boolean isThereException() {
		return this.exception == null? false:true;
	}
}
