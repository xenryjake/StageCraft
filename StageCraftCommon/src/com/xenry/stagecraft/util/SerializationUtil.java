package com.xenry.stagecraft.util;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SerializationUtil {
	
	private SerializationUtil(){}
	
	public static String serializeBase64(Object object) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		new ObjectOutputStream(out).writeObject(object);
		return Base64.getEncoder().encodeToString(out.toByteArray());
	}
	
	public static Object deserializeBase64(String string) throws Exception {
		return new ObjectInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(string))).readObject();
	}
	
	public static String serializeHex(Object object) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		new ObjectOutputStream(out).writeObject(object);
		return HexBin.encode(out.toByteArray());
	}
	
	public static Object deserializeHex(String string) throws Exception {
		return new ObjectInputStream(new ByteArrayInputStream(HexBin.decode(string))).readObject();
	}
	
}
