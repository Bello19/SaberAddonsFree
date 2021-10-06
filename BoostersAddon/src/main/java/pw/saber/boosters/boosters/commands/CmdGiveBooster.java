package pw.saber.boosters.boosters.commands;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.cmd.Aliases;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.NumberUtils;
import com.massivecraft.factions.zcore.util.TL;
import pw.saber.boosters.boosters.BoosterTypes;
import pw.saber.boosters.boosters.ItemCreation;


public class CmdGiveBooster extends FCommand {

    public CmdGiveBooster() {
        super();
        this.aliases.addAll(Aliases.giveBooster);
        this.requiredArgs.add("target");
        this.requiredArgs.add("type");
        this.requiredArgs.add("duration");
        this.requiredArgs.add("multiplier");

        this.requirements = new CommandRequirements.Builder(Permission.GIVEBOOSTER)
                .build();
    }


    @Override
    public void perform(CommandContext context) {
        FPlayer target = context.argAsBestFPlayerMatch(0);
        String boosterType = context.args.get(1);

        if (target == null) {
            context.msg(TL.PLAYER_NOT_FOUND);
            return;
        }

        if (!NumberUtils.isNumber(context.args.get(2))) {
            context.msg(TL.COMMAND_GIVEBOOSTER_INVALID_DURATION, context.args.get(2));
            return;
        }

        BoosterTypes boosterTypes;
        try {
            boosterTypes = BoosterTypes.valueOf(boosterType);
        } catch (Exception e) {
            context.msg(TL.COMMAND_GIVEBOOSTER_INVALID_BOOSTER, boosterType);
            return;
        }

        double multiplier;
        try {
            multiplier = Math.max(Double.parseDouble(context.args.get(3)), 1.1);
        } catch (Exception e) {
            context.msg(TL.COMMAND_GIVEBOOSTER_INVALID_MULTIPLIER, context.args.get(3));
            return;
        }

        int duration = Integer.parseInt(context.args.get(2));
        if (duration < 0) {
            duration = 1;
        }

        target.getPlayer().getInventory().addItem(ItemCreation.createBoosterItem(boosterTypes, duration, multiplier));
        target.getPlayer().updateInventory();
        context.msg(TL.COMMAND_GIVEBOOSTER_BOOSTER_GIVEN, target.getName());
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_GIVEBOOSTER_DESCRIPTION;
    }
}
