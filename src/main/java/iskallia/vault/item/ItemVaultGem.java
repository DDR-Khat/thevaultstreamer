package iskallia.vault.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

public class ItemVaultGem extends Item {

    public ItemVaultGem(ItemGroup group, ResourceLocation id) {
        super(new Properties()
                .group(group)
                .maxStackSize(64));

        this.setRegistryName(id);
    }

}
