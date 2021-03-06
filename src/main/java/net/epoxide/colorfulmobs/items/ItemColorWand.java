package net.epoxide.colorfulmobs.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.epoxide.colorfulmobs.ColorfulMobs;
import net.epoxide.colorfulmobs.handler.ContentHandler;
import net.epoxide.colorfulmobs.handler.GuiHandler;

public class ItemColorWand extends Item {
    
    public ItemColorWand() {
        
        this.setCreativeTab(ColorfulMobs.tabColor);
        this.setUnlocalizedName("colorfulmobs.colorwand");
        this.setTextureName("colorfulmobs:colorwand");
        this.setMaxDamage(16);
    }
    
    @Override
    public boolean itemInteractionForEntity (ItemStack stack, EntityPlayer player, EntityLivingBase entity) {
        
        player.triggerAchievement(ContentHandler.achWand);
        GuiHandler.setEntity(entity);
        player.openGui(ColorfulMobs.instance, 0, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
        stack.damageItem(1, player);
        return true;
    }
    
    @Override
    public boolean getIsRepairable (ItemStack input, ItemStack repairItem) {
        
        return (repairItem.getItem() instanceof ItemRainbowDust);
    }
}