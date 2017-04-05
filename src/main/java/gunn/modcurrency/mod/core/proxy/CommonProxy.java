package gunn.modcurrency.mod.core.proxy;

import gunn.modcurrency.mod.ModConfig;
import gunn.modcurrency.mod.ModCurrency;
import gunn.modcurrency.mod.block.ModBlocks;
import gunn.modcurrency.mod.core.handler.EventHandlerCommon;
import gunn.modcurrency.mod.core.handler.GuiHandler;
import gunn.modcurrency.mod.core.network.PacketHandler;
import net.minecraftforge.common.config.Configuration;
import gunn.modcurrency.mod.item.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.io.File;

/**
 * Distributed with the Currency-Mod for Minecraft.
 * Copyright (C) 2016  Brady Gunn
 *
 * File Created on 2016-10-28.
 */
public class CommonProxy {
    public static Configuration config;

    public void preInit(FMLPreInitializationEvent e){
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "GoodOlCurrency.cfg"));
        ModConfig.readConfig();

        ModItems.preInit();
        ModBlocks.preInit();

        NetworkRegistry.INSTANCE.registerGuiHandler(ModCurrency.instance, new GuiHandler());
        PacketHandler.registerMessages("modcurrency");
    }

    public void Init(FMLInitializationEvent e){
        ModBlocks.addRecipes();
        ModItems.addRecipes();
        MinecraftForge.EVENT_BUS.register(new EventHandlerCommon());
    }


    public void postInit(FMLPostInitializationEvent e){
    }
}
