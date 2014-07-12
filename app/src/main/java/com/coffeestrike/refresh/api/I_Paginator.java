package com.coffeestrike.refresh.api;

import java.util.List;

public interface I_Paginator<T>{
	
	public interface PaginatorListener{
		public void onPageReady();
		public void onRefeshComplete();
		
	}
	
	public void registerListener(PaginatorListener listener);
	public void unregisterListener(PaginatorListener listener);
	
	public void requestNextSet();

	public List<T> getList();
	
	public void refresh();
	
	public void setPageSize(int pageSize);
	public int getPageSize();
	
	public boolean isRequestPending();

}
