package com.xenry.stagecraft.creative.gameplay.pvptoggle;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.xenry.stagecraft.creative.gameplay.pvptoggle.PvPLock.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/22/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class PvPLockCommand extends Command<Creative,GameplayManager> {
	
	public static final Access ACCESS = Rank.HEAD_MOD;
	
	public PvPLockCommand(GameplayManager manager){
		super(manager, ACCESS, "pvplock", "lockpvp");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		if(args.length < 1){
			sender.sendMessage(M.msg + "Current PvP lock state: " + M.elm + manager.getPvPHandler().getLockState());
			sender.sendMessage(M.usage("/" + label + " <clear|on|off>"));
			return;
		}
		PvPLock lockState;
		switch(args[0].toLowerCase()){
			case "on":
			case "enable":
				lockState = LOCK_ON;
				break;
			case "off":
			case "disable":
				lockState = LOCK_OFF;
				break;
			case "none":
			case "clear":
				lockState = NONE;
				break;
			default:
				sender.sendMessage(M.error("Please specify a valid state: on, off, or clear."));
				return;
		}
		manager.getPvPHandler().setLockState(lockState);
		if(lockState == NONE){
			sender.sendMessage(M.msg + "You have cleared the PvP Lock.");
		}else{
			sender.sendMessage(M.msg + "You have locked all players' PvP to "
					+ (lockState == LOCK_ON ? "§aon" : "§coff") + M.msg + ".");
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Arrays.asList("none", "on", "off");
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Arrays.asList("none", "on", "off");
	}
	
}
