package net.neferett.linaris.api.ranks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import net.neferett.linaris.utils.ItemBuilder;
import net.neferett.linaris.utils.ObjectSerializable;
import net.neferett.linaris.utils.TimeUtils;
import net.neferett.linaris.utils.json.JSONException;
import net.neferett.linaris.utils.json.JSONObject;

public class Kit extends ObjectSerializable {

	private static final HashMap<String, Long>	cooldown	= new HashMap<>();
	@Getter
	private final long							cool;
	@Getter
	@JsonView(ObjectDataView.nonSerializable.class)
	private List<ItemStack>						items		= new ArrayList<>();
	@Getter
	private final String						name;

	@Getter
	private final int							price;

	public Kit(final JSONObject kit) throws JSONException {
		this(kit.getString("name"), kit.getLong("cool"), kit.getInt("price"));
		this.items = this.deserialize(kit.getString("items"));
	}

	public Kit(final String name, final long cooldown, final int price, final ItemStack... items) {
		super("ObjectSerializable@2");
		this.name = name;
		this.price = price;
		this.cool = cooldown;
		this.items = Arrays.asList(items);
	}

	@SuppressWarnings("deprecation")
	public List<ItemStack> deserialize(final String serialized) {
		final List<ItemStack> li = new ArrayList<>();
		System.out.println(serialized);
		final List<String> kitSerialized = Arrays.asList(serialized.split(this.sepString));

		kitSerialized.forEach(e -> {
			final List<String> list = Arrays.asList(e.split(":"));
			System.out.println(e);
			System.out.println(list.size());
			final ItemBuilder im = new ItemBuilder(Material.getMaterial(Integer.valueOf(list.get(0))),
					Integer.valueOf(list.get(1)), Short.valueOf(list.get(2)));
			list.stream().filter(la -> la.contains("-")).collect(Collectors.toList()).forEach(enchantment -> {
				im.addEnchantment(Enchantment.getById(Integer.valueOf(enchantment.split("-")[0])),
						Integer.valueOf(enchantment.split("-")[1]));
			});
			li.add(im.build());
		});
		return li;
	}

	public long getPlayer(final String a) {
		return cooldown.get(a.toLowerCase());
	}

	public void giveKits(final Player p) {
		if (this.isExists(p.getName())
				&& TimeUtils.CreateTestCoolDown(this.getCool()).test(this.getPlayer(p.getName())))
			p.sendMessage(
					"§7Tu dois attente encore §e" + TimeUtils.getTimeLeft(this.getPlayer(p.getName()), this.getCool()));
		else {
			this.setPlayer(p.getName());
			p.sendMessage("§aVous venez de recevoir le kit §b" + this.getName());
			this.getItems().forEach(item -> {
				if (item != null && item.getType() != null)
					p.getInventory().addItem(item);
			});
		}
	}

	public boolean isExists(final String a) {
		return cooldown.containsKey(a.toLowerCase());
	}

	@Override
	public String serialize() throws JSONException {
		final JSONObject o = new JSONObject();
		o.put("name", this.name);
		o.put("cool", this.cool);
		o.put("price", this.price);
		o.put("items", this.SerializeItems());
		return o.toString();
	}

	@SuppressWarnings("deprecation")
	private String SerializeEnchant(final ItemStack i) {
		final StringBuilder sb = new StringBuilder();
		i.getEnchantments().forEach((e, l) -> {
			sb.append(":" + e.getId() + "-" + l);
		});
		return sb.toString();
	}

	@SuppressWarnings("deprecation")
	public String SerializeItems() {
		final StringBuilder a = new StringBuilder();

		this.getItems().forEach(e -> {
			a.append(e.getTypeId() + ":" + e.getAmount() + ":" + e.getDurability() + ":"
					+ (e.getEnchantments().size() >= 1 ? this.SerializeEnchant(e) : ""));
			a.append(this.sepString);
		});
		return a.toString();
	}

	public void setPlayer(final String a) {
		cooldown.put(a.toLowerCase(), System.currentTimeMillis());
	}
}
