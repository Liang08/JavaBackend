package com.java.xuhaotian;

/**
 * 请求错误类型，包括错误代码以及错误信息
 * -1:Server Error
 * 1:userName or password empty when registration
 * 2:userName already exist when registration
 * 3:userName or password empty when login
 * 4:userName not exist when login
 * 5:password incorrect
 * 6:course or searchKey empty when getInstanceList
 * 7:name or course empty when getInfoByInstanceName
 * 8:inputQuestion empty when inputQuestion
 * 9:not logged in
 * 10:offset or limit negative when getInstanceList
 * 11:context empty when linkInstance
 * 12:uriName empty when getQuestionListByUriName
 * 13:offset or limit negative when getQuestionListByUriName
 * 14:password empty when modifying password
 * 15:invalid query when getInfoByInstanceName
 * 16:name or course empty when set/reset favourite or addInstanceHistory
 * 17 course or subjectName empty when getRelatedSubject
 * 18:course or searchKey empty when addSearchHistory
 * @author xht13127
 * 
 */
public class Error {
	private int code;
	private String message;

	public int getCode() {
    	return code;
	}

	public void setCode(int code) {
    	this.code = code;
	}

	public String getMessage() {
    	return message;
	}

	public void setMessage(String message) {
    	this.message = message;
	}
	
	public Error(int code, String message) {
		this.code = code;
		this.message = message;
	};
}
