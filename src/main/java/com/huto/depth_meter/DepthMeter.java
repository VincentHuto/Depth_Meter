/*
 * This class is distributed as part of the Depth Meter Mod.
 * Created By Vincent_Huto 4/17/2021
 */
package com.huto.depth_meter;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod("depth_meter")
@Mod.EventBusSubscriber(modid = DepthMeter.MOD_ID, bus = Bus.MOD)
public class DepthMeter {
	public static final String MOD_ID = "depth_meter";
	public static DepthMeter instance;
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
	public static final RegistryObject<Item> depth_meter = ITEMS.register("depth_meter",
			() -> new ItemDepthMeter(new Item.Properties().tab(ItemGroup.TAB_TOOLS)));

	public DepthMeter() {
		instance = this;
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ITEMS.register(modEventBus);

	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void itemPropOverrideClient(final FMLClientSetupEvent event) {
		ItemModelsProperties.register(depth_meter.get(), new ResourceLocation("mode"), new IItemPropertyGetter() {
			@Override
			public float call(ItemStack stack, ClientWorld world, LivingEntity ent) {
				if (stack.hasTag()) {
					return stack.getTag().getInt("mode");
				} else {
					return 0;

				}
			}
		});
	}
}
