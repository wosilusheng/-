package com.lusheng.bookcrossing.uitls;


public class BookcrossingUtils {
	public static int getLevelByPoints(int points){
		if(points<0){
			return 0;
		}
		return (points-1)/50+1;
	}
	
	public static int getCanBorrowOrDestineBookNumByLevel(int level){
		if(level<0){
			return 0;
		}
		return ((level-1)/10+1)*2;
	}

}
