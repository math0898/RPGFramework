package io.github.math0898.rpgframework.parties;

import io.github.math0898.rpgframework.PlayerManager;
import io.github.math0898.rpgframework.RpgPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static io.github.math0898.rpgframework.main.plugin;

/**
 * The Party Command is used for interacting and manipulating parties.
 *
 * @author Sugaku
 */
public class PartyCommand implements CommandExecutor { // todo could use quite a bit of optimization still.

    /**
     * The tab completer which fills in the options for this command.
     */
    public static TabCompleter autocomplete = new TabCompleter() {

        /**
         * Requests a list of possible completions for a command argument.
         *
         * @param sender Source of the command. For players tab-completing a command inside a command block, this will
         *               be the player, not the command block.
         * @param command Command which was executed.
         * @param alias The alias used.
         * @param args The arguments passed to the command, including final partial argument to be completed and command
         *             label.
         * @return A List of possible completions for the final argument, or null to default to the command executor
         */
        @Override
        public List<String> onTabComplete (@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args) {
            if (command.getName().equalsIgnoreCase("party")) {
                ArrayList<String> list = new ArrayList<>();
                if (args.length == 1) {
                    String[] Options = { "accept", "chat", "info", "invite", "kick", "leave", "list", "promote", "summon" };
                    if (!args[0].equals("")) { for (String o: Options) if (o.toLowerCase().startsWith(args[0].toLowerCase())) list.add(o); }
                    else list.addAll(Arrays.asList(Options));
                } else if (args[1].equalsIgnoreCase("invite")) Bukkit.getOnlinePlayers().forEach((p) -> list.add(p.getName()));
                else if (args[1].equalsIgnoreCase("kick") || args[1].equalsIgnoreCase("promote")) {
                    if (sender instanceof Player player) {
                        RpgPlayer rpgPlayer = PlayerManager.getPlayer(player.getUniqueId());
                        assert rpgPlayer != null;
                        Party party = rpgPlayer.getParty();
                        if (party != null) party.getPlayers().forEach((p) -> list.add(p.getName()));
                    }
                }
                return list;
            }
            return null;
        }
    };

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
     * Executes the given command, returning its success.
     * If false is returned, then the "usage" plugin.yml entry for this command (if defined) will be sent to the player.
     *
     * @param sender Source of the command.
     * @param command Command which was executed.
     * @param label Alias of the command which was used.
     * @param args Passed command arguments.
     * @return True if a valid command, otherwise false.
     */
    public boolean onCommand (@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            send(sender, "Sorry but something about that usage seemed off. Here is a list of options.");
            send(sender, "- accept: Accept an incoming invite.", false);
            send(sender, "- chat: Toggle party chat.", false);
            send(sender, "- info: View status information about party members.", false);
            send(sender, "- invite <name>: Invite the given online player.", false);
            send(sender, "- kick <name>: Remove the given player from your party.", false);
            send(sender, "- leave: Leave the party you are currently in.", false);
            send(sender, "- list: Lists members of your party including invitations.", false);
            send(sender, "- promote <name>: Designates a new party member as leader.", false);
            send(sender, "- summon: Summons party members to your location.", false);
        }
        else if (!(sender instanceof Player)) send(sender, "Please only run this command as a player.");
        else switch (args[0]) {
            case "accept" -> acceptSubcommand(sender);
            case "info" -> infoSubcommand(sender);
            case "invite" -> inviteSubcommand(sender, args);
            case "kick" -> kickSubcommand(sender, args);
            case "leave" -> leaveSubcommand(sender);
            case "list" -> listSubcommand(sender);
            case "summon" -> summonSubcommand(sender);
            case "chat" -> PartyManager.togglePartyChat((Player) sender);
            case "promote" -> promoteSubcommand(sender, args);
            default -> send(sender, ChatColor.RED + "Command not found.");
        }
        return true;
    }

    /**
     * Accepts the currently pending invitation to a party.
     *
     * @param sender The player who accepted the invitation.
     */
    private void acceptSubcommand (CommandSender sender) {
        RpgPlayer rpgPlayer = PlayerManager.getPlayer(sender.getName());
        assert rpgPlayer != null;
        Party pending = rpgPlayer.getPendingParty();
        Party party = rpgPlayer.getParty();
        if (party != null) send(sender, "You are already in a party!");
        else if (pending != null) {
            if (pending.addPlayer((Player) sender)) {
                pending.sendAll(prefix + "[" + rpgPlayer.getFormattedClass() + ChatColor.DARK_GRAY + "] "
                        + ChatColor.GRAY + rpgPlayer.getBukkitPlayer().getName() + " has joined the party!");
                send(sender, "You have joined the party!");
                rpgPlayer.joinParty(pending);
            } else send(sender, "Sorry, but that party is full.");
        } else send(sender,"You do not have any party invites.");
    }

    /**
     * Attempts to send information about the players current party members.
     *
     * @param sender The player that should be sent the information. Their party is also the one that should be sent.
     */
    private void infoSubcommand (CommandSender sender) {
        RpgPlayer rpgPlayer = PlayerManager.getPlayer(sender.getName());
        assert rpgPlayer != null;
        Party party = rpgPlayer.getParty();
        if (party == null) {
            send(sender, ChatColor.RED + "You are not in a party.");
            return;
        }
        send(sender, ChatColor.GRAY + "---- Party Status ----");
        for (Player p : party.getPlayers()) send(sender, ChatColor.GRAY + p.getName() + " -> " + rpgPlayer.getFormattedHealth());
    }

    /**
     * Invites the given player to the command sender's party.
     *
     * @param sender The player who is inviting another.
     * @param args The command arguments which should include a player to invite.
     */
    private void inviteSubcommand (CommandSender sender, String[] args) {
        RpgPlayer rpgPlayer = PlayerManager.getPlayer(sender.getName());
        assert rpgPlayer != null;
        Party party = rpgPlayer.getParty();
        if (args.length == 1) {
            send(sender, "You must list a player as an argument. /party invite <name>");
            return;
        } else if (party == null) {
            party = new Party((Player) sender);
            rpgPlayer.joinParty(party);
            PartyManager.addParty(party);
        }
        ArrayList<RpgPlayer> invitedPlayers = new ArrayList<>();
        for (int i = 1; i < args.length; i++) invitedPlayers.add(PlayerManager.getPlayer(args[i]));
        for (RpgPlayer p: invitedPlayers)  {
            if (p == null) {
                send(rpgPlayer.getBukkitPlayer(), ChatColor.RED + "Could not find one or more players. Are they online?");
                break;
            }
            send(p.getBukkitPlayer(), "You have been invited to join " + rpgPlayer.getName() + "'s party.");
            send(rpgPlayer.getBukkitPlayer(), "You have invited " + p.getName() + " to the party.");
            p.setPendingParty(party);
        }
    }

    /**
     * Kicks players listed in args if they're currently part of the party.
     *
     * @param sender The sender of the command. Should be a party leader for anything to happen.
     * @param args The arguments of the command.
     */
    private void kickSubcommand (CommandSender sender, String[] args) {
        RpgPlayer rpgPlayer = PlayerManager.getPlayer(sender.getName());
        assert rpgPlayer != null;
        Party party = rpgPlayer.getParty();
        if (args.length == 1) {
            send(sender, "You must list a player as an argument. /party kick <name>");
            return;
        } else if (party == null) {
            send(sender, "You cannot kick someone if you're not in a party.");
            return;
        } else if (!party.getLeader().equals(sender)) {
            send(sender, "You cannot kick someone if you're not the party leader.");
            return;
        }
        ArrayList<RpgPlayer> kickedPlayers = new ArrayList<>();
        for (int i = 1; i < args.length; i++) kickedPlayers.add(PlayerManager.getPlayer(args[i]));
        for (RpgPlayer p : kickedPlayers) {
            party.sendAll(prefix + p.getName() + " has been kicked from the party.");
            p.leaveParty();
            party.removePlayer(p.getBukkitPlayer());
        }
    }

    /**
     * The player who executed the command attempts the leave any parties they are a part of.
     *
     * @param sender The player who executed the command.
     */
    private void leaveSubcommand (CommandSender sender) {
        RpgPlayer rpgPlayer = PlayerManager.getPlayer(sender.getName());
        assert rpgPlayer != null;
        Party party = rpgPlayer.getParty();
        if (party == null) {
            send(sender, "You are not in a party and thus cannot leave the party.");
            return;
        }
        rpgPlayer.leaveParty();
        party.removePlayer((Player) sender);
        send(sender, "You have left the party.");
        party.sendAll(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "RPG" + ChatColor.DARK_GRAY + "] ["
                + rpgPlayer.getFormattedClass() + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY
                + rpgPlayer.getBukkitPlayer().getName() + " has left the party.");
    }

    /**
     * Sends the command sender a list of all players currently in the party.
     *
     * @param sender The sender of the command.
     */
    private void listSubcommand (CommandSender sender) {
        RpgPlayer rpgPlayer = PlayerManager.getPlayer(sender.getName());
        assert rpgPlayer != null;
        Party party = rpgPlayer.getParty();
        if (party == null)
            send(sender, "You do not belong to a party. A party can be started by inviting another player or accepting an invite from someone else.");
        else {
            send(sender, "This is your current party roster:");
            Collection<RpgPlayer> players = party.getRpgPlayers();
            int i = 1;
            for (RpgPlayer p: players) {
                send(sender, ChatColor.GRAY + "" + i + ". " + ChatColor.DARK_GRAY + "[" + p.getFormattedClass() + ChatColor.DARK_GRAY + "] "
                        + p.getPlayerRarity() + p.getName(), false);
                i++;
            }
        }
    }

    /**
     * Will summon all party members to the command sender so long as they are the party leader.
     *
     * @param sender The player who executed the command.
     */
    private void summonSubcommand (CommandSender sender) {
        RpgPlayer rpgPlayer = PlayerManager.getPlayer(sender.getName());
        assert rpgPlayer != null;
        Party party = rpgPlayer.getParty();
        if (party == null) send(sender, "You do not belong to a party and thus cannot summon your party.");
        else if (!party.getLeader().equals(sender)) send(sender, "You are not the party leader and cannot summon everyone.");
        else {
            party.getPlayers().forEach((p) -> {
                if (!p.equals(party.getLeader())) {
                    p.sendMessage(prefix + "You are being summoned to " + sender.getName() + ". 3s");
                    Bukkit.getScheduler().runTaskLater(plugin, () -> p.teleport(((Player) sender).getLocation()), 3 * 20);
                }
                else p.sendMessage(prefix + "You are summoning your party. They will be here in 3s");
            });
        }
    }

    /**
     * Attempts to promote a player passed through arguments as leader.
     *
     * @param sender The sender of the command.
     * @param args The arguments given to the command.
     */
    private void promoteSubcommand (CommandSender sender, String[] args) {
        RpgPlayer rpgPlayer = PlayerManager.getPlayer(sender.getName());
        assert rpgPlayer != null;
        Party party = rpgPlayer.getParty();
        if (party == null) send(sender, ChatColor.RED + "You do not belong to a party and thus cannot promote anyone.");
        else if (!party.getLeader().equals(sender)) send(sender, ChatColor.RED + "You are not the party leader and thus cannot designate a new leader.");
        else if (args.length < 2) send(sender, ChatColor.RED + "You must select someone to promote.");
        else {
            Player p = Bukkit.getPlayer(args[1]);
            if (p == null) send(sender, ChatColor.RED + "Player not found!");
            else if (!party.hasMember(p)) send(sender, ChatColor.RED + "That player is not currently part of the party!");
            else {
                party.promote(p);
                party.sendAll(prefix + ChatColor.GREEN + args[1] + " has been promoted to leader!");
            }
        }
    }
}
