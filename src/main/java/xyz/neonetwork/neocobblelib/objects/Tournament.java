package xyz.neonetwork.neocobblelib.objects;

import de.markusbordihn.easynpc.data.npc.SavedNPCEntityEntry;
import de.markusbordihn.easynpc.entity.ModNPCEntityType;
import de.markusbordihn.easynpc.entity.NPCEntityFactory;
import de.markusbordihn.easynpc.entity.NPCEntityManager;
import de.markusbordihn.easynpc.entity.easynpc.EasyNPC;
import de.markusbordihn.easynpc.entity.easynpc.npc.standard.HumanoidNPC;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import xyz.neonetwork.neocobblelib.NeoConstants;

import java.util.*;

public class Tournament {

    private final ServerPlayer player;
    private UUID npcUUID;
    private final List<NPCParty> npcParties;
    private final TournamentType type;
    private int battleCounter;

    public Tournament(ServerPlayer player, TournamentType type) {
        this.player = player;
        this.npcUUID = UUID.randomUUID();
        this.npcParties = new ArrayList<>();
        this.type = type;
        this.battleCounter = 0;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public UUID getNpcUUID() {
        return npcUUID;
    }

    public List<NPCParty> getNPCParties() {
        return npcParties;
    }

    public TournamentType getType() {
        return type;
    }

    public void spawnNPC() {
        NeoConstants.LOGGER.info("spawnNPC");

        HumanoidNPC npc = new HumanoidNPC(EntityType.ZOMBIE, this.player.level());

        this.player.serverLevel().addFreshEntity(npc);
//
//        if (npc instanceof EasyNPC<?> easyNPC) {
//            NeoConstants.LOGGER.info("Entity is EasyNPC");
//            OwnerDataCapable<?> ownerData = easyNPC.getEasyNPCOwnerData();
//
//            if (ownerData != null) {
//                ownerData.setNPCOwnerUUID(this.player.getUUID());
//            }
//        }
    }

    public Optional<SavedNPCEntityEntry> getNPC() {
        return NPCEntityManager.getNPC(this.npcUUID);
    }

    public void startBattle() {
        NPCParty party = npcParties.get(battleCounter);
        NPCBattle battle = new NPCBattle(this.player, party);

        // Start fight
        battle.startBattle();
    }

    public void finishBattle() {
        this.battleCounter++;
    }
}
