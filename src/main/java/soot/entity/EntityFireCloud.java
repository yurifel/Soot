package soot.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityFireCloud extends EntityCloud {
    public EntityFireCloud(World worldIn) {
        super(worldIn);
    }

    public EntityFireCloud(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public boolean applyEffectToEntity(EntityLivingBase entitylivingbase) {
        if(entitylivingbase.isImmuneToFire())
            return false;

        entitylivingbase.setFire(30);
        return true;
    }
}
