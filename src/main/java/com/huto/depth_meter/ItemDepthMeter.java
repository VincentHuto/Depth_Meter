/*
 * This class is distributed as part of the Depth Meter Mod.
 * Created By Vincent_Huto 4/17/2021
 */
package com.huto.depth_meter;

import java.util.List;

import com.ibm.icu.text.DecimalFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemDepthMeter extends Item {

	public static String TAG_MODE = "mode";

	public ItemDepthMeter(Properties prop) {
		super(prop.stacksTo(1));
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
		CompoundNBT tag = stack.getOrCreateTag();
		double height = entityIn.getY();
		if (height >= 200) {
			tag.putInt(TAG_MODE, 6);
		} else if (height < 200 && height >= 105) {
			tag.putInt(TAG_MODE, 5);
		} else if (height < 105 && height >= 55) {
			tag.putInt(TAG_MODE, 4);
		} else if (height < 55 && height >= 25) {
			tag.putInt(TAG_MODE, 3);
		} else if (height < 25 && height >= 8) {
			tag.putInt(TAG_MODE, 2);
		} else if (height < 8 && height >= 0) {
			tag.putInt(TAG_MODE, 1);
		} else {
			tag.putInt(TAG_MODE, 999);
		}

	}

	@Override
	public ITextComponent getName(ItemStack stack) {
		return new StringTextComponent(convertInitToLang(stack.getItem().getRegistryName().getPath()))
				.withStyle(TextFormatting.ITALIC).withStyle(TextFormatting.LIGHT_PURPLE);
	}

	public static String convertInitToLang(String text) {
		if (text == null || text.isEmpty()) {
			return text;
		}

		StringBuilder converted = new StringBuilder();
		boolean convertNext = true;
		text.replace("_trail", "");
		for (char ch : text.toCharArray()) {
			if (ch == '_') {
				ch = ' ';
				convertNext = true;
			} else if (convertNext) {
				ch = Character.toTitleCase(ch);
				convertNext = false;
			} else {
				ch = Character.toLowerCase(ch);
			}
			converted.append(ch);
		}

		return converted.toString();
	}

	DecimalFormat df = new DecimalFormat("#.##");

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		if (playerIn != null && worldIn != null) {
			if (worldIn.isClientSide) {
				double height = playerIn.getY();
				double seaLevel = worldIn.getSeaLevel();
				double diff = Math.abs(seaLevel - height);
				height = Double.valueOf(df.format(height));
				seaLevel = Double.valueOf(df.format(seaLevel));
				diff = Double.valueOf(df.format(diff));
				playerIn.displayClientMessage(new StringTextComponent("Sea Level =" + String.valueOf(seaLevel) + "m"),
						false);
				playerIn.displayClientMessage(
						new StringTextComponent("Absolute Height =" + String.valueOf(height) + "m"), false);
				if (height == seaLevel) {
					playerIn.displayClientMessage(new StringTextComponent("At Sea Level!"), false);
				} else if (height < seaLevel) {
					playerIn.displayClientMessage(new StringTextComponent(
							"Depth Below Sea Level =" + String.valueOf(seaLevel - height) + "m"), false);
				} else {
					playerIn.displayClientMessage(
							new StringTextComponent("Height Above Sea Level =" + String.valueOf(diff) + "m"), false);

				}
			}
		}

		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@SuppressWarnings("resource")
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		PlayerEntity player = Minecraft.getInstance().player;
		if (player != null && worldIn != null) {
			double height = player.getY();
			double seaLevel = worldIn.getSeaLevel();
			double diff = Math.abs(seaLevel - height);
			height = Double.valueOf(df.format(height));
			seaLevel = Double.valueOf(df.format(seaLevel));
			diff = Double.valueOf(df.format(diff));
			tooltip.add(new StringTextComponent("Sea Level =" + String.valueOf(seaLevel) + "m"));
			tooltip.add(new StringTextComponent("Absolute Height =" + String.valueOf(height) + "m"));
			if (height == seaLevel) {
				tooltip.add(new StringTextComponent("At Sea Level!"));
			} else if (height < seaLevel) {
				tooltip.add(
						new StringTextComponent("Depth Below Sea Level =" + String.valueOf(seaLevel - height) + "m"));
			} else {
				tooltip.add(new StringTextComponent("Height Above Sea Level =" + String.valueOf(diff) + "m"));

			}
		}

	}

}