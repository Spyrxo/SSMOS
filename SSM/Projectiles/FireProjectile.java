package SSM.Projectiles;

import SSM.Events.SmashDamageEvent;
import SSM.Utilities.ServerMessageType;
import SSM.Utilities.Utils;
import SSM.Utilities.VelocityUtil;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FireProjectile extends SmashProjectile {

    protected Entity burned_entity = null;
    protected int burn_ticks = 10;

    public FireProjectile(Player firer, String name) {
        super(firer, name);
        this.damage = 0.25;
        this.hitbox_size = 0.3;
        this.knockback_mult = 0;
        this.expiration_ticks = 14;
        this.blockDetection = false;
        this.idleDetection = false;
    }

    @Override
    public void launchProjectile() {
        super.launchProjectile();
        firer.getWorld().playSound(firer.getEyeLocation(), Sound.GHAST_FIREBALL, 0.1f, 1f);
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if(!(entity instanceof LivingEntity)) {
            return false;
        }
        LivingEntity livingEntity = (LivingEntity) entity;
        return !livingEntity.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE);
    }

    @Override
    protected Entity createProjectileEntity() {
        ItemStack fire = new ItemStack(Material.BLAZE_POWDER);
        return firer.getWorld().dropItem(firer.getEyeLocation(), fire);
    }

    @Override
    protected void doVelocity() {
        projectile.setVelocity(firer.getEyeLocation().getDirection().multiply(1.8));
    }

    @Override
    protected void doEffect() {
        return;
    }

    @Override
    protected boolean onExpire() {
        return true;
    }

    @Override
    protected boolean onHitLivingEntity(LivingEntity hit) {
        SmashDamageEvent smashDamageEvent = new SmashDamageEvent(hit, firer, damage);
        smashDamageEvent.multiplyKnockback(knockback_mult);
        smashDamageEvent.setIgnoreDamageDelay(true);
        smashDamageEvent.setReason(name);
        smashDamageEvent.callEvent();
        int fire_ticks = Math.min(160, Math.max(0, hit.getFireTicks()) + burn_ticks);
        hit.setFireTicks(fire_ticks);
        burned_entity = hit;
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(burned_entity.getFireTicks() <= 0) {
                    burned_entity = null;
                    cancel();
                }
            }
        };
        runnable.runTaskTimer(plugin, 0L ,5L);
        destroy();
        return false;
    }

    @Override
    protected boolean onHitBlock(Block hit) {
        return false;
    }

    @Override
    protected boolean onIdle() {
        return false;
    }

    @EventHandler
    public void armorPiercingFire(SmashDamageEvent e) {
        if(e.getDamager() != null && e.getDamager().equals(firer)) {
            return;
        }
        if(e.getDamageCause() != EntityDamageEvent.DamageCause.FIRE_TICK) {
            return;
        }
        if(burned_entity == null || !burned_entity.equals(e.getDamagee())) {
            return;
        }
        e.setDamager(firer);
        e.setDamagerName(firer.getName());
        e.setIgnoreArmor(true);
        e.multiplyKnockback(0);
    }

}
