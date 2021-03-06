package com.xenry.stagecraft.mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.Core;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 5/13/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class MongoManager extends Manager<Core> {
	
	private final MongoClient mongo;
	private final DB coreDB;
	
	public MongoManager(Core plugin){
		super("Mongo", plugin);
		
		mongo = new MongoClient("localhost", 27017);
		//noinspection deprecation
		coreDB = mongo.getDB("stagecraft");
	}
	
	public MongoClient getMongo() {
		return mongo;
	}
	
	public DB getCoreDB() {
		return coreDB;
	}
	
	public DBCollection getCoreCollection(String name){
		return coreDB.getCollection(name);
	}
	
}
