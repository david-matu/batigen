package com.dave.apis.batigen.dao;

import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import com.dave.apis.batigen.mappers.AppMapper;

@RegisterMapper(AppMapper.class)
public interface AppDAO {

	@SqlQuery("SELECT 1")
	int testDBHealth();


/*
	* Custom SQL Functions to generate unique  IDs
	*/	
	@SqlQuery("select genUserID();")
	public long getUniqueUserId();

	@SqlQuery("select genIntegerItemID();")
	public int getUniqueIntegerItemID();

	@SqlQuery("select genLongItemID();")
	public long getUniqueLongItemID();

}
