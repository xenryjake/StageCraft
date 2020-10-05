package com.xenry.stagecraft.survival.economy;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 9/11/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class DiamondsCommand extends Command<Survival,EconomyManager> {
	
	public DiamondsCommand(EconomyManager manager){
		super(manager, Rank.ADMIN, "diamonds");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 2){
			profile.sendMessage(M.usage("/" + label + " <add|remove> <amount>"));
			return;
		}
		args[0] = args[0].toLowerCase();
		boolean remove;
		if(args[0].startsWith("a")){
			remove = false;
		}else if(args[0].startsWith("r")){
			remove = true;
		}else{
			profile.sendMessage(M.error("Add or remove?"));
			return;
		}
		int amount;
		try{
			amount = Integer.parseInt(args[1]);
		}catch(Exception ex){
			profile.sendMessage(M.error("Invalid integer."));
			return;
		}
		if(remove){
			if(manager.removeDiamondsFromInventory(profile.getPlayer(), amount)){
				profile.sendMessage(M.msg + "Success: removed " + amount + " diamonds.");
			}else{
				profile.sendMessage(M.error("Failed! Not enough diamonds or severe error."));
			}
		}else{
			int n = manager.addDiamondsToInventory(profile.getPlayer(), amount);
			if(n == 0){
				profile.sendMessage(M.msg + "Success: added " + amount + " diamonds.");
			}else{
				profile.sendMessage(M.msg + "You didn't have room for " + n + " of the diamonds.");
			}
		}
	}
	
}
