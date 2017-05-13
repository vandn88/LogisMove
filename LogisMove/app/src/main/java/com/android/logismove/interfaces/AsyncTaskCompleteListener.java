package com.android.logismove.interfaces;

/**
 * @interface: 		AsyncTaskCompleteListener
 * @Description: 	asyncTask complete listerner
 * @author: 		vandn
 * @create date: 	05/11/2014
 * */
public interface AsyncTaskCompleteListener<T> {
	 public void onTaskComplete(T result);	 
	 public void onFailure(int errorCode);
}
