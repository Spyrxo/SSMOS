package ssm.attributes.doublejumps;

import ssm.attributes.Attribute;
import ssm.events.SmashDamageEvent;
import ssm.managers.KitManager;
import ssm.kits.Kit;
import ssm.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class DoubleJump extends Attribute {

    protected int maxDoubleJumps;
    protected double height;
    protected double power;
    protected Sound doubleJumpSound;
    protected long recharge_air_ticks = 0;
    protected boolean needs_xp = false;
    protected int remainingDoubleJumps = 0;

    public DoubleJump(double power, double height, int maxDoubleJumps, Sound doubleJumpSound) {
        super();
        this.power = power;
        this.height = height;
        this.maxDoubleJumps = maxDoubleJumps;
        this.doubleJumpSound = doubleJumpSound;
        task = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                if(owner == null) {
                    return;
                }
                Kit kit = KitManager.getPlayerKit(owner);
                if(kit != null && !kit.isActive()) {
                    owner.setAllowFlight(false);
                    owner.setFlying(false);
                    return;
                }
                if (groundCheck()) {
                    resetDoubleJumps();
                }
            }
        }, 0L, 0L);
    }

    /**
     * Ensure the player is able to double jump and pass jump method.
     */
    @EventHandler
    public void playerFlightEvent(PlayerToggleFlightEvent e) {
        if(owner == null) {
            return;
        }
        Player player = e.getPlayer();
        if (!player.equals(owner)) {
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if(!check()) {
            e.setCancelled(true);
            player.setFlying(false);
            player.setAllowFlight(false);
            return;
        }

        e.setCancelled(true);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setFallDistance(0);

        if (remainingDoubleJumps <= 0) {
            Bukkit.broadcastMessage("Remaining jumps were 0 even though you toggled flight... what?");
        }

        if (remainingDoubleJumps > 0 && check()) {
            remainingDoubleJumps--;

            playDoubleJumpSound();

            checkAndActivate();

            if (remainingDoubleJumps <= 0) {
                player.setAllowFlight(false);
            } else {
                BukkitRunnable runnable = new BukkitRunnable() {
                    private int ticks_passed = 0;

                    @Override
                    public void run() {
                        if(owner == null) {
                            return;
                        }
                        if(owner.getAllowFlight()) {
                            cancel();
                            return;
                        }
                        if(ticks_passed >= recharge_air_ticks) {
                            if(needs_xp && owner.getExp() <= 0) {
                                cancel();
                                return;
                            }
                            player.setAllowFlight(true);
                            cancel();
                        }
                        ticks_passed++;
                    }
                };
                runnable.runTaskTimer(plugin, 0L, 0L);
            }
        }
    }

    public void playDoubleJumpSound() {
        owner.getWorld().playSound(owner.getLocation(), doubleJumpSound, 1f, 1f);
    }

    public boolean groundCheck() {
        return Utils.entityIsOnGround(owner);
    }

    public void resetDoubleJumps() {
        remainingDoubleJumps = maxDoubleJumps;
        owner.setAllowFlight(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void smashDamageEvent(SmashDamageEvent e) {
        if (e.getDamageCause() == EntityDamageEvent.DamageCause.FALL) {
            e.setCancelled(true);
        }
    }

    public void activate() {
        jump();
    }

    protected abstract void jump();

    @Override
    public void setOwner(Player owner) {
        if(owner == null) {
            this.owner.setFlying(false);
            this.owner.setAllowFlight(false);
        }
        super.setOwner(owner);
        if(owner == null) {
            return;
        }
        owner.setFlying(false);
        owner.setAllowFlight(remainingDoubleJumps > 0);
    }

    public void setRemainingDoubleJumps(int remainingDoubleJumps) {
        this.remainingDoubleJumps = remainingDoubleJumps;
        if(owner == null) {
            return;
        }
        owner.setFlying(false);
        owner.setAllowFlight(remainingDoubleJumps > 0);
    }

    public int getRemainingDoubleJumps() {
        return remainingDoubleJumps;
    }

}
