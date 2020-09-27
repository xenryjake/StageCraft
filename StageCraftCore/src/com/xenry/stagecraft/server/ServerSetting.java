package com.xenry.stagecraft.server;
import com.mongodb.BasicDBObject;
import com.xenry.stagecraft.util.NumberUtil;
import org.jetbrains.annotations.Nullable;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/19/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class ServerSetting extends BasicDBObject {
	
	@Deprecated
	public ServerSetting(){
		//required for Mongo instantiation
	}
	
	public ServerSetting(String key, Type type){
		put("key", key);
		put("type", type.name());
	}
	
	public String getKey(){
		return (String)get("key");
	}
	
	public Type getType(){
		return Type.valueOf((String)get("type"));
	}
	
	public ServerSetting setStringValue(String stringValue){
		if(getType() != Type.STRING){
			throw new UnsupportedOperationException("Wrong setting datatype");
		}
		put("stringValue", stringValue);
		return this;
	}
	
	@Nullable
	public String getStringValue(){
		return (String) get("stringValue");
	}
	
	public ServerSetting setBooleanValue(Boolean booleanValue){
		if(getType() != Type.BOOLEAN){
			throw new UnsupportedOperationException("Wrong setting datatype");
		}
		put("booleanValue", booleanValue);
		return this;
	}
	
	@Nullable
	public Boolean getBooleanValue(){
		return (Boolean) get("booleanValue");
	}
	
	public ServerSetting setIntegerValue(Integer integerValue){
		if(getType() != Type.INTEGER){
			throw new UnsupportedOperationException("Wrong setting datatype");
		}
		put("integerValue", integerValue);
		return this;
	}
	
	@Nullable
	public Integer getIntegerValue(){
		return (Integer) get("integerValue");
	}
	
	public ServerSetting setLongValue(Long longValue){
		if(getType() != Type.LONG){
			throw new UnsupportedOperationException("Wrong setting datatype");
		}
		put("longValue", longValue);
		return this;
	}
	
	@Nullable
	public Long getLongValue(){
		return (Long) get("longValue");
	}
	
	public ServerSetting setFloatValue(Float floatValue){
		if(getType() != Type.FLOAT){
			throw new UnsupportedOperationException("Wrong setting datatype");
		}
		put("floatValue", floatValue);
		return this;
	}
	
	@Nullable
	public Float getFloatValue(){
		Object obj = get("floatValue");
		if(obj instanceof Float){
			return (Float)obj;
		}else{
			return NumberUtil.doubleToFloat((Double)obj);
		}
	}
	
	public ServerSetting setDoubleValue(Double doubleValue){
		if(getType() != Type.DOUBLE){
			throw new UnsupportedOperationException("Wrong setting datatype");
		}
		put("doubleValue", doubleValue);
		return this;
	}
	
	@Nullable
	public Double getDoubleValue(){
		return (Double) get("doubleValue");
	}
	
	public enum Type {
		STRING, BOOLEAN, INTEGER, LONG, FLOAT, DOUBLE
	}
	
}
