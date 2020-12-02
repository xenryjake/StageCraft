package com.xenry.stagecraft.survival.gameplay.commands;
import com.google.common.base.Joiner;
import com.xenry.stagecraft.command.Access;
import com.xenry.stagecraft.command.Command;
import com.xenry.stagecraft.profile.Profile;
import com.xenry.stagecraft.profile.Rank;
import com.xenry.stagecraft.survival.Survival;
import com.xenry.stagecraft.survival.gameplay.GameplayManager;
import com.xenry.stagecraft.util.M;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * StageCraft created by Henry Blasingame (Xenry) on 10/30/20
 * The content in this file and all related files are
 * Copyright (C) 2020 Henry Blasingame.
 * Usage of this content without written consent of Henry Blasingame
 * is prohibited.
 */
public final class BookCommand extends Command<Survival,GameplayManager> {
	
	public static final Access CHANGE_AUTHOR = Rank.HEAD_MOD;
	public static final Access CHANGE_TITLE = Rank.HEAD_MOD;
	public static final Access EDIT_OTHER = Rank.HEAD_MOD;
	public static final Access USE_COLORS = Rank.HEAD_MOD;
	
	public BookCommand(GameplayManager manager){
		super(manager, Rank.PREMIUM, "book");
		setCanBeDisabled(true);
	}
	
	@Override
	protected void serverPerform(CommandSender sender, String[] args, String label) {
		onlyForPlayers(sender);
	}
	
	@Override
	protected void playerPerform(Profile profile, String[] args, String label) {
		Player player = profile.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		ItemMeta itemMeta = item.getItemMeta();
		if(!(itemMeta instanceof BookMeta)){
			player.sendMessage(M.error("You are not holding a valid book."));
			return;
		}
		BookMeta meta = (BookMeta)itemMeta;
		String author = meta.getAuthor();
		String title = meta.getTitle();
		if(author != null && !author.trim().isEmpty() && !author.equals(player.getName()) && !EDIT_OTHER.has(profile)){
			player.sendMessage(M.error("You don't have permission to edit other player's books."));
			return;
		}
		if(item.getType() == Material.WRITTEN_BOOK){
			if(args.length >= 1){
				if(args[0].equalsIgnoreCase("author")){
					if(!CHANGE_AUTHOR.has(profile)){
						player.sendMessage(M.error("You can't change a book's author."));
						return;
					}
					if(args.length < 2){
						player.sendMessage(M.usage("/book author <name>"));
						return;
					}
					author = Joiner.on(' ').join(Arrays.copyOfRange(args, 1, args.length));
					if(USE_COLORS.has(profile)){
						author = ChatColor.translateAlternateColorCodes('&', author);
					}
					meta.setAuthor(author);
					item.setItemMeta(meta);
					player.sendMessage(M.msg + "Changed this book's author to: " + M.WHITE + author);
					return;
				}else if(args[0].equalsIgnoreCase("title")){
					if(!CHANGE_TITLE.has(profile)){
						player.sendMessage(M.error("You can't change a book's title."));
						return;
					}
					if(args.length < 2){
						player.sendMessage(M.usage("/book title <title>"));
						return;
					}
					title = Joiner.on(' ').join(Arrays.copyOfRange(args, 1, args.length));
					if(USE_COLORS.has(profile)){
						title = ChatColor.translateAlternateColorCodes('&', title);
					}
					meta.setTitle(title);
					item.setItemMeta(meta);
					player.sendMessage(M.msg + "Changed this book's title to: " + M.WHITE + title);
					return;
				}
			}
			ItemStack newBook = new ItemStack(Material.WRITABLE_BOOK, item.getAmount());
			newBook.setItemMeta(meta);
			player.getInventory().setItemInMainHand(newBook);
			player.sendMessage(M.msg + "Book unsigned.");
		}else if(item.getType() == Material.WRITABLE_BOOK){
			if(author == null || author.isEmpty()){
				meta.setAuthor(player.getName());
			}
			if(title == null || title.isEmpty()){
				meta.setTitle("Written Book");
			}
			ItemStack newBook = new ItemStack(Material.WRITTEN_BOOK, item.getAmount());
			newBook.setItemMeta(meta);
			player.getInventory().setItemInMainHand(newBook);
			player.sendMessage(M.msg + "Book signed.");
		}else{
			player.sendMessage(M.error("There is something wrong with the item you're holding."));
		}
	}
	
	@Override
	protected @NotNull List<String> playerTabComplete(Profile profile, String[] args, String label) {
		if(args.length == 1){
			List<String> results = new ArrayList<>();
			results.add("sign");
			results.add("unsign");
			if(CHANGE_AUTHOR.has(profile)){
				results.add("author");
			}
			if(CHANGE_TITLE.has(profile)){
				results.add("title");
			}
			return results;
		}else{
			return Collections.emptyList();
		}
	}
	
	@Override
	protected @NotNull List<String> serverTabComplete(CommandSender sender, String[] args, String label) {
		return Collections.emptyList();
	}
	
}
