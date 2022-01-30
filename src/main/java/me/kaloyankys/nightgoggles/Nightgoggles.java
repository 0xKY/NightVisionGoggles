package me.kaloyankys.nightgoggles;

import me.kaloyankys.nightgoggles.item.CustomArmourMaterials;
import me.kaloyankys.nightgoggles.item.NightGoggles;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Nightgoggles implements ModInitializer {
    public static final Item NIGHT_GOGGLES = new NightGoggles(CustomArmourMaterials.COPPER_ARMOUR_MATERIAL, EquipmentSlot.HEAD, new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1));

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("nightgoggles", "nightvision_goggles"), NIGHT_GOGGLES);
    }
}