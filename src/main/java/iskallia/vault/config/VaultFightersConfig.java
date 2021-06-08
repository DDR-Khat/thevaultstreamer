package iskallia.vault.config;

import java.util.Collections;
import java.util.List;
import com.google.gson.annotations.Expose;

public class VaultFightersConfig extends Config{
    @Expose public List<String> FIGHTER_LIST;
    
    @Override
	public String getName() {
		return "vault_fighters";
	}

	@Override
	protected void reset() {
		FIGHTER_LIST = Collections.emptyList();
	}
}
