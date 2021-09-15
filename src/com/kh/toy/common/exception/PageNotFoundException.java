package com.kh.toy.common.exception;

public class PageNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -521587827126031031L;
	public PageNotFoundException() {
		//스택트레이스를 비워낸다
		this.setStackTrace(new StackTraceElement[0]);
 }
}