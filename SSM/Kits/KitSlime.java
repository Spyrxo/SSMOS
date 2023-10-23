package SSM.Kits;

import SSM.Abilities.SlimeRocket;
import SSM.Abilities.SlimeSlam;
import SSM.Attributes.DoubleJumps.GenericDoubleJump;
import SSM.Attributes.ExpCharge;
import SSM.Attributes.Regeneration;
import SSM.GameManagers.DisguiseManager;
import SSM.GameManagers.Disguises.SlimeDisguise;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KitSlime extends Kit {

    public KitSlime() {
        super();
        this.damage = 6;
        this.armor = 4;
        this.regeneration = 0.35;
        this.knockback = 1.75;
        this.name = "Slime";
        this.menuItem = Material.SLIME_BALL;
    }

    @Override
    public void initializeKit() {
        setArmor(Material.CHAINMAIL_BOOTS, 0);
        setArmor(Material.CHAINMAIL_CHESTPLATE, 2);
        setArmor(Material.CHAINMAIL_HELMET, 3);

        setAbility(new SlimeRocket(), 0);
        setAbility(new SlimeSlam(), 1);

        addAttribute(new Regeneration(regeneration, 20));
        addAttribute(new ExpCharge(0.005f, 1, false));
        addAttribute(new GenericDoubleJump(1.2, 1.0, 1, Sound.GHAST_FIREBALL));

        DisguiseManager.addDisguise(owner, new SlimeDisguise(owner));
    }

    @Override
    public void setPreviewHotbar() {
        setItem(new ItemStack(Material.IRON_SWORD, 1), 0);
        setItem(new ItemStack(Material.IRON_AXE, 1), 1);
    }

    @Override
    public void setGameHotbar() {
        setItem(new ItemStack(Material.IRON_SWORD, 1), 0);
        setItem(new ItemStack(Material.IRON_AXE, 1), 1);
    }

}
