package com.smacna.util;

import java.util.Comparator;

import com.smacna.model.OutPutScreen;

public class PrioritySorting implements Comparator<OutPutScreen>{
	
	public int compare(OutPutScreen o1, OutPutScreen o2){
		int i = Integer.valueOf(o1.getPriority()).compareTo(Integer.valueOf(o2.getPriority()));
		int j = Integer.valueOf(o1.getFinalDuctGage()).compareTo(Integer.valueOf(o2.getFinalDuctGage()));
		
		if(i==-1&&j==-1){
			return -1;
		}if(i==1&&j==0){
			return 1;
		}
		if(i==1&&j==1){
			return -1;
		}
		
		return i;
	}

}
