package iskallia.vault.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;

import static iskallia.vault.Vault.raiders;


public class RaiderCommand extends Command {

    @Override
    public String getName() {
        return "raider";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) { 
        builder.executes(RaiderCommand::execute);
    }

    private static int execute(CommandContext<CommandSource> context) throws CommandSyntaxException {
        if(raiders==null) raiders = context.getSource().getServer().getScoreboard().createTeam("raiders");
        MinecraftServer server = context.getSource().getServer();
        ServerPlayerEntity player = context.getSource().asPlayer();
        if(player.getTeam()==raiders){
            server.getScoreboard().removePlayerFromTeam(player.getName().getString(),raiders);
            server.getPlayerList().func_232641_a_(new StringTextComponent(player.getName().getString()+" has left the raiding party"), ChatType.CHAT, player.getUniqueID());
        }
        else{
            server.getScoreboard().addPlayerToTeam(player.getName().getString(),raiders);
            server.getPlayerList().func_232641_a_(new StringTextComponent(player.getName().getString()+" has joined the raiding party!"), ChatType.CHAT, player.getUniqueID());
        }
        return 0;
    }

    @Override
    public boolean isDedicatedServerOnly() {
        return false;
    }

}