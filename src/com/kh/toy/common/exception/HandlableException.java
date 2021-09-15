package com.kh.toy.common.exception;

import com.kh.toy.common.code.ErrorCode;

public class HandlableException extends RuntimeException{

	private static final long serialVersionUID = 7847451612288681161L;
	public  ErrorCode error;
	
	public HandlableException(ErrorCode error) {
		this.error = error;
		this.setStackTrace(new StackTraceElement[0]);
	}
	public HandlableException(ErrorCode error, Exception e) {
		this.error = error;
		e.printStackTrace();
		this.setStackTrace(new StackTraceElement[0]);
		
	}
	//1. �ֿܼ� �α�
	//2. result.jsp�� ����� ����ڿ��� �˶��޽��� ����ֱ�
	//	  �߻��� ���ܺ� ���� �޼�����, �������� ���

}
