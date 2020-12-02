package com.xenry.stagecraft.creative.gameplay.sign;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.chat.emotes.Emote;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.creative.Creative;
import com.xenry.stagecraft.creative.gameplay.GameplayManager;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.util.M;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 7/14/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class SignEditCommand extends Command<Creative,GameplayManager> {
	
	public SignEditCommand(GameplayManager manager){
		super(manager, Rank.MEMBER, "edit");
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		if(args.length < 2){
			profile.sendMessage(M.usage("/sign " + label + " <line> <text>"));
			return;
		}
		int line;
		try{
			line = Integer.parseInt(args[0]);
		}catch(Exception ex){
			profile.sendMessage(M.error("Invalid integer: " + args[0]));
			return;
		}
		if(line < 1 || line > 4){
			profile.sendMessage(M.error("Line must be between 1 and 4."));
			return;
		}
		Player player = profile.getPlayer();
		Block block = player.getTargetBlock(null, 5);
		if(!(block.getState() instanceof Sign)){
			profile.sendMessage(M.error("You aren't looking at a sign."));
			return;
		}
		
		String text = Joiner.on(' ').join(Arrays.copyOfRange(args, 1, args.length));
		if(SignEditHandler.COLOR_SIGNS.has(profile)){
			text = ChatColor.translateAlternateColorCodes('&', text);
		}
		if(Emote.EMOTE_ACCESS.has(profile)){
			text = Emote.replaceEmotes(text);
		}
		
		if(!manager.getSignEditHandler().checkPermission(player, block)){
			return;
		}
		
		Sign sign = (Sign)block.getState();
		
		sign.setLine(line - 1, text);
		
		sign.update();
		profile.sendMessage(M.msg + "You set line " + line + " to " + M.WHITE + text + M.msg + " on the sign.");
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		return args.length == 1 ? Arrays.asList("1", "2", "3", "4") : Collections.emptyList();
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
