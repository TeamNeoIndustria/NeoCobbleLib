package xyz.neonetwork.neocobblelib.utils;

import de.markusbordihn.easynpc.entity.ModEntityType;
import de.markusbordihn.easynpc.entity.ModNPCEntityType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import xyz.neonetwork.neocobblelib.NeoConstants;
import xyz.neonetwork.neocobblelib.cobblemon.CobblemonPresetTrainers;

public class NPCUtils {

    public static Entity createHumanoidNPC(ServerLevel level, String name, ChatFormatting nameColour, BlockPos position) {
        EntityType<?> type = ModEntityType.NPC_TYPE.get(ModNPCEntityType.HUMANOID).get();
        Entity entity = type.spawn(
                level,
                null,
                null,
                position,
                MobSpawnType.COMMAND,
                true,
                false
        );


        if (entity == null) {
            NeoConstants.LOGGER.error("Error spawning npc.");

            return null;
        }

        entity.setCustomName(Component.literal(name).withStyle(nameColour));
        entity.moveTo(position.getX(), position.getY(), position.getZ());

        level.addFreshEntity(entity);

        return entity;
    }

    public static Entity createTrainer(ServerLevel level, CobblemonPresetTrainers trainer, BlockPos position) {
        EntityType<?> type = ModEntityType.NPC_TYPE.get(ModNPCEntityType.HUMANOID).get();
        Entity entity = type.spawn(
                level,
                null,
                null,
                position,
                MobSpawnType.COMMAND,
                true,
                false
        );


        if (entity == null) {
            NeoConstants.LOGGER.error("Error spawning trainer.");

            return null;
        }

        entity.setCustomName(Component.literal(trainer.getName()).withStyle(trainer.getNameColour()));
        entity.moveTo(position.getX(), position.getY(), position.getZ());

        level.addFreshEntity(entity);

        return entity;
    }
}
