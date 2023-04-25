package sugaku.rpg.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import sugaku.rpg.framework.mobs.MobManager;
import sugaku.rpg.mobs.teir1.eiryeras.EiryerasBoss;
import sugaku.rpg.mobs.teir1.krusk.KruskBoss;
import sugaku.rpg.mobs.teir1.krusk.KruskMinion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CommandSummonBoss extends AbstractCommand implements CommandExecutor {

    /**
     * Tab completer which fills in options for this command.
     */
    public TabCompleter autocomplete = new AutocompleteSummon();

    public CommandSummonBoss(String name) { super(name); }

    /**
     * Code to be executed on run.
     * @return Whether the command executed successfully or not
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length < 1 || args.length > 2) { send(sender, "Check your usage. Should be /summonRPG <mob>"); return true; }

        Player player = (Player) sender;

        int loop = 1;
        if (args.length >= 2) {
            try { loop = Integer.parseInt(args[1]); }
            catch (Exception e) { send(sender, "Could not find the number you would like to spawn. Assuming 1."); }
        }

        for (int i = 0; i < loop; i++) {
            if (args[0].equalsIgnoreCase("Eiryeras")) {
                send(player, ChatColor.GREEN + "Eiryeras," + ChatColor.GRAY + " honored hunter of the Agloytan area has been summoned.");
                MobManager.addMob(new EiryerasBoss(player.getLocation()));
            } else if (args[0].equalsIgnoreCase("Krusk")) {
                send(player, ChatColor.GREEN + "Krusk,"  + ChatColor.GRAY +  " one of the generals of the undead has been summoned.");
                MobManager.addMob(new KruskBoss(player.getLocation()));
            } else if (args[0].equalsIgnoreCase("KruskMinion")) {
                send(player, "One of " + ChatColor.GREEN +  "Krusk's" + ChatColor.GRAY + " minions has been summoned.");
                MobManager.addMob(new KruskMinion(player.getLocation(), (int) (new Random().nextDouble() * 8)));
            } else send(player, "Sorry but we could not find that mob.");
        }
        return true;
    }
}

class AutocompleteSummon implements TabCompleter {
    /**
     * Tab completion of the command described above.
     * @return List<String> of options
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("summonrpg")) {
            if (args.length == 1) {
                ArrayList<String> list = new ArrayList<>();
                //Keep this sorted. It saves an O(n log n) sort
                String[] Options = {"Eiryeras", "Krusk", "KruskMinion"};

                if (!args[0].equals("")) { for (String o: Options) if (o.toLowerCase().startsWith(args[0].toLowerCase())) list.add(o); }
                else list.addAll(Arrays.asList(Options));

                return list;
            }
        }
        return null;
    }
}
