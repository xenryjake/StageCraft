package com.xenry.stagecraft.util.entity;
import org.bukkit.Chunk;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.xenry.stagecraft.util.entity.Entities.Category.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 11/9/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class EntityUtil {
	
	private EntityUtil(){}
	
	public static int countEntities(Chunk chunk){
		int i = 0;
		for(Entity entity : chunk.getEntities()){
			if(Entities.get(entity).category != PLAYER){
				i++;
			}
		}
		return i;
	}
	
	public static int countEntities(Chunk chunk, Entities.Category...categories){
		int i = 0;
		List<Entities.Category> catList = Arrays.asList(categories);
		for(Entity entity : chunk.getEntities()){
			if(catList.contains(Entities.get(entity).category)){
				i++;
			}
		}
		return i;
	}
	
	public static int countEntitiesExclude(Chunk chunk, Entities.Category...categories){
		List<Entities.Category> catList = new ArrayList<>(Arrays.asList(categories));
		catList.add(PLAYER);
		int i = 0;
		for(Entity entity : chunk.getEntities()){
			if(!catList.contains(Entities.get(entity).category)){
				i++;
			}
		}
		return i;
	}
	
	public static int countLivingEntities(Chunk chunk){
		int i = 0;
		for(Entity entity : chunk.getEntities()){
			if(entity instanceof LivingEntity && !(entity instanceof HumanEntity)){
				i++;
			}
		}
		return i;
	}
	
	public static int countMobs(Chunk chunk){
		int i = 0;
		for(Entity entity : chunk.getEntities()){
			if(Entities.get(entity).category.isMob()){
				i++;
			}
		}
		return i;
	}
	
}
