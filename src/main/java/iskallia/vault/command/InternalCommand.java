package iskallia.vault.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import iskallia.vault.config.VaultFightersConfig;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.CrystalData;
import iskallia.vault.item.ItemGiftBomb;
import iskallia.vault.item.ItemGiftBomb.Variant;
import iskallia.vault.item.ItemTraderCore;
import iskallia.vault.item.ItemTraderCore.CoreType;
import iskallia.vault.item.ItemVaultCrystal;
import iskallia.vault.util.EntityHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.item.ItemStack;


public class InternalCommand extends Command {

    @Override
    public String getName() {
        return "internal";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                Commands.literal("received_sub")
                        .then(Commands.argument("actor", StringArgumentType.word())
                                .then(Commands.argument("tier", IntegerArgumentType.integer(0,3))
                                        .executes(InternalCommand::receivedSub)))
        );
        builder.then(
                Commands.literal("received_sub_gift")
                        .then(Commands.argument("actor", StringArgumentType.word())
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .then(Commands.argument("tier", IntegerArgumentType.integer(0,3))
                                                .executes(InternalCommand::receivedSubGift))))
        );
        builder.then(
                Commands.literal("received_donation")
                        .then(Commands.argument("actor", StringArgumentType.word())
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(InternalCommand::receivedDonation)))
        );
        builder.then(
                Commands.literal("received_bit_donation")
                        .then(Commands.argument("actor", StringArgumentType.word())
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(InternalCommand::receivedBitDonation)))
        );
        builder.then(
            Commands.literal("received_sub")
                    .then(Commands.argument("actor", StringArgumentType.word())
                    .executes(context -> receivedSub(context)))
        );

        builder.then(
                Commands.literal("raffle")
                        .then(Commands.argument("actor", StringArgumentType.word())
                                .executes(InternalCommand::raffle))
        );
    }

    private static int receivedSubGift(CommandContext<CommandSource> context) throws CommandSyntaxException {
        String actor = StringArgumentType.getString(context, "actor");
        int amount = IntegerArgumentType.getInteger(context,"amount");
        int tier = IntegerArgumentType.getInteger(context,"tier");
        int variant = 0;
        if(amount >= ModConfigs.VAULT_STREAMER_CONFIG.GIFT_SUB_BRACKETS.get(0))
        {
            if(amount >= ModConfigs.VAULT_STREAMER_CONFIG.GIFT_SUB_BRACKETS.get(1) && amount < ModConfigs.VAULT_STREAMER_CONFIG.GIFT_SUB_BRACKETS.get(2))
                variant = 1;
            else if(amount >= ModConfigs.VAULT_STREAMER_CONFIG.GIFT_SUB_BRACKETS.get(2) && amount < ModConfigs.VAULT_STREAMER_CONFIG.GIFT_SUB_BRACKETS.get(3))
                variant = 2;
            else if(amount >= ModConfigs.VAULT_STREAMER_CONFIG.GIFT_SUB_BRACKETS.get(3))
                variant = 3;
            ItemStack item = ItemGiftBomb.forGift(Variant.values()[variant], actor, amount);
            EntityHelper.giveItem(context.getSource().asPlayer(), item);
        }
        if(ModConfigs.VAULT_STREAMER_CONFIG.GIFT_SUBS_GIVE_BITS) GiveBitsCommand.dropBits(context.getSource().asPlayer(), amount*500);
        return 0;
    }

    private static int receivedSub(CommandContext<CommandSource> context) throws CommandSyntaxException {
        String actor = StringArgumentType.getString(context, "actor");
        int amount = 0;
        int tier = IntegerArgumentType.getInteger(context,"tier");
        int variant = 0;
        boolean GiveTrader = ModConfigs.VAULT_STREAMER_CONFIG.SUBS_GIVE_TRADER;
        boolean GiveBits = ModConfigs.VAULT_STREAMER_CONFIG.SUBS_GIVE_BITS;
        boolean Rarity = ModConfigs.VAULT_STREAMER_CONFIG.SUB_BRACKET_AS_RARITY;
        CoreType corerare = CoreType.COMMON;
        if(tier<2&&GiveBits) amount = 500;
        if(tier==2)
        {
            if(GiveBits) amount = 1000;
            if(Rarity) corerare = CoreType.RARE;
        }
        if(tier==3)
        {
            if(GiveBits) amount = 2500;
            if(Rarity) corerare = CoreType.OMEGA;
        }
        boolean isMegaHead = amount >= 2500;
        if(GiveTrader) {
            ItemStack item = ItemTraderCore.generate(actor, amount, isMegaHead, corerare);
            EntityHelper.giveItem(context.getSource().asPlayer(), item);
        }
        GiveBitsCommand.dropBits(context.getSource().asPlayer(), amount);
        return 0;
    }

    private static int receivedDonation(CommandContext<CommandSource> context) throws CommandSyntaxException {
        String actor = StringArgumentType.getString(context, "actor");
        int amount = IntegerArgumentType.getInteger(context,"amount");
        boolean isMegaHead = amount >= 100;
        CoreType corerare = CoreType.COMMON;
        if(amount >= ModConfigs.VAULT_STREAMER_CONFIG.DONOR_TRADER_BRACKETS.get(0)){
            if(ModConfigs.VAULT_STREAMER_CONFIG.DONOR_TRADER_BRACKETS.get(1)!=-1&&(amount>=ModConfigs.VAULT_STREAMER_CONFIG.DONOR_TRADER_BRACKETS.get(1)&&amount<=ModConfigs.VAULT_STREAMER_CONFIG.DONOR_TRADER_BRACKETS.get(2))) corerare = CoreType.RARE;
            if(ModConfigs.VAULT_STREAMER_CONFIG.DONOR_TRADER_BRACKETS.get(2)!=-1&&(amount>=ModConfigs.VAULT_STREAMER_CONFIG.DONOR_TRADER_BRACKETS.get(1)&&amount<=ModConfigs.VAULT_STREAMER_CONFIG.DONOR_TRADER_BRACKETS.get(3))) corerare = CoreType.EPIC;
            if(ModConfigs.VAULT_STREAMER_CONFIG.DONOR_TRADER_BRACKETS.get(3)!=-1&&amount>=ModConfigs.VAULT_STREAMER_CONFIG.DONOR_TRADER_BRACKETS.get(1)) corerare = CoreType.OMEGA;
            ItemStack item = ItemTraderCore.generate(actor,amount,isMegaHead,corerare);
            EntityHelper.giveItem(context.getSource().asPlayer(), item);
        }
        return 0;
    }

    private static int receivedBitDonation(CommandContext<CommandSource> context) throws CommandSyntaxException {
        String actor = StringArgumentType.getString(context, "actor");
        int amount = IntegerArgumentType.getInteger(context,"amount");
        boolean GiveTrader = ModConfigs.VAULT_STREAMER_CONFIG.BITS_GIVE_TRADER;
        boolean isMegaHead = amount >= ModConfigs.VAULT_STREAMER_CONFIG.BIT_MEGA_HEAD;
        CoreType corerare = CoreType.COMMON;
        if(ModConfigs.VAULT_STREAMER_CONFIG.BIT_BRACKETS.get(1)!=-1&&(amount >= ModConfigs.VAULT_STREAMER_CONFIG.BIT_BRACKETS.get(1) && amount < ModConfigs.VAULT_STREAMER_CONFIG.BIT_BRACKETS.get(2))) corerare = CoreType.RARE;
        if(ModConfigs.VAULT_STREAMER_CONFIG.BIT_BRACKETS.get(2)!=-1&&(amount >= ModConfigs.VAULT_STREAMER_CONFIG.BIT_BRACKETS.get(2) && amount < ModConfigs.VAULT_STREAMER_CONFIG.BIT_BRACKETS.get(3))) corerare = CoreType.EPIC;
        if(ModConfigs.VAULT_STREAMER_CONFIG.BIT_BRACKETS.get(3)!=-1&&(amount >= ModConfigs.VAULT_STREAMER_CONFIG.BIT_BRACKETS.get(3))) corerare = CoreType.OMEGA;
        if(amount >= ModConfigs.VAULT_STREAMER_CONFIG.BIT_BRACKETS.get(0) && GiveTrader){
            ItemStack item = ItemTraderCore.generate(actor, amount, isMegaHead, corerare);
            EntityHelper.giveItem(context.getSource().asPlayer(), item);
        }
        GiveBitsCommand.dropBits(context.getSource().asPlayer(), amount);
        return 0;
    }


    private static int receivedSub(CommandContext<CommandSource> context) throws CommandSyntaxException{
        String actor = StringArgumentType.getString(context, "actor");
        ModConfigs.VAULT_FIGHTERS.FIGHTER_LIST.add(actor);
        return 0;
    }
    

    private static int raffle(CommandContext<CommandSource> context) throws CommandSyntaxException {
        String actor = StringArgumentType.getString(context, "actor");
        ItemStack item = ItemVaultCrystal.getCrystalWithBoss(actor);
        ItemVaultCrystal.getData(item).addModifier("Raffle", CrystalData.Modifier.Operation.ADD,1.0F);
        EntityHelper.giveItem(context.getSource().asPlayer(), item);
        return 0;
    }

    @Override
    public boolean isDedicatedServerOnly() {
        return false;
    }

}