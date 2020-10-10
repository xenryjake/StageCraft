package com.xenry.stagecraft.bungee.proxy;
import com.mongodb.BasicDBObject;
import com.xenry.stagecraft.bungee.util.NumberUtil;
import org.jetbrains.annotations.Nullable;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/7/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public class ProxySetting extends BasicDBObject {

	@Deprecated
	public ProxySetting(){
		//required for Mongo instantiation
	}
	
	public ProxySetting(String key, Type type){
		put("key", key);
		put("type", type.name());
	}
	
	public String getKey(){
		return (String)get("key");
	}
	
	public Type getType(){
		return Type.valueOf((String)get("type"));
	}
	
	public void setStringValue(String stringValue){
		if(getType() != Type.STRING){
			throw new UnsupportedOperationException("Wrong setting datatype");
		}
		put("stringValue", stringValue);
	}
	
	@Nullable
	public String getStringValue(){
		return (String) get("stringValue");
	}
	
	public void setBooleanValue(Boolean booleanValue){
		if(getType() != Type.BOOLEAN){
			throw new UnsupportedOperationException("Wrong setting datatype");
		}
		put("booleanValue", booleanValue);
	}
	
	@Nullable
	public Boolean getBooleanValue(){
		return (Boolean) get("booleanValue");
	}
	
	public void setIntegerValue(Integer integerValue){
		if(getType() != Type.INTEGER){
			throw new UnsupportedOperationException("Wrong setting datatype");
		}
		put("integerValue", integerValue);
	}
	
	@Nullable
	public Integer getIntegerValue(){
		return (Integer) get("integerValue");
	}
	
	public void setLongValue(Long longValue){
		if(getType() != Type.LONG){
			throw new UnsupportedOperationException("Wrong setting datatype");
		}
		put("longValue", longValue);
	}
	
	@Nullable
	public Long getLongValue(){
		return (Long) get("longValue");
	}
	
	public void setFloatValue(Float floatValue){
		if(getType() != Type.FLOAT){
			throw new UnsupportedOperationException("Wrong setting datatype");
		}
		put("floatValue", floatValue);
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
	
	public void setDoubleValue(Double doubleValue){
		if(getType() != Type.DOUBLE){
			throw new UnsupportedOperationException("Wrong setting datatype");
		}
		put("doubleValue", doubleValue);
	}
	
	@Nullable
	public Double getDoubleValue(){
		return (Double) get("doubleValue");
	}
	
	public enum Type {
		STRING, BOOLEAN, INTEGER, LONG, FLOAT, DOUBLE
	}

}
