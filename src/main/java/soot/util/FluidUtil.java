package soot.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FluidUtil {
    public static final String BREW_MODIFIERS_TAG = "brew_modifiers";
    public static final HashMap<String,FluidModifier> MODIFIERS = new HashMap<>();

    public static void registerModifier(FluidModifier fluidModifier) {
        MODIFIERS.put(fluidModifier.name,fluidModifier);
    }

    public static void setDefaultValue(Fluid fluid, String name, float value)
    {
        FluidModifier modifier = MODIFIERS.get(name);
        if(modifier != null)
        {
            modifier.setDefault(fluid,value);
        }
    }

    public static void setDefaultValues(Fluid fluid, Map<String,Float> valuemap)
    {
        for (Map.Entry<String, Float> entry : valuemap.entrySet()) {
            setDefaultValue(fluid,entry.getKey(),entry.getValue());
        }
    }

    public static NBTTagCompound createModifiers(FluidStack stack)
    {
        if(stack.tag == null)
            stack.tag = new NBTTagCompound();
        NBTTagCompound brew_modifiers = stack.tag.getCompoundTag(BREW_MODIFIERS_TAG);
        stack.tag.setTag(BREW_MODIFIERS_TAG,brew_modifiers);
        return brew_modifiers;
    }

    public static NBTTagCompound getModifiers(FluidStack stack)
    {
        return (stack == null || stack.tag == null) ? new NBTTagCompound() : stack.tag.getCompoundTag(BREW_MODIFIERS_TAG);
    }

    public static void garbageCollect(FluidStack stack)
    {
        if(stack.tag == null)
            return;
        NBTTagCompound brew_modifiers = stack.tag.getCompoundTag(BREW_MODIFIERS_TAG);
        if(!brew_modifiers.getKeySet().stream().anyMatch(key -> brew_modifiers.getFloat(key) != 0))
            stack.tag.removeTag(BREW_MODIFIERS_TAG);
        if(stack.tag.getSize() == 0)
            stack.tag = null;
    }

    public static Random getRandom(World world, FluidStack stack, int seedoffset)
    {
        NBTTagCompound brew_modifiers = stack.tag != null ? stack.tag.getCompoundTag(BREW_MODIFIERS_TAG) : new NBTTagCompound();
        long seed = world.getSeed() ^ brew_modifiers.hashCode() + seedoffset;
        return new Random(seed);
    }

    public static float getModifier(FluidStack stack, String name)
    {
        return getModifier(getModifiers(stack),stack!=null ? stack.getFluid() : null,name);
    }

    public static float getModifier(NBTTagCompound compound, Fluid fluid, String name)
    {
        FluidModifier modifier = MODIFIERS.get(name);
        return modifier != null ? modifier.getOrDefault(compound, fluid) : 0;
    }
}