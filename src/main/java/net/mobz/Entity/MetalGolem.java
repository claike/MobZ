package net.mobz.Entity;

import net.mobz.Inits.Configinit;
import net.mobz.Inits.Iteminit;
import net.mobz.Config.configz;
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
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.TrackIronGolemTargetGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.goal.WanderAroundPointOfInterestGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class MetalGolem extends IronGolemEntity {

  public MetalGolem(EntityType<? extends IronGolemEntity> entityType, World world) {
    super(entityType, world);
    this.experiencePoints = 25;
  }

  @Override
  protected void initGoals() {
    this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0D, true));
    this.goalSelector.add(2, new GoToEntityTargetGoal(this, 0.9D, 36.0F));
    this.goalSelector.add(2, new WanderAroundPointOfInterestGoal(this, 0.8D));
    this.goalSelector.add(3, new MoveThroughVillageGoal(this, 0.9D, false, 10, () -> {
      return false;
    }));
    this.goalSelector.add(5, new IronGolemLookGoal(this));
    this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.65D));
    this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
    this.goalSelector.add(8, new LookAroundGoal(this));
    this.targetSelector.add(1, new TrackIronGolemTargetGoal(this));
    this.targetSelector.add(2, new RevengeGoal(this, new Class[0]));
    this.targetSelector.add(3, new FollowTargetGoal<>(this, MobEntity.class, 5, false, false, (livingEntity) -> {
      return livingEntity instanceof Monster && !(livingEntity instanceof CreeperEntity);
    }));
  }

  @Override
  public boolean damage(DamageSource source, float amount) {
    IronGolemEntity.Crack crack = this.getCrack();
    boolean bl = super.damage(source, amount);
    if (bl && this.getCrack() != crack) {
      this.playSound(Soundinit.MGOLEMBREAKEVENT, 1.0F, 1.0F);
    }

    return bl;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSource_1) {
    return Soundinit.MGOLEMHITEVENT;
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
    return false;
  }

  @Override
  public boolean cannotDespawn() {
    return true;
  }

  @Override
  protected void initAttributes() {
    super.initAttributes();
    this.getAttributeInstance(EntityAttributes.MAX_HEALTH)
        .setBaseValue(Configinit.CONFIGZ.MetalGolemLife * Configinit.CONFIGZ.LifeMultiplicatorMob);
    this.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.27D);
    this.getAttributeInstance(EntityAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1.5D);
    this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE)
        .setBaseValue(Configinit.CONFIGZ.MetalGolemAttack * Configinit.CONFIGZ.DamageMultiplicatorMob);
  }

  @Override
  public boolean canSpawn(WorldView view) {
    return AutoConfig.getConfigHolder(configz.class).getConfig().MetalGolemSpawn;
  }

  @Override
  protected boolean interactMob(PlayerEntity player, Hand hand) {
    ItemStack itemStack = player.getStackInHand(hand);
    Item item = itemStack.getItem();
    if (item != Iteminit.HARDENEDMETAL) {
      return false;
    } else {
      float f = this.getHealth();
      this.heal(40.0F);
      if (this.getHealth() == f) {
        return false;
      } else {
        float g = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
        this.playSound(SoundEvents.ENTITY_IRON_GOLEM_REPAIR, 1.0F, g);
        if (!player.abilities.creativeMode) {
          itemStack.decrement(1);
        }

        return true;
      }
    }
  }

}