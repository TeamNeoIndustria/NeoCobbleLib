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

import java.util.Optional;
import java.util.function.Consumer;

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

    public static Optional<NPCEntity> spawnTrainer(ServerLevel level, CobblemonPresetTrainers trainer,
                                                   double x, double y, double z, float yaw,
                                                   Consumer<NPCEntity> beforeSpawn) {
        NPCEntity npc = new NPCEntity(level);

        npc.setNpc(NPCClasses.INSTANCE.getByName("standard"));
        npc.finalizeSpawn(level, level.getCurrentDifficultyAt(BlockPos.containing(x, y, z)), MobSpawnType.EVENT, null);
        npc.moveTo(x, y, z, yaw, 0F);
        npc.setYHeadRot(yaw);
        npc.setYBodyRot(yaw);
        npc.setParty(trainer.getParty().getAsNPCPartyStore(npc));
        npc.setCustomName(Component.literal(trainer.getName()).withStyle(trainer.getNameColour()));
        npc.setPersistenceRequired();

        beforeSpawn.accept(npc);

        if (!level.addFreshEntity(npc)) {
            NeoConstants.LOGGER.error("Failed to add trainer {} at {}, {}, {}.", trainer.getName(), x, y, z);
            return Optional.empty();
        }

        return Optional.of(npc);
    }

    public static void sendNPCToClient(NPCEntity entity) {
        if (!entity.level().addFreshEntity(entity)) {
            NeoConstants.LOGGER.error("Failed to add NPC entity to the level.");
        }
    }
}
