package com.smacna.util;

import java.util.Comparator;

import com.smacna.model.OutPutScreen;

public class OutPutScreenSorting implements Comparator<OutPutScreen>{

	public int compare(OutPutScreen o1, OutPutScreen o2){
		
		 int i = Integer.valueOf(o1.getFinalDuctGage()).compareTo(Integer.valueOf(o2.getFinalDuctGage()));
		 int j = Integer.valueOf(o1.getScore()).compareTo(Integer.valueOf(o2.getScore()));

		 if(i==0){
			 System.out.println("o1 Gage is greater thn o2");
			 return j;
	}if(i==-1){
		System.out.println("o1 Gage is smaller thn o2");
		if(o1.getScore()==0||o2.getScore()==0){
			return -1;
		}
		if(j==-1){
			return 1;
		}

	}if(i==1&&j==0){
		return -1;
	}
		 return i;
		
	}
	
	
}