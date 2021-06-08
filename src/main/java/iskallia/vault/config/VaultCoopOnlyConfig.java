package iskallia.vault.config;

import com.google.gson.annotations.Expose;

public class VaultCoopOnlyConfig extends Config{
    
    @Expose public Boolean IS_COOP_ONLY;
    @Expose public Boolean VAULT_EXP_EQUAL;
    @Expose public Boolean EXTRA_BOSS_CRATES;
    @Expose public Boolean RAIDER_FRIENDLYFIRE;
    
    public String getName() {
        return "vault_coop_only";
    }
    public String getXPShare() {
        return "vault_exp_equal";
    }
    public String getExtraCrate() {
        return "vault_extra_boss_crates";
    }

    public void reset(){
        this.IS_COOP_ONLY=false;
        this.VAULT_EXP_EQUAL=false;
        this.EXTRA_BOSS_CRATES=false;
        this.RAIDER_FRIENDLYFIRE=false;
    }
    public void resetXPShare(){
        this.VAULT_EXP_EQUAL=false;
    }
    public void resetExtraCrate(){
        this.EXTRA_BOSS_CRATES=false;
    }
}
