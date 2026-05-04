package cn.leolezury.eternalstarlight.common.spell;

import cn.leolezury.eternalstarlight.common.registry.ESSpells;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import java.util.Objects;

public record SpellCastData(boolean hasSpell, AbstractSpell spell, int strength, int castTicks, boolean offhand) {

	public static SpellCastData getDefault() {
		return new SpellCastData(false, ESSpells.GUIDANCE_OF_STARS.get(), 0, 0, false);
	}

	@Override
	public boolean hasSpell() {
		return hasSpell && spell() != null;
	}

	public SpellCastData increaseTick() {
		return new SpellCastData(hasSpell(), spell(), strength(), castTicks() + 1, offhand());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeBoolean(hasSpell());
		buf.writeVarInt(ESSpells.SPELLS.registry().getId(spell()));
		buf.writeInt(strength());
		buf.writeInt(castTicks());
		buf.writeBoolean(offhand());
	}

	public static SpellCastData read(FriendlyByteBuf buf) {
		boolean hasSpell = buf.readBoolean();
		AbstractSpell spell = ESSpells.SPELLS.registry().byId(buf.readVarInt());
		int strength = buf.readInt();
		int ticks = buf.readInt();
		boolean offhand = buf.readBoolean();
		return new SpellCastData(hasSpell, spell, strength, ticks, offhand);
	}

	public record ItemSpellSource(Item item, InteractionHand hand) implements SpellSource {
		@Override
		public boolean canContinue(LivingEntity living) {
			return living.getItemInHand(hand()).is(item());
		}
	}

	public interface SpellSource {
		boolean canContinue(LivingEntity living);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		SpellCastData that = (SpellCastData) o;
		return strength == that.strength
			&& castTicks == that.castTicks
			&& offhand == that.offhand
			&& hasSpell == that.hasSpell
			&& Objects.equals(spell, that.spell);
	}

	@Override
	public int hashCode() {
		return Objects.hash(hasSpell, spell, strength, castTicks, offhand);
	}
}
