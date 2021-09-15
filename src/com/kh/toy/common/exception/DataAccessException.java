package com.kh.toy.common.exception;

import com.kh.toy.common.code.ErrorCode;

//����ó���� �������� �ʴ�  UnCheckedException
public class DataAccessException extends HandlableException{

	private static final long serialVersionUID = 521587827126031031L;
	
	public DataAccessException(Exception e) {
		super(ErrorCode.DATABASE_ACCESS_ERROR,e);
	}

}
