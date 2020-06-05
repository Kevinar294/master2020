package com.prototype.app;

import java.util.ArrayList;

public class Permutation {
	ArrayList<IndexArray> index;
	
	public Permutation() {
		index = new ArrayList<IndexArray>();
	}
	
	public ArrayList<IndexArray> getResult(){
		ArrayList<IndexArray> result = new ArrayList<IndexArray>();
		for(IndexArray e:index) {
			if(!result.contains(e)) {
				result.add(e);
			}
		}
		return result;
	}
	
	public void Permutate(int a[], int size, int n) {
		
		if (size == 1) {
			new IndexArray(a[0],a[1]);
			index.add(new IndexArray(a[0],a[1]));
			index.add(new IndexArray(a[0],a[0]));
		}
  
        for (int i=0; i<size; i++) 
        { 
            Permutate(a, size-1, n); 
  
            // if size is odd, swap first and last 
            // element
            if (size % 2 == 1) 
            { 
                int temp = a[0]; 
                a[0] = a[size-1]; 
                a[size-1] = temp; 
            } 
  
            // If size is even, swap ith and last 
            // element
            else
            { 
                int temp = a[i]; 
                a[i] = a[size-1]; 
                a[size-1] = temp; 
            } 
        } 
	}
}
