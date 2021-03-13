package me.jet315.stacker.events;

import me.jet315.stacker.MobStacker;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Jet on 24/01/2018.
 */
public class OnEntityDeath implements Listener{

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity() == null) {
            return; // Not a living entity.
        }

        LivingEntity entity =  e.getEntity();

        if(entity.getType() == EntityType.ARMOR_STAND || entity.getType() == EntityType.SLIME){
            return;
        }


        if (entity.getType() != EntityType.PLAYER) {
            if (MobStacker.getEntityStacker().getEntitiesToMultiplyOnDeath().contains(entity) || (entity.getKiller() != null && MobStacker.getEntityStacker().getInstantKillPlayers().contains(entity.getKiller().getName()))) {
                MobStacker.getEntityStacker().getEntitiesToMultiplyOnDeath().remove(entity);
                e.setDroppedExp(e.getDroppedExp() * multiplyDropsReturnExp(entity, e.getDrops()));
                return;

            }
            MobStacker.getStackEntity().attemptUnstackOne(entity);
        }

        if (MobStacker.getMobStackerConfig().stackOnlySpawnerMobs) {
            MobStacker.getEntityStacker().getValidEntity().remove(entity);
        }
    }


    public int multiplyDropsReturnExp(LivingEntity dead, List<ItemStack> drops){
        int amountToMultiply = MobStacker.getStackEntity().parseAmount(dead.getCustomName());
        if(amountToMultiply <=1) return 1;
        for(ItemStack i : drops){
            ItemStack item = new ItemStack(i);
            item.setAmount(amountToMultiply);
            dead.getWorld().dropItem(dead.getLocation(),item);

        }
        return amountToMultiply;

    }

}
