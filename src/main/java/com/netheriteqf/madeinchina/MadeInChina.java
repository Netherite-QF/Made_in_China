package com.netheriteqf.madeinchina;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class MadeInChina implements ModInitializer {
    public static final TagKey<Item> MICTag = TagKey.of(RegistryKeys.ITEM, new Identifier("made_in_china", "explosion_proof_item"));
    @Override
    public void onInitialize() {

    }
}
