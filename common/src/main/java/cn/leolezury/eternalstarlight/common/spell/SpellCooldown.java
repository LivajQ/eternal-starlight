package cn.leolezury.eternalstarlight.common.spell;

import cn.leolezury.eternalstarlight.common.registry.ESSpells;
import net.minecraft.network.FriendlyByteBuf;

public class SpellCooldown {

	private final AbstractSpell spell;
	private int cooldown;

	public SpellCooldown(AbstractSpell spell) {
		this(spell, 0);
	}

	public SpellCooldown(AbstractSpell spell, int cooldown) {
		this.spell = spell;
		this.cooldown = cooldown;
	}

	public AbstractSpell getSpell() {
		return spell;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public void tick() {
		cooldown = Math.max(cooldown - 1, 0);
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeVarInt(ESSpells.SPELLS.registry().getId(spell));
		buf.writeVarInt(cooldown);
	}

	public static SpellCooldown read(FriendlyByteBuf buf) {
		AbstractSpell spell = ESSpells.SPELLS.registry().byId(buf.readVarInt());
		int cooldown = buf.readVarInt();
		return new SpellCooldown(spell, cooldown);
	}
}
