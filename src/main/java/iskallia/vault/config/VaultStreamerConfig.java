package iskallia.vault.config;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Arrays;

public class VaultStreamerConfig extends Config{
    
    @Expose public ArrayList<Integer> GIFT_SUB_BRACKETS = new ArrayList<>();
    @Expose public Boolean GIFT_SUBS_GIVE_BITS = false;
    @Expose public Integer DONOR_MEGA_HEAD = 100;
    @Expose public ArrayList<Integer> DONOR_TRADER_BRACKETS = new ArrayList<>();
    @Expose public Boolean BITS_GIVE_TRADER = false;
    @Expose public ArrayList<Integer> BIT_BRACKETS = new ArrayList<>();
    @Expose public Integer BIT_MEGA_HEAD = 10000;
    @Expose public Boolean SUBS_GIVE_TRADER = false;
    @Expose public Boolean SUB_BRACKET_AS_RARITY = false;
    @Expose public Boolean SUBS_GIVE_BITS = false;

    public String getName() {
        return "vault_streamer_config";
    }

    public void reset(){
        this.GIFT_SUB_BRACKETS = new ArrayList<>(Arrays.asList(5, 10, 20, 50));
        this.GIFT_SUBS_GIVE_BITS = false;
        this.DONOR_MEGA_HEAD = 100;
        this.DONOR_TRADER_BRACKETS = new ArrayList<>(Arrays.asList(25, -1, -1, -1));
        this.BITS_GIVE_TRADER = false;
        this.BIT_BRACKETS = new ArrayList<>(Arrays.asList(2500, -1, -1, -1));
        this.BIT_MEGA_HEAD = 10000;
        this.SUBS_GIVE_TRADER = false;
        this.SUB_BRACKET_AS_RARITY = false;
        this.SUBS_GIVE_BITS = false;
    }
}
