package net.mobz.Entity;

import net.mobz.Inits.Configinit;
import net.mobz.Inits.Entityinit;
import net.mobz.Config.configz;
import net.mobz.Entity.Attack.*;
import net.mobz.Inits.Soundinit;
import net.minecraft.entity.passive.IronGolemEntity;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.GoToEntityTargetGoal;
import net.minecraft.entity.ai.goal.IronGolemLookGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class StoneGolem extends IronGolemEntity {

    public StoneGolem(EntityType<? extends IronGolemEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 20;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new GolemAttack(this, 1.0D, true));
        this.goalSelector.add(2, new GoToEntityTargetGoal(this, 0.9D, 32.0F));
        this.goalSelector.add(5, new IronGolemLookGoal(this));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.8D));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new FollowTargetGoal<>(this, PlayerEntity.class, true));
        this.initCustomGoals();
    }

    protected void initCustomGoals() {
        this.targetSelector.add(1, (new RevengeGoal(this, new Class[0])).setGroupRevenge(ZombieEntity.class));
        this.targetSelector.add(2, (new RevengeGoal(this, new Class[0])).setGroupRevenge(SkeletonEntity.class));
        this.targetSelector.add(3, (new RevengeGoal(this, new Class[0])).setGroupRevenge(SpiderEntity.class));
        this.targetSelector.add(4, (new RevengeGoal(this, new Class[0])).setGroupRevenge(CreeperEntity.class));
        this.targetSelector.add(5, (new RevengeGoal(this, new Class[0])).setGroupRevenge(SlimeEntity.class));
        this.targetSelector.add(6, (new RevengeGoal(this, new Class[0])).setGroupRevenge(DwarfEntity.class));
        this.targetSelector.add(7, (new RevengeGoal(this, new Class[0])).setGroupRevenge(FastEntity.class));
        this.targetSelector.add(8, (new RevengeGoal(this, new Class[0])).setGroupRevenge(TankEntity.class));
        this.targetSelector.add(9, (new RevengeGoal(this, new Class[0])).setGroupRevenge(skeli2.class));
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource_1) {
        return Soundinit.GOLEMHITEVENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return Soundinit.GOLEMDEATHEVENT;
    }

    @Override
    protected void playStepSound(BlockPos blockPos_1, BlockState blockState_1) {
        this.playSound(Soundinit.GOLEMWALKEVENT, 1.0F, 1.0F);
    }

    @Override
    public boolean canImmediatelyDespawn(double double_1) {
        return true;
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(EntityAttributes.MAX_HEALTH)
                .setBaseValue(Configinit.CONFIGZ.StoneGolemLife * Configinit.CONFIGZ.LifeMultiplicatorMob);
        this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE)
                .setBaseValue(Configinit.CONFIGZ.StoneGolemAttack * Configinit.CONFIGZ.DamageMultiplicatorMob);

    }

    public boolean canSpawn(WorldView view) {
        BlockPos blockunderentity = new BlockPos(this.getX(), this.getY() - 1, this.getZ());
        BlockPos posentity = new BlockPos(this.getX(), this.getY(), this.getZ());
        return view.intersectsEntities(this) && this.world.isDay()
                && this.world.getLocalDifficulty(posentity).getGlobalDifficulty() != Difficulty.PEACEFUL
                && this.world.isDay() && this.world.getBlockState(posentity).getBlock().canMobSpawnInside()
                && this.world.getBlockState(blockunderentity).getBlock().allowsSpawning(
                        world.getBlockState(blockunderentity), view, blockunderentity, Entityinit.STONEGOLEM)
                && AutoConfig.getConfigHolder(configz.class).getConfig().StoneGolemSpawn;

    }
}