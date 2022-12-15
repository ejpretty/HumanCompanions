package com.github.justinwon777.humancompanions.sound;

import com.github.justinwon777.humancompanions.HumanCompanions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, HumanCompanions.MOD_ID);

    public static final RegistryObject<SoundEvent> DOWSING_ROD_FOUND_ORE =
            registerSoundEvent();

    private static RegistryObject<SoundEvent> registerSoundEvent() {
        return SOUND_EVENTS.register("dowsing_rod_found_ore", () -> new SoundEvent(new ResourceLocation(HumanCompanions.MOD_ID, "dowsing_rod_found_ore")));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
