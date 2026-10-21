package xyz.neonetwork.neocobblelib.utils;

import com.cobblemon.mod.common.api.npc.NPCClasses;
import com.cobblemon.mod.common.entity.npc.NPCEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import xyz.neonetwork.neocobblelib.NeoConstants;
import xyz.neonetwork.neocobblelib.cobblemon.CobblemonPresetTrainers;

public class NPCUtils {

    public static NPCEntity createNPC(ServerLevel level, BlockPos position) {
        NPCEntity npcEntity = new NPCEntity(level);

        npcEntity.setNpc(NPCClasses.INSTANCE.getByName("standard"));
        npcEntity.finalizeSpawn(level, level.getCurrentDifficultyAt(position), MobSpawnType.COMMAND, null);
        npcEntity.teleportTo(level, position.getX(), position.getY(), position.getZ(), null, 0, 0);
        npcEntity.setCustomName(Component.literal("Trainer"));

        if (!level.addFreshEntity(npcEntity)) {
            NeoConstants.LOGGER.error("Failed to add NPC entity to the level.");
        }

        return npcEntity;
    }

    public static NPCEntity createTrainer(ServerLevel level, CobblemonPresetTrainers trainer, BlockPos position) {
        NPCEntity npcEntity = new NPCEntity(level);

        npcEntity.setNpc(NPCClasses.INSTANCE.getByName("standard"));
        npcEntity.finalizeSpawn(level, level.getCurrentDifficultyAt(position), MobSpawnType.COMMAND, null);
        npcEntity.teleportTo(level, position.getX(), position.getY(), position.getZ(), null, 0, 0);
        npcEntity.setParty(trainer.getParty().getAsNPCPartyStore(npcEntity));
        npcEntity.setCustomName(Component.literal(trainer.getName()).withStyle(trainer.getNameColour()));

        if (!level.addFreshEntity(npcEntity)) {
            NeoConstants.LOGGER.error("Failed to add NPC entity to the level.");
        }

        return npcEntity;
    }

    public static NPCEntity createTrainerDelay(ServerLevel level, BlockPos position) {
        NPCEntity npcEntity = new NPCEntity(level);

        npcEntity.setNpc(NPCClasses.INSTANCE.getByName("standard"));
        npcEntity.finalizeSpawn(level, level.getCurrentDifficultyAt(position), MobSpawnType.COMMAND, null);
        npcEntity.teleportTo(level, position.getX(), position.getY(), position.getZ(), null, 0, 0);
        npcEntity.setCustomName(Component.literal("Randomizer!").withStyle(ChatFormatting.GOLD));

        return npcEntity;
    }

    public static void sendNPCToClient(NPCEntity entity) {
        if (!entity.level().addFreshEntity(entity)) {
            NeoConstants.LOGGER.error("Failed to add NPC entity to the level.");
        }
    }
}
