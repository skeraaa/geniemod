# Genie Mod for Minecraft 1.20.1 (Fabric)

Мод добавляет процедурно генерируемые храмы, Лампу Джина и систему желаний с риском "подковырки".

## Особенности

- 🏛️ **Храмы**: Генерируются в пустынях, джунглях и Незере
- 🧞 **Джины**: 4 уровня редкости (Common, Uncommon, Rare, Mythic)
- 📜 **Желания**: Система JSON-конфигов для гибкой настройки
- 🎲 **Удача**: Шанс негативного эффекта зависит от удачи игрока

## Установка

1. Установите [Fabric Loader](https://fabricmc.net/use/)
2. Установите [Fabric API](https://modrinth.com/mod/fabric-api)
3. Скачайте мод и поместите в папку `mods`

## Сборка из исходников

```bash
./gradlew build
```

Собранный файл появится в `build/libs/`

## Структура проекта

```
src/main/java/com/skera/genie/
├── GenieMod.java          # Основной класс мода
├── client/                # Клиентская часть
├── item/                  # Предметы (LampItem)
├── network/               # Сетевые пакеты
├── screen/                # GUI экрана Джина
└── config/                # Конфигурация желаний
```

## Создание желаний

Пример JSON в `data/genie/wishes/`:

```json
{
  "id": "genie:wealth_gold",
  "category": "Богатство",
  "displayName": "Золото x64",
  "baseRisk": 0.3,
  "effect": {
    "type": "item",
    "itemId": "minecraft:gold_ingot",
    "count": 64
  },
  "trickyEffect": {
    "type": "spawn_mob",
    "mobId": "minecraft:creeper"
  }
}
```

## Лицензия

MIT License - см. файл LICENSE

## Автор

Sкера
