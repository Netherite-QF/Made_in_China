package com.netheriteqf.madeinchina.mixin;

import com.netheriteqf.madeinchina.MadeInChina;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements Ownable {
    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    @Shadow
    private int health;

    @Shadow
    @Final
    private static TrackedData<ItemStack> STACK;

    @Shadow
    public ItemStack getStack() {
        return (ItemStack)this.getDataTracker().get(STACK);
    }

    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (this.isInvulnerableTo(source)) {
            cir.setReturnValue(false);
        } else if (!this.getStack().isEmpty() && this.getStack().isIn(MadeInChina.MICTag) && source.isIn(DamageTypeTags.IS_EXPLOSION)) {
            cir.setReturnValue(false);
        } else if (!this.getStack().getItem().damage(source)) {
            cir.setReturnValue(false);
        } else if (this.getWorld().isClient) {
            cir.setReturnValue(true);
        } else {
            this.scheduleVelocityUpdate();
            this.health = (int)((float)this.health - amount);
            this.emitGameEvent(GameEvent.ENTITY_DAMAGE, source.getAttacker());
            if (this.health <= 0) {
                this.getStack().onItemEntityDestroyed(((ItemEntity)(Object)this));
                this.discard();
            }
            cir.setReturnValue(true);
        }
    }
}
