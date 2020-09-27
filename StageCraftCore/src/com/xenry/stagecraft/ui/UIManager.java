package com.xenry.stagecraft.ui;
import com.xenry.stagecraft.Manager;
import com.xenry.stagecraft.Core;
import com.xenry.stagecraft.ui.page.UIPage;

import java.util.ArrayList;
import java.util.List;

/**
 * StageCraft Created by Henry Blasingame (Xenry) on 4/10/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Jake
 * is prohibited.
 */
public final class UIManager extends Manager<Core> {
	
	private UIClickHandler clickHandler;
	private final List<UIPage> allPages;
	
	public UIManager(Core plugin){
		super("UI", plugin);
		allPages = new ArrayList<>();
	}
	
	@Override
	protected void onEnable() {
		clickHandler = new UIClickHandler(this);
		registerListener(clickHandler);
	}
	
	public UIClickHandler getClickHandler() {
		return clickHandler;
	}
	
	public List<UIPage> getAllPages() {
		return allPages;
	}
	
	public void registerPage(UIPage page){
		allPages.add(page);
	}
	
}
