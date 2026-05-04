package cn.leolezury.eternalstarlight.common.network;

import cn.leolezury.eternalstarlight.common.EternalStarlight;
import cn.leolezury.eternalstarlight.common.crest.Crest;
import cn.leolezury.eternalstarlight.common.util.ESMiscUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class OpenCrestGuiPacket implements ESPacket {
	private final List<Crest.Instance> crests;
	private final List<Crest.Instance> ownedCrests;

	public OpenCrestGuiPacket(List<Crest.Instance> crests, List<Crest.Instance> ownedCrests) {
		this.crests = crests;
		this.ownedCrests = ownedCrests;
	}

	public static OpenCrestGuiPacket read(FriendlyByteBuf buf) {
		int size1 = buf.readInt();
		List<Crest.Instance> crests = new ArrayList<>(size1);
		for (int i = 0; i < size1; i++) {
			crests.add(Crest.Instance.read(buf));
		}

		int size2 = buf.readInt();
		List<Crest.Instance> owned = new ArrayList<>(size2);
		for (int i = 0; i < size2; i++) {
			owned.add(Crest.Instance.read(buf));
		}

		return new OpenCrestGuiPacket(crests, owned);
	}

	@Override
	public void write(FriendlyByteBuf buf) {
		buf.writeInt(crests.size());
		for (Crest.Instance inst : crests) {
			inst.write(buf);
		}

		buf.writeInt(ownedCrests.size());
		for (Crest.Instance inst : ownedCrests) {
			inst.write(buf);
		}
	}

	@Override
	public void handle(Player player) {
		ESMiscUtil.runWhenOnClient(() ->
			() -> EternalStarlight.getClientHelper().handleOpenCrestGui(this)
		);
	}

	@Override
	public ResourceLocation id() {
		return EternalStarlight.id("open_crest_gui");
	}

	public List<Crest.Instance> crests() { return crests; }
	public List<Crest.Instance> ownedCrests() { return ownedCrests; }
}
