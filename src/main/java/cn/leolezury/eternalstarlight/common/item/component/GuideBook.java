package cn.leolezury.eternalstarlight.common.item.component;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;

public record GuideBook(ResourceLocation id, HashSet<String> listeningNamespaces) {

	public static final Codec<GuideBook> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		ResourceLocation.CODEC.fieldOf("id").forGetter(GuideBook::id),
		Codec.STRING.listOf().xmap(Sets::newHashSet, Lists::newArrayList)
			.fieldOf("listening_namespaces").forGetter(GuideBook::listeningNamespaces)
	).apply(instance, GuideBook::new));

	public static GuideBook getGuideBook(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		if (tag == null || !tag.contains("GuideBook")) return null;
		return GuideBook.loadNBT(tag.getCompound("GuideBook"));
	}

	public static void setGuideBook(ItemStack stack, GuideBook book) {
		stack.getOrCreateTag().put("GuideBook", book.saveNBT());
	}

	public CompoundTag saveNBT() {
		CompoundTag tag = new CompoundTag();
		tag.putString("Id", id.toString());

		ListTag list = new ListTag();
		for (String ns : listeningNamespaces) {
			list.add(StringTag.valueOf(ns));
		}
		tag.put("Namespaces", list);

		return tag;
	}

	public static GuideBook loadNBT(CompoundTag tag) {
		ResourceLocation id = new ResourceLocation(tag.getString("Id"));

		HashSet<String> namespaces = new HashSet<>();
		ListTag list = tag.getList("Namespaces", Tag.TAG_STRING);
		for (Tag t : list) {
			namespaces.add(t.getAsString());
		}

		return new GuideBook(id, namespaces);
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeUtf(id.toString());
		buf.writeVarInt(listeningNamespaces.size());
		for (String ns : listeningNamespaces) {
			buf.writeUtf(ns);
		}
	}

	public static GuideBook read(FriendlyByteBuf buf) {
		ResourceLocation id = new ResourceLocation(buf.readUtf());

		int size = buf.readVarInt();
		HashSet<String> namespaces = new HashSet<>();
		for (int i = 0; i < size; i++) {
			namespaces.add(buf.readUtf());
		}

		return new GuideBook(id, namespaces);
	}
}