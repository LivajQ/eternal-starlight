package cn.leolezury.eternalstarlight.common.spell;

import cn.leolezury.eternalstarlight.common.registry.ESSpells;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

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
		buf.writeResourceLocation(ESSpells.SPELLS.getId(spell));
		buf.writeVarInt(cooldown);
	}

	public static SpellCooldown read(FriendlyByteBuf buf) {
		ResourceLocation id = buf.readResourceLocation();
		AbstractSpell spell = ESSpells.SPELLS.get(id);
		int cooldown = buf.readVarInt();
		return new SpellCooldown(spell, cooldown);
	}

}
