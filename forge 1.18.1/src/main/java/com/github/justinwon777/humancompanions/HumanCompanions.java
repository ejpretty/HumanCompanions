package com.github.justinwon777.humancompanions;

import com.github.justinwon777.humancompanions.core.*;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

//import org.apache.logging.log4j;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.openjdk.nashorn.internal.objects.NativeString.valueOf;


@Mod(HumanCompanions.MOD_ID)
public class HumanCompanions {
//    static final Logger logger = Logger.getLogger(MyLogger.class.getName());
    public static final String MOD_ID = "humancompanions";
    public static MyLogger logger = MyLogger.getInstance();

    public Level level;
    public String levelId;




    public HumanCompanions() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        EntityInit.ENTITIES.register(eventBus);
        StructureInit.DEFERRED_REGISTRY_STRUCTURE.register(eventBus);
        PacketHandler.register();
        Config.register();
        eventBus.addListener(this::setup);
//        this.levelId = p_78276_;
        System.out.println("level id: " + getLevelId());
//        logger.severe(valueOf(getLevelId()) + " participant/world ID");
        logger.severe("hope it works");


        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);
    }

    public void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            StructureInit.setupStructures();
            ConfiguredStructures.registerConfiguredStructures();
        });
    }

    public String getLevelId() {
        return this.levelId;
    }

    private static Method GETCODEC_METHOD;
    public void addDimensionalSpacing(final WorldEvent.Load event) {
        if(event.getWorld() instanceof ServerLevel serverLevel){
            ChunkGenerator chunkGenerator = serverLevel.getChunkSource().getGenerator();

            if (chunkGenerator instanceof FlatLevelSource && serverLevel.dimension().equals(Level.OVERWORLD)) {
                return;
            }

            StructureSettings worldStructureConfig = chunkGenerator.getSettings();

            HashMap<StructureFeature<?>, HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> StructureToMultiMap =
                    new HashMap<>();

            ImmutableSet<ResourceKey<Biome>> oakBiomes = ImmutableSet.<ResourceKey<Biome>>builder()
                    .add(Biomes.PLAINS)
                    .add(Biomes.SUNFLOWER_PLAINS)
                    .add(Biomes.MEADOW)
                    .add(Biomes.JUNGLE)
                    .add(Biomes.SPARSE_JUNGLE)
                    .add(Biomes.BAMBOO_JUNGLE)
                    .build();
            ImmutableSet<ResourceKey<Biome>> oakAndBirchBiomes = ImmutableSet.<ResourceKey<Biome>>builder()
                    .add(Biomes.FOREST)
                    .add(Biomes.FLOWER_FOREST)
                    .build();
            ImmutableSet<ResourceKey<Biome>> birchBiomes = ImmutableSet.<ResourceKey<Biome>>builder()
                    .add(Biomes.BIRCH_FOREST)
                    .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
                    .build();
            ImmutableSet<ResourceKey<Biome>> sandstoneBiomes = ImmutableSet.<ResourceKey<Biome>>builder()
                    .add(Biomes.DESERT)
                    .build();
            ImmutableSet<ResourceKey<Biome>> acaciaBiomes = ImmutableSet.<ResourceKey<Biome>>builder()
                    .add(Biomes.SAVANNA)
                    .add(Biomes.SAVANNA_PLATEAU)
                    .add(Biomes.WINDSWEPT_SAVANNA)
                    .build();
            ImmutableSet<ResourceKey<Biome>> spruceBiomes = ImmutableSet.<ResourceKey<Biome>>builder()
                    .add(Biomes.SNOWY_PLAINS)
                    .add(Biomes.SNOWY_TAIGA)
                    .add(Biomes.GROVE)
                    .add(Biomes.WINDSWEPT_FOREST)
                    .add(Biomes.TAIGA)
                    .add(Biomes.OLD_GROWTH_PINE_TAIGA)
                    .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
                    .build();
            ImmutableSet<ResourceKey<Biome>> darkOakBiomes = ImmutableSet.<ResourceKey<Biome>>builder()
                    .add(Biomes.DARK_FOREST)
                    .build();
            ImmutableSet<ResourceKey<Biome>> terracottaBiomes = ImmutableSet.<ResourceKey<Biome>>builder()
                    .add(Biomes.BADLANDS)
                    .add(Biomes.ERODED_BADLANDS)
                    .add(Biomes.WOODED_BADLANDS)
                    .build();

            oakBiomes.forEach(biomeKey -> associateBiomeToConfiguredStructure(StructureToMultiMap,
                    ConfiguredStructures.Configured_Oak_House, biomeKey));
            oakAndBirchBiomes.forEach(biomeKey -> associateBiomeToConfiguredStructure(StructureToMultiMap,
                    ConfiguredStructures.Configured_Oak_Birch_House, biomeKey));
            birchBiomes.forEach(biomeKey -> associateBiomeToConfiguredStructure(StructureToMultiMap,
                    ConfiguredStructures.Configured_Birch_House, biomeKey));
            acaciaBiomes.forEach(biomeKey -> associateBiomeToConfiguredStructure(StructureToMultiMap,
                    ConfiguredStructures.Configured_Acacia_House, biomeKey));
            spruceBiomes.forEach(biomeKey -> associateBiomeToConfiguredStructure(StructureToMultiMap,
                    ConfiguredStructures.Configured_Spruce_House, biomeKey));
            sandstoneBiomes.forEach(biomeKey -> associateBiomeToConfiguredStructure(StructureToMultiMap,
                    ConfiguredStructures.Configured_Sandstone_House, biomeKey));
            darkOakBiomes.forEach(biomeKey -> associateBiomeToConfiguredStructure(StructureToMultiMap,
                    ConfiguredStructures.Configured_DarkOak_House, biomeKey));
            terracottaBiomes.forEach(biomeKey -> associateBiomeToConfiguredStructure(StructureToMultiMap,
                    ConfiguredStructures.Configured_Terracotta_House, biomeKey));

            ImmutableMap.Builder<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> tempStructureToMultiMap = ImmutableMap.builder();
            worldStructureConfig.configuredStructures.entrySet().stream().filter(entry -> !StructureToMultiMap.containsKey(entry.getKey())).forEach(tempStructureToMultiMap::put);

            StructureToMultiMap.forEach((key, value) -> tempStructureToMultiMap.put(key,
                    ImmutableMultimap.copyOf(value)));

            worldStructureConfig.configuredStructures = tempStructureToMultiMap.build();

            try {
                if(GETCODEC_METHOD == null) GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "codec");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(chunkGenerator));
                if(cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            }
            catch(Exception e){
                logger.severe("Was unable to check if " + serverLevel.dimension().location() + " is using " +
                        "Terraforged's ChunkGenerator.");
            }

            Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(worldStructureConfig.structureConfig());
            tempMap.putIfAbsent(StructureInit.COMPANION_HOUSE.get(),
                    StructureSettings.DEFAULTS.get(StructureInit.COMPANION_HOUSE.get()));
            worldStructureConfig.structureConfig = tempMap;
        }
    }

    /**
     * Helper method that handles setting up the map to multimap relationship to help prevent issues.
     */
    private static void associateBiomeToConfiguredStructure(Map<StructureFeature<?>,
            HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> StructureToMultiMap,
                                                            ConfiguredStructureFeature<?, ?> configuredStructureFeature, ResourceKey<Biome> biomeRegistryKey) {
        StructureToMultiMap.putIfAbsent(configuredStructureFeature.feature, HashMultimap.create());
        HashMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> configuredStructureToBiomeMultiMap =
                StructureToMultiMap.get(configuredStructureFeature.feature);
        if(configuredStructureToBiomeMultiMap.containsValue(biomeRegistryKey)) {
            logger.severe("""
                    Detected 2 ConfiguredStructureFeatures that share the same base StructureFeature trying to be added to same biome. One will be prevented from spawning.
                    This issue happens with vanilla too and is why a Snowy Village and Plains Village cannot spawn in the same biome because they both use the Village base structure.
                    The two conflicting ConfiguredStructures are: {}, {}
                    The biome that is attempting to be shared: {}
                """);
            logger.severe(String.valueOf(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureFeature)));
            logger.severe(String.valueOf(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(configuredStructureToBiomeMultiMap.entries().stream().filter(e -> e.getValue() == biomeRegistryKey).findFirst().get().getKey())));
            logger.severe(String.valueOf(biomeRegistryKey));
//            );
        }
        else{
            configuredStructureToBiomeMultiMap.put(configuredStructureFeature, biomeRegistryKey);
        }
    }
}
