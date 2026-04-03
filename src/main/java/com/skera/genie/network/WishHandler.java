package com.skera.genie.network;

import com.skera.genie.config.WishConfig;
import com.skera.genie.config.WishConfig.EffectData;
import com.skera.genie.config.WishConfig.Wish;
import com.skera.genie.item.ModItems;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class WishHandler {

    private static final Random RANDOM = new Random();

    public static void executeWish(ServerWorld world, PlayerEntity player, String wishId, ItemStack lampStack) {
        Wish wish = WishConfig.getWish(wishId);
        if (wish == null) {
            player.sendMessage(Text.literal("§cОшибка: Желание не найдено!"), true);
            return;
        }

        // Расчет удачи и подковырки
        float luck = (float) player.getAttributeValue(net.minecraft.entity.attribute.EntityAttributes.GENERIC_LUCK);
        float trickyChance = Math.max(0.05f, wish.baseRisk - (luck * 0.1f)); // Минимум 5% шанс
        boolean isTricky = RANDOM.nextFloat() < trickyChance;

        EffectData effectData = isTricky ? wish.trickyEffect : wish.effect;

        // Сообщение в чат
        String prefix = isTricky ? "§4§lЗЛОЙ ДЖИН: " : "§b§lДЖИН: ";
        String message = isTricky ? "Твое желание исполнено... но с подвохом!" : "Твое желание исполнено!";
        player.sendMessage(Text.literal(prefix + message), true);

        // Применение эффекта
        applyEffect(world, player, effectData, isTricky);

        // Уменьшение счетчика желаний в лампе
        decrementWishes(lampStack);
    }

    private static void applyEffect(ServerWorld world, PlayerEntity player, EffectData data, boolean isTricky) {
        if (data == null) return;

        switch (data.type) {
            case "item":
                giveItem(player, data.itemId, data.count, data.customName, data.isFake, data.nbtJson);
                break;

            case "effect":
                if (data.effectId != -1) {
                    player.addStatusEffect(new StatusEffectInstance(
                            StatusEffect.byRawId(data.effectId),
                            data.duration,
                            data.amplifier
                    ));
                }
                break;

            case "vampire":
                applyVampirism(player, data.rank); // rank: 0=Low, 1=Mid, 2=High
                break;

            case "flight":
                // Временный полет (креативный режим на время)
                player.setAbilities(new net.minecraft.entity.player.PlayerAbilities());
                player.getAbilities().allowFlying = true;
                player.getAbilities().flySpeed = 0.1f;
                player.sendAbilitiesUpdate();
                player.sendMessage(Text.literal("§eВы можете летать! Эффект временный."), true);
                // Таймер снятия полета нужно реализовывать через серверный тик или запланированную задачу
                break;

            case "god_mode":
                player.sendMessage(Text.literal("§6§lОтныне вы – божество!.. Но увы, одной воли мало."), true);
                giveItem(player, "genie:golden_lamp", 1, "Золотая Лампа", false, null);
                break;

            case "custom_item":
                handleCustomItem(player, data.itemId, isTricky);
                break;
                
            case "mob_aggro":
                // Логика аггро мобов (заглушка, требует сложной реализации AI)
                player.sendMessage(Text.literal("§cТеперь мобы чувствуют тебя за версту!"), true);
                break;

            default:
                player.sendMessage(Text.literal("§cНеизвестный тип эффекта: " + data.type), true);
        }
    }

    private static void giveItem(PlayerEntity player, String itemId, int count, String customName, boolean isFake, String nbtJson) {
        Identifier id = Identifier.tryParse(itemId);
        if (id == null) {
            // Если ID невалиден (например, для кастомных предметов, которых нет в реестре)
            player.sendMessage(Text.literal("§cПредмет не найден: " + itemId), true);
            return;
        }

        ItemStack stack = new ItemStack(net.minecraft.item.Items.STICK); // Заглушка, нужно получить из реестра
        
        // Попытка получить предмет из реестра
        net.minecraft.item.Item item = net.minecraft.registry.Registries.ITEM.get(id);
        if (item == net.minecraft.items.Items.AIR) {
             // Если предмет не зарегистрирован (фейк или кастом), даем палку с именем
             stack = new ItemStack(Items.STICK);
        } else {
             stack = new ItemStack(item, count);
        }

        if (isFake) {
            stack = new ItemStack(Items.COBBLESTONE); // Заменяем фейковые алмазы на булыжник для примера
            // В реальной моде нужно регистрировать свой блок FakeDiamondBlock
        }

        NbtCompound nbt = stack.getOrCreateNbt();
        if (customName != null && !customName.isEmpty()) {
            nbt.put("display", new NbtCompound());
            stack.getName(); // Триггер обновления
            // Устанавливаем кастомное имя через JSON текст
            nbt.putString("CustomName", "{\"text\":\"" + customName + "\",\"italic\":false}");
        }
        
        if (nbtJson != null) {
            // Парсинг доп NBT если нужно
        }

        if (!player.getInventory().insertStack(stack)) {
            player.dropItem(stack, false);
        }
    }

    private static void handleCustomItem(PlayerEntity player, String itemId, boolean isTricky) {
        ItemStack stack = new ItemStack(Items.PAPER);
        String name = "Неизвестный предмет";
        
        if ("genie:coupon_infinite".equals(itemId)) {
            stack = new ItemStack(Items.PAPER);
            name = "Купон на бесконечные желания";
            setCustomName(stack, name);
        } else if ("genie:dead_fish".equals(itemId)) {
            stack = new ItemStack(Items.COD); // Используем треску как мертвую рыбку
            name = "Мёртвая золотая рыбка";
            setCustomName(stack, name);
        } else if ("genie:seven_flower".equals(itemId)) {
            stack = new ItemStack(Items.POPPY);
            name = "Цветик-семицветик";
            setCustomName(stack, name);
        } else if ("genie:hatabych_hair".equals(itemId)) {
            stack = new ItemStack(Items.STRING);
            name = "Волос Хатабыча";
            setCustomName(stack, name);
        } else if ("genie:golden_lamp".equals(itemId)) {
            stack = new ItemStack(Items.GOLD_BLOCK); // Заглушка модели
            name = "Золотая Лампа (Божественная)";
            setCustomName(stack, name);
        }

        if (!player.getInventory().insertStack(stack)) {
            player.dropItem(stack, false);
        }
    }

    private static void setCustomName(ItemStack stack, String name) {
        NbtCompound nbt = stack.getOrCreateNbt();
        NbtCompound display = nbt.getCompound("display");
        display.putString("CustomName", "{\"text\":\"" + name + "\",\"color\":\"gold\",\"italic\":false}");
        nbt.put("display", display);
    }

    private static void applyVampirism(PlayerEntity player, int rank) {
        // rank: 0 = Низший, 1 = Вампир, 2 = Высший
        NbtCompound nbt = player.getPersistentData();
        NbtCompound genieData = nbt.getCompound("genie_data");
        
        genieData.putBoolean("is_vampire", true);
        genieData.putInt("vampire_rank", rank);
        genieData.putBoolean("blood_thirst", true);
        
        nbt.put("genie_data", genieData);

        String rankName = rank == 0 ? "Низший вампир" : (rank == 1 ? "Вампир" : "Высший вампир");
        player.sendMessage(Text.literal("§4Вы превратились в " + rankName + "!"), true);
        player.sendMessage(Text.literal("§4Используйте книгу \"Путь Вампира\" для подробностей."), true);
        
        // Начальные эффекты
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, -1, rank)); 
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, -1, rank > 0 ? 1 : 0));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, -1, rank > 0 ? 0 : 0));
    }

    private static void decrementWishes(ItemStack lampStack) {
        NbtCompound nbt = lampStack.getOrCreateNbt();
        int wishesLeft = nbt.getInt("WishesLeft");
        if (wishesLeft > 0) {
            wishesLeft--;
            nbt.putInt("WishesLeft", wishesLeft);
            if (wishesLeft <= 0) {
                lampStack.setCount(0); // Лампа исчезает
            }
        }
    }
}
