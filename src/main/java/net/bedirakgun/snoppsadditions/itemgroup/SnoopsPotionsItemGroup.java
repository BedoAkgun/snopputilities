
package net.bedirakgun.snoppsadditions.itemgroup;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;

import net.bedirakgun.snoppsadditions.item.HealthLargeItem;
import net.bedirakgun.snoppsadditions.SnoppsAdditionsModElements;

@SnoppsAdditionsModElements.ModElement.Tag
public class SnoopsPotionsItemGroup extends SnoppsAdditionsModElements.ModElement {
	public SnoopsPotionsItemGroup(SnoppsAdditionsModElements instance) {
		super(instance, 23);
	}

	@Override
	public void initElements() {
		tab = new ItemGroup("tabsnoops_potions") {
			@OnlyIn(Dist.CLIENT)
			@Override
			public ItemStack createIcon() {
				return new ItemStack(HealthLargeItem.block);
			}

			@OnlyIn(Dist.CLIENT)
			public boolean hasSearchBar() {
				return false;
			}
		};
	}

	public static ItemGroup tab;
}
