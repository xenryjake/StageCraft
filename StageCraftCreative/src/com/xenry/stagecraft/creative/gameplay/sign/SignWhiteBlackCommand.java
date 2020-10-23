package com.xenry.stagecraft.creative.gameplay.sign;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.profile.Setting;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 8/2/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SignWhiteBlackCommand extends Command<Creative,GameplayManager> {
	
	public SignWhiteBlackCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "white", "black");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		boolean white;
		if(label.equalsIgnoreCase("white")){
			white = true;
		}else if(label.equalsIgnoreCase("black")){
			white = false;
		}else{
			profile.sendMessage(M.error("Invalid default sign color."));
			return;
		}
		profile.setSetting(Setting.WHITE_SIGN_TEXT, white);
		profile.sendMessage(M.msg + "Your default sign color has been set to " + M.WHITE + (white ? "white" : "black") + M.msg + ".");
	}
	
}
