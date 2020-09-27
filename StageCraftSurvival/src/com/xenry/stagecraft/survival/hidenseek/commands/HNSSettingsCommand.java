package com.xenry.stagecraft.survival.hidenseek.commands;
import com.xenry.stagecraft.commands.Command;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.hidenseek.game.GameSettings;
import com.xenry.stagecraft.survival.hidenseek.HideNSeekManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

import static com.xenry.stagecraft.survival.hidenseek.HM.*;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 4/28/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class HNSSettingsCommand extends Command<Survival,HideNSeekManager> {
	
	public HNSSettingsCommand(HideNSeekManager manager){
		super(manager, Rank.ADMIN, "settings", "setting", "set");
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		serverPerform(profile.getPlayer(), args, label);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		GameSettings settings = manager.getSettings();
		if(args.length < 1){
			sender.sendMessage(msg + "Game Settings");
			sender.sendMessage(elm + " minimum-players" + gry + ": " + hlt + settings.getMinimumPlayers() + gry + " §o(min)");
			sender.sendMessage(elm + " hide-seconds" + gry + ": " + hlt + settings.getHideSeconds() + gry + " §o(hs)");
			sender.sendMessage(elm + " seek-seconds" + gry + ": " + hlt + settings.getSeekSeconds() + gry + " §o(ss)");
			sender.sendMessage(elm + " hider-movement-allowed" + gry + ": " + (settings.isHiderMovementAllowed() ? "§atrue" : "§cfalse") + gry + " §o(hma)");
			sender.sendMessage(elm + " see-hider-distance" + gry + ": " + (settings.canSeekersSeeHiderDistance() ? "§atrue" : "§cfalse") + gry + " §o(shd)");
			return;
		}
		if(args.length < 2){
			sender.sendMessage(err + "Usage: /ha setting <setting> <value>");
			return;
		}
		String stg = args[0].toLowerCase();
		String input = args[1].toLowerCase();
		switch (stg) {
			case "minimum-players":
			case "minimumplayers":
			case "min": {
				int value;
				try {
					value = Integer.parseInt(input);
				} catch(Exception ex) {
					sender.sendMessage(err + "Invalid integer.");
					return;
				}
				if(value <= 0 || value > 256) {
					sender.sendMessage(err + "Please specify an integer between 1 and 256.");
					return;
				}
				settings.setMinimumPlayers(value);
				sender.sendMessage(msg + "Set " + elm + "minimum-players" + msg + " to " + hlt + value + msg + ".");
				break;
			}
			case "hide-seconds":
			case "hideseconds":
			case "hs": {
				int value;
				try {
					value = Integer.parseInt(input);
				} catch(Exception ex) {
					sender.sendMessage(err + "Invalid integer.");
					return;
				}
				if(value <= 0 || value > 300) {
					sender.sendMessage(err + "Please specify an integer between 1 and 300.");
					return;
				}
				settings.setHideSeconds(value);
				sender.sendMessage(msg + "Set " + elm + "hide-seconds" + msg + " to " + hlt + value + msg + ".");
				break;
			}
			case "seek-seconds":
			case "seekseconds":
			case "ss": {
				int value;
				try {
					value = Integer.parseInt(input);
				} catch(Exception ex) {
					sender.sendMessage(err + "Invalid integer.");
					return;
				}
				if(value <= 0 || value > 3600) {
					sender.sendMessage(err + "Please specify an integer between 1 and 3600.");
					return;
				}
				settings.setSeekSeconds(value);
				sender.sendMessage(msg + "Set " + elm + "seek-seconds" + msg + " to " + hlt + value + msg + ".");
				break;
			}
			case "hider-movement-allowed":
			case "hidermovementallowed":
			case "hma": {
				boolean value;
				if(input.equals("true") || input.equals("on")) {
					value = true;
				} else if(input.equals("false") || input.equals("off")) {
					value = false;
				} else {
					sender.sendMessage(err + "Please specify a valid boolean.");
					return;
				}
				settings.setHiderMovementAllowed(value);
				sender.sendMessage(msg + "Set " + elm + "hider-movement-allowed" + msg + " to " + (value ? "§atrue" :
						"§cfalse") + msg + ".");
				break;
			}
			case "see-hider-distance":
			case "seehiderdistance":
			case "shd": {
				boolean value;
				if(input.equals("true") || input.equals("on")) {
					value = true;
				} else if(input.equals("false") || input.equals("off")) {
					value = false;
				} else {
					sender.sendMessage(err + "Please specify a valid boolean.");
					return;
				}
				settings.setSeekersSeeHiderDistance(value);
				sender.sendMessage(msg + "Set " + elm + "see-hider-distance" + msg + " to " + (value ? "§atrue" :
						"§cfalse") + msg + ".");
				break;
			}
			default:
				sender.sendMessage(err + "Invalid setting. Type " + hlt + "/ha settings" + err + " to see valid settings.");
				break;
		}
	}
	
	@Override
	protected List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return Arrays.asList("minimum-players", "hide-seconds", "seek-seconds", "hider-movement-allowed", "see-hider-distance");
	}
	
	@Override
	protected List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Arrays.asList("minimum-players", "hide-seconds", "seek-seconds", "hider-movement-allowed", "see-hider-distance");
	}
	
}
