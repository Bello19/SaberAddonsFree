package pw.saber.shop.cmds;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.util.Cooldown;
import com.massivecraft.factions.zcore.util.TL;
import pw.saber.shop.ui.ShopGUIFrame;

public class CmdShop extends FCommand {

    /**
     * @author Driftay
     */

    public CmdShopAdd cmdShopAdd = new CmdShopAdd();
    public CmdShopRemove cmdShopRemove = new CmdShopRemove();

    public CmdShop() {
        super();
        this.aliases.add("shop");
        this.requirements = new CommandRequirements.Builder(Permission.SHOP)
                .playerOnly()
                .withRole(Role.MODERATOR)
                .build();

        this.addSubCommand(cmdShopAdd);
        this.addSubCommand(cmdShopRemove);
    }


    @Override
    public void perform(CommandContext context) {
        if (context.fPlayer.isAdminBypassing() || context.player.isOp()) {
            context.commandChain.add(this);
            FactionsPlugin.getInstance().cmdAutoHelp.execute(context);
        }

        if (context.fPlayer.getFaction().isNormal()) {
            if (!Cooldown.isOnCooldown(context.fPlayer.getPlayer(), "factionShop")) {
                new ShopGUIFrame().buildGUI(context.fPlayer);
            }
        } else {
            context.fPlayer.msg(TL.COMMAND_SHOP_NO_FACTION);
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SHOP_DESCRIPTION;
    }
}