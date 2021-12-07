package io.github.math0898.rpgframework.parties;

import io.github.math0898.rpgframework.PlayerManager;
import io.github.math0898.rpgframework.RpgPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PartyCommand implements CommandExecutor { // todo copies from RPG 1.0 - needs cleaning.

    /**
     * Tab completer which fills in options for this command.
     */
    public TabCompleter autocomplete = new AutocompleteParty();

    /**
     * The prefix sent with every message.
     */
    private static final String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;

    /**
     * Sends the given message to the given sender. todo update this using lang
     *
     * @param sender The person to send the message to.
     * @param message The message to send to this player.
     */
    public void send (CommandSender sender, String message) {
        send(sender, message, true);
    }

    /**
     * Sends the given message to the given sender. todo update this using lang
     *
     * @param sender The person to send the message to.
     * @param message The message to send to this player.
     * @param prefix Whether to send the prefix.
     */
    public void send (CommandSender sender, String message, boolean prefix) {
        if (prefix) message = PartyCommand.prefix + message;
        sender.sendMessage(message);
    }

    /**
     * Code to be executed on run.
     * @return Whether the command executed successfully or not
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //No Args
        if (args.length == 0) {
            send(sender, "Sorry but something about that usage seemed off. Here is a list of options.");
            send(sender, "- accept: Accept an incoming invite.", false);
            send(sender, "- chat: Toggle party chat.", false);
            send(sender, "- info: View status information about party members.", false);
            send(sender, "- invite <name>: Invite the given online player.", false);
            send(sender, "- kick <name>: Remove the given player from your party.", false);
            send(sender, "- leave: Leave the party you are currently in.", false);
            send(sender, "- list: Lists members of your party including invitations.", false);
            send(sender, "- summon: Summons party members to your location.", false);
            return true;
        } else if (!(sender instanceof Player)) {
            send(sender, "Please only run this command as a player.");
            return true;
        }

        //Accept Subcommand
        if (args[0].equalsIgnoreCase("accept")) acceptSubcommand(sender);

        //Info Subcommand TODO
        if (args[0].equalsIgnoreCase("info")) infoSubcommand(sender);

        //Invite Subcommand
        if (args[0].equalsIgnoreCase("invite")) inviteSubcommand(sender, args);

        //Kick Subcommand TODO
        if (args[0].equalsIgnoreCase("kick")) kickSubcommand(sender, args);

        //Leave Subcommand
        if (args[0].equalsIgnoreCase("leave")) leaveSubcommand(sender);

        //List Subcommand
        if (args[0].equalsIgnoreCase("list")) listSubcommand(sender);

        //Summon Subcommand TODO
        if (args[0].equalsIgnoreCase("summon")) summonSubcommand(sender);

        if (args[0].equalsIgnoreCase("chat")) chatSubcommand(sender);

        return true;
    }

    private void acceptSubcommand(CommandSender sender) {
        RpgPlayer rpgPlayer = PlayerManager.getPlayer(((Player) sender).getUniqueId());
        assert rpgPlayer != null;
        Party pending = rpgPlayer.getPendingParty();
        Party party = rpgPlayer.getParty();
        if (party != null) send(sender, "You are already in a party!");
        else if (pending != null) {
            if (pending.addPlayer((Player) sender)) {
                pending.sendAll(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] [" + rpgPlayer.getFormattedClass() + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + rpgPlayer.getBukkitPlayer().getName() + " has joined the party!");
                send(sender, "You have joined the party!");
                rpgPlayer.joinParty(pending);
            } else send(sender, "Sorry, but that party is full.");
        } else send(sender,"You do not have any party invites.");
    }

    //TODO
    private void infoSubcommand(CommandSender sender) { send(sender, "Work in progress."); }

    private void inviteSubcommand(CommandSender sender, String[] args) {
        RpgPlayer rpgPlayer = PlayerManager.getPlayer(((Player) sender).getUniqueId());
        assert rpgPlayer != null;
        Party party = rpgPlayer.getParty();

        if (args.length == 1) { send(sender, "You must list a player as an argument. /party invite <name>"); return; }
        if (party == null) {
            party = new Party((Player) sender);
            rpgPlayer.joinParty(party);
            PartyManager.addParty(party);
        }

        ArrayList<RpgPlayer> invitedPlayers = new ArrayList<>();
        for (int i = 1; i < args.length; i++) invitedPlayers.add(PlayerManager.getPlayer(args[i]));

        for (RpgPlayer p: invitedPlayers)  {
            send(p.getBukkitPlayer(), "You have been invited to join " + rpgPlayer.getName() + "'s party.");
            send(rpgPlayer.getBukkitPlayer(), "You have invited " + p.getName() + " to the party.");
            p.setPendingParty(party);
        }
    }

    //TODO
    private void kickSubcommand(CommandSender sender, String[] args) { send(sender, "Work in progress."); }

    private void leaveSubcommand(CommandSender sender) {
        RpgPlayer rpgPlayer = PlayerManager.getPlayer(((Player) sender).getUniqueId());
        assert rpgPlayer != null;
        Party party = rpgPlayer.getParty();
        if (party == null) { send(sender, "You are not in a party and thus cannot leave the party."); return; }

        rpgPlayer.leaveParty();
        party.removePlayer((Player) sender);
        send(sender, "You have left the party.");
        party.sendAll(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] [" + rpgPlayer.getFormattedClass() + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + rpgPlayer.getBukkitPlayer().getName() + " has left the party.");
    }

    private void listSubcommand(CommandSender sender) {
        send(sender, "Work in progress.");
//        RpgPlayer rpgPlayer = PlayerManager.getPlayer(((Player) sender).getUniqueId());
//        assert rpgPlayer != null;
//        Party party = rpgPlayer.getParty();
//
//        if (party == null) {
//            send(sender, "You do not belong to a party. A party can be started by inviting another player or accepting an invite from someone else.");
//        } else {
//            send(sender, "This is your current party roster:");
//            ArrayList<RpgPlayer> players = party.getRpgPlayers();
//            int i = 1;
//            for (RpgPlayer p: players) {
//                send(sender, ChatColor.GRAY + "" + i + ". " + ChatColor.DARK_GRAY + "[" + p.getFormattedClass() + ChatColor.DARK_GRAY + "] " + p.getPlayerRarity() + p.getName(), false);
//                i++;
//            }
//        }
    }

    //TODO
    private void summonSubcommand(CommandSender sender) { send(sender, "Work in progress."); }

    /**
     * Toggles party chat for the command sender.
     *
     * @param sender The player who wishes to have their party chat toggled.
     */
    private void chatSubcommand (CommandSender sender) {
        PartyManager.togglePartyChat((Player) sender);
    }
}

class AutocompleteParty implements TabCompleter {
    /**
     * Tab completion of the command described above.
     * @return List<String> of options
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("party")) {
            ArrayList<String> list = new ArrayList<>();
            if (args.length == 1) {
                //Keep this sorted. It saves an O(n log n) sort
                String[] Options = {"accept", "chat", "info", "invite", "kick", "leave", "list", "summon"};

                if (!args[0].equals("")) { for (String o: Options) if (o.toLowerCase().startsWith(args[0].toLowerCase())) list.add(o); }
                else list.addAll(Arrays.asList(Options));

                return list;
            } else if (args.length == 2 && !args[1].equals("list") && !args[1].equals("summon")) return null;
            else return list;
        }
        return null;
    }
}