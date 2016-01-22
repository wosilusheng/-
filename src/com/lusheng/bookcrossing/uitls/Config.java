package com.lusheng.bookcrossing.uitls;

public interface Config {
	int WRONG_TOKEN_ERROR_CODE = 100;
	int EMPTY_PRAMETERS_ERROR_CODE = 101;
	int INVALID_PRAMETERS_ERROR_CODE = 102;
	int SERVER_BUG_ERROR_CODE = 103;
	int OTHER_ERROR_CODE=104;
	String SERVER_BUG_ERROR_STR="服务器内部错误";
	String AVATAR_BASE_PATH="/file/image/avatar/";
	String BOOK_BASE_PATH="/file/image/book/";
}
