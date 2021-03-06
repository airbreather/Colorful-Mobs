package net.epoxide.colorfulmobs.common;

import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import net.minecraftforge.common.IExtendedEntityProperties;

import net.epoxide.colorfulmobs.ColorfulMobs;
import net.epoxide.colorfulmobs.common.network.PacketColorSync;
import net.epoxide.colorfulmobs.handler.ConfigurationHandler;
import net.epoxide.colorfulmobs.lib.ColorObject;

public class ColorProperties implements IExtendedEntityProperties {
    
    public static final String PROP_NAME = "ColorProperties";
    
    public EntityLivingBase entity;
    private ColorObject colorObj;
    
    public ColorProperties(EntityLivingBase living) {
        
        entity = living;
        colorObj = new ColorObject();
    }
    
    @Override
    public void saveNBTData (NBTTagCompound compound) {
        
        NBTTagCompound entityData = new NBTTagCompound();
        entityData.setTag("color", this.colorObj.getTagFromColor());
        compound.setTag(PROP_NAME, entityData);
    }
    
    @Override
    public void loadNBTData (NBTTagCompound compound) {
        
        NBTTagCompound playerData = compound.getCompoundTag(PROP_NAME);
        this.colorObj = new ColorObject(playerData.getCompoundTag("color"));
    }
    
    @Override
    public void init (Entity entity, World world) {
    
    }
    
    /**
     * Synchronizes the EntityProperties data.
     */
    public void sync () {
        
        ColorfulMobs.network.sendToAll(new PacketColorSync(this));
    }
    
    /**
     * Retrieves the EntityProperties instance from an Entity.
     *
     * @param entity: The Entity to retrieve from.
     * @return ColorProperties: The EntityProperties instance for that entity.
     */
    public static ColorProperties getProperties (EntityLivingBase entity) {
        
        return (ColorProperties) entity.getExtendedProperties(PROP_NAME);
    }
    
    /**
     * Sets the EntityProperties instance for an entity.
     *
     * @param entity: The entity to set to.
     * @return EntityProperties: The new EntityProperties instance.
     */
    public static ColorProperties setProperties (EntityLivingBase entity) {
        
        entity.registerExtendedProperties(PROP_NAME, new ColorProperties(entity));
        return getProperties(entity);
    }
    
    /**
     * A check to determine whether or not an Entity has an instance of EntityProperties.
     *
     * @param entity: The Entity to check.
     * @return boolean: Whether or not the entity has an instance of EntityProperties.
     */
    public static boolean hasProperties (EntityLivingBase entity) {
        
        return getProperties(entity) != null;
    }
    
    /**
     * Retrieves a usable ColorObject instance from the properties instance.
     *
     * @return ColorObject: An object containing all of the color data. If the one from the
     *         properties is not usable, a default one is provided.
     */
    public ColorObject getColorObj () {
        
        return (this.colorObj == null) ? new ColorObject() : this.colorObj;
    }
    
    /**
     * Sets a ColorObject to the ColorProperties for an entity.
     *
     * @param newColor: The new color to set. If this value is null, a new ColorObject will be
     *            created.
     */
    public ColorProperties setColorObject (ColorObject newColor) {
        
        this.colorObj = (newColor == null) ? new ColorObject() : newColor;
        
        return this;
    }
    
    /**
     * Checks if the color for this entity has any abnormalities.
     *
     * @return boolean: True if any of the color values are less than one.
     */
    public boolean isDyed () {
        
        return (this.colorObj.getRed() < 1f || this.colorObj.getGreen() < 1f || this.colorObj.getBlue() < 1f || this.colorObj.getAlpha() < 1f);
    }
    
    /**
     * Checks to see if the mob is a valid target for being colored. If the target mob is a
     * player, it will only be valid if the config option for dying players is set to true. If
     * the entity is not a player, it will be valid, unless it is on the prohibited mobs config
     * list.
     *
     * @return boolean: True, but only if the mob is a valid target to be dyed.
     */
    public boolean isValidTarget () {
        
        if (this.entity instanceof EntityPlayer && ConfigurationHandler.dyePlayer)
            return true;
            
        if (ConfigurationHandler.limitMobs && !ConfigurationHandler.validMobs.contains(EntityList.getEntityString(entity)))
            return false;
            
        return true;
    }
}