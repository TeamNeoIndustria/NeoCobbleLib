package xyz.neonetwork.neocobblelib.utils;

import com.cobblemon.mod.common.api.npc.NPCClasses;
import com.cobblemon.mod.common.api.storage.party.NPCPartyStore;
import com.cobblemon.mod.common.entity.npc.NPCEntity;
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

    public static NPCEntity createNPC(ServerLevel level, BlockPos position) {
        NPCEntity npcEntity = new NPCEntity(level);

        npcEntity.setNpc(NPCClasses.INSTANCE.getByName("standard"));
        npcEntity.finalizeSpawn(level, level.getCurrentDifficultyAt(position), MobSpawnType.COMMAND, null);
        npcEntity.teleportTo(level, position.getX(), position.getY(), position.getY(), null, 0, 0);
        npcEntity.setCustomName(Component.literal("Trainer"));

        NeoConstants.SERVER.getPlayerList().getPlayers().forEach(players -> players.level().addFreshEntity(npcEntity));

        return npcEntity;
    }

    public static NPCEntity createTrainer(ServerLevel level, CobblemonPresetTrainers trainer, BlockPos position) {
        NPCEntity npcEntity = new NPCEntity(level);

        npcEntity.setNpc(NPCClasses.INSTANCE.getByName("standard"));
        npcEntity.finalizeSpawn(level, level.getCurrentDifficultyAt(position), MobSpawnType.COMMAND, null);
        npcEntity.teleportTo(level, position.getX(), position.getY(), position.getZ(), null, 0, 0);
        npcEntity.setParty(trainer.getParty().getAsNPCPartyStore(npcEntity));
        npcEntity.setCustomName(Component.literal(trainer.getName()).withStyle(trainer.getNameColour()));

        NeoConstants.SERVER.getPlayerList().getPlayers().forEach(players -> players.level().addFreshEntity(npcEntity));

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
        NeoConstants.SERVER.getPlayerList().getPlayers().forEach(players -> players.level().addFreshEntity(entity));
    }
}
