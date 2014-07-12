package com.coffeestrike.refresh.datatypes;

import com.coffeestrike.refresh.api.API;
import com.coffeestrike.refresh.api.Paginator;


public interface I_Source {
	
	public String getName();
	
	public API getAPI() throws ClassNotFoundException;
	
	public Paginator getPaginator();

}
