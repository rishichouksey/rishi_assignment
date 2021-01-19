package com.rishi.common;

public enum RequestStatus {
	SUCCESS("success"), FAILURE("failure");

	private String status;

	private RequestStatus(final String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return status;
	}
}
