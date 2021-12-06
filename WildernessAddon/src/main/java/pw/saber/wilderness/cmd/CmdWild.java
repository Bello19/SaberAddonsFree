package pw.saber.wilderness.cmd;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.Aliases;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.Logger;
import com.massivecraft.factions.util.WarmUpUtil;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class CmdWild extends FCommand {

    private final List<String> worldsAllowed;
    private final int minRange;
    private final int maxRange;
    private final List<Material> disabledBlocks;
    private final boolean playSound;
    private final Sound soundEffect;

    public CmdWild() {
        super();
        this.aliases.addAll(Aliases.wild);
        worldsAllowed = FactionsPlugin.getInstance().getConfig().getStringList("Wild.allowedWorlds");

        minRange = FactionsPlugin.getInstance().getConfig().getInt("Wild.minRange");
        maxRange = FactionsPlugin.getInstance().getConfig().getInt("Wild.maxRange");
        disabledBlocks = new ArrayList<>();
        for (String materialName : FactionsPlugin.getInstance().getConfig().getStringList("Wild.disabled-blocks")) {
            if (XMaterial.matchXMaterial(materialName).get().parseMaterial() == null) continue;
            Logger.print("[Factions (Wild)] Adding " + materialName + " to disabled-blocks!", Logger.PrefixType.DEFAULT);
            disabledBlocks.add(XMaterial.matchXMaterial(materialName).get().parseMaterial());
        }
        playSound = FactionsPlugin.getInstance().getConfig().getBoolean("Wild.playSoundOnTeleport");
        soundEffect = XSound.matchXSound(FactionsPlugin.getInstance().getConfig().getString("Wild.SoundType")).get().parseSound();
        this.requirements = new CommandRequirements.Builder(Permission.WILD)
                .playerOnly()
                .build();
    }


    @Override
    public void perform(CommandContext context) {
        FactionsPlugin plugin = FactionsPlugin.getInstance();

        FPlayer fPlayer = context.fPlayer;
        World world = fPlayer.getPlayer().getWorld();

        if(plugin.getConfig().getBoolean("Wild.requirePlayerFaction") && !fPlayer.hasFaction()) {
            context.msg(TL.GENERIC_MEMBERONLY);
            return;
        }

        if(!this.worldsAllowed.contains(world.getName())) {
            context.msg(TL.COMMAND_WILD_WORLD_NOT_ALLOWED);
            return;
        }

        Player player = context.player;

        for (int counter = 0; counter < 25; ++counter) {
            Random r = new Random();
            int xRange = minRange + r.nextInt(maxRange - minRange);
            int zRange = minRange + r.nextInt(maxRange - minRange);
            int x = world.getWorldBorder().getCenter().getBlockX() - xRange;
            int z = world.getWorldBorder().getCenter().getBlockZ() - zRange;
            if (new Random().nextBoolean()) {
                x = Math.abs(x);
            }
            if (new Random().nextBoolean()) {
                z = Math.abs(z);
            }
            int y = world.getHighestBlockYAt(x, z);
            Block block = world.getBlockAt(x, y - 1, z);
            Material material = XMaterial.matchXMaterial(block.getType()).parseMaterial();
            Material aboveHead = XMaterial.matchXMaterial(block.getLocation().add(0.0, 1.0, 0.0).getBlock().getType()).parseMaterial();
            if (disabledBlocks.contains(material) || disabledBlocks.contains(aboveHead) || !Board.getInstance().getFactionAt(new FLocation(block.getLocation())).isWilderness()) continue;
            int finalX = x;
            int finalZ = z;
            if(!fPlayer.isAdminBypassing()) {
                context.doWarmUp(WarmUpUtil.Warmup.WILD, TL.WARMUPS_NOTIFY_TELEPORT, "Wilderness", () -> {
                    if (playSound) XSound.matchXSound(soundEffect).play(player.getLocation(), 1.0f, 0.67f);
                    context.player.teleport(new Location(world, (double) finalX + 0.5, y, (double) finalZ + 0.5));
                    context.msg(TL.COMMAND_WILD_SUCCESS);
                }, FactionsPlugin.getInstance().getConfig().getLong("warmups.f-wild", 5));
            } else {
                context.player.teleport(new Location(world, (double) finalX + 0.5, y, (double) finalZ + 0.5));
            }
            return;
        }
        context.msg(TL.COMMAND_WILD_FAILED);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_WILD_DESCRIPTION;
    }
}
