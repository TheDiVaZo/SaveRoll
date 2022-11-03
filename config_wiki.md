Данное вики является инструкцией для работы с плагином SaveRoll.
Плагин SaveRoll был сделан специально для проекта [VotiveRP](https://votiverp.com/)

## 1. Конфиг
При первом запуске сервера с плагином, в папке SaveRoll появится файл config.yml содержащий вот такие данные:
<details>
<summary>config.yml</summary>

```yaml
#author - TheDiVaZo
#Специально для https://votiverp.com

varsion: 1.0 #version of config

#База данных MySQL пока не актуальна
#Данное поле пока не работает.
mysql:
  server_url: "jdbc:mysql://127.0.0.1/basedata"
  name: "username"
  pass: "password"
  dataSourceProperty:
    cachePrepStmts: true
    prepStmtCacheSize: 250
    prepStmtCacheSqlLimit: 2048
    #и другие...

settings:
  command:
    dice:
      distance: 15 #дистанция видимости команды.
      text: "&eИгрок %player_name% кинул ролл на [roll-name]:&c [roll-count-random] &eиз &c12-ти" #[roll-name] - короткий аналог %rollplus_[имя]_name% для вывода ролла ТОЛЬКО по команде /dice
  debug: false #debug режим.

bonusrolls:
  defend: # системное имя ролла
    # %rollplus_defend_count% - плюс к роллу игрока на защиту (defend)
    # %rollplus_defend_count_random% - рандомное число от 1 до 12 с учетом плюса к роллу.
    # %rollplus_defend_name% - имя ролла, на который идет плюс
    # %rollplus_defend_name_system% - системное имя ролла
    name: "&cЗащита" #имя ролла, которое должен видеть пользователь.
    equip:
      hard-armor: #любое название, оно не используется все равно.
        items: ["iron_helmet", "iron_chestplate", "iron_leggings", "iron_boots", "diamond_helmet", "diamond_chestplate", "diamond_leggings", "diamond_boots"]
        #id предметов из майнкрафта.
        # "iron_helmet" - железный шлем может присутствовать, а может и нет в одном из доступных слотов
        # "*iron_helmet" - железный шлем обязательно должен присутствовать в одном из доступных слотов
        # "!iron_helmet" - железный шлем не должен присутствовать в одном из доступных слотов
        slots: ["FEED", "CHEST", "HEAD", "LEGS"] #"FEED", "CHEST", "HEAD", "LEGS", "OFF_HAND", "HAND", "INVENTORY"
        # "helmet" - заполнение слота зависит от настроек fill-slot
        # "*helmet" - слот шлема обязательно должен быть заполнен и не зависит от fill-slot
        # "!helmet" - слот шлема должен оставаться пустым и не зависит от fill-slot
        fill-slot: 3 #кол-во минимальных слотов из slots, которые должны быть заполнены, чтобы сработал бонус
        # "all" - все слоты из slots
        # цифра - кол-во слотов для заполнения. Не должен привышать длинну списка slots
        additional-roll: 3 #прибавление к роллам
      shield:
        items: "*shield"
        slots: "off_hand"
        fill-slot: 1
        additional-roll: 1
    potion:
      strength-effect: #любое название
        effects: ["*strength:1", "!weakness:1"]
        #*effect:x - effect от x-ого уровня и выше обязательно должен быть наложен на игрока
        #!effect:x - effect от x-ого уровня и выше не должен быть наложен на игрока
        #effect:x - effect от x-ого уровня и выше может быть наложен, а может и нет.
        count-effects: 1 #кол-во минимальных эффектов из effects которые должны быть наложены на игрока.
        additional-roll: 1
    rider:
      horse-rider:
        animals: ["horse"] #текстовый ID животного.
        armor: ["leather_horse_armor"] #броня животного, на которого сел игрок.
        #не имеет модификаторов приоритета.
        additional-roll: 1
```
</details>

Давайте пройдемся по основным строчкам в конфиге:

### MySQL:

```yaml
mysql:
  server_url: "jdbc:mysql://127.0.0.1/basedata"
  name: "username"
  pass: "password"
  dataSourceProperty:
    cachePrepStmts: true
    prepStmtCacheSize: 250
    prepStmtCacheSqlLimit: 2048
    #и другие...
```

Данная секция нужна для подключения плагина к базе данных MySQL, но по сколько, на данный момент в плагине не реализованна эта функция, секция не будет работать.

##settings:

```yaml
settings:
  command:
    dice:
      distance: 15 #дистанция видимости команды.
      text: "&eИгрок %player_name% кинул ролл на [roll-name]:&c [roll-count-random] &eиз &c12-ти" #[roll-name] - короткий аналог %rollplus_[имя]_name% для вывода ролла ТОЛЬКО по команде /dice
  debug: false #debug режим.
```

Данная секция используется для настройки плагина. В комментариях написанны пояснения к каждому полю.

##bonusrolls:
Основная секция настроек в конфиге.

```yaml
bonusrolls:
  defend: # системное имя ролла
    # %rollplus_defend_count% - плюс к роллу игрока на защиту (defend)
    # %rollplus_defend_count_random% - рандомное число от 1 до 12 с учетом плюса к роллу.
    # %rollplus_defend_name% - имя ролла, на который идет плюс
    # %rollplus_defend_name_system% - системное имя ролла
    name: "&cЗащита" #имя ролла, которое должен видеть пользователь.
    equip:
      hard-armor: #любое название, оно не используется все равно.
        items: ["iron_helmet", "iron_chestplate", "iron_leggings", "iron_boots", "diamond_helmet", "diamond_chestplate", "diamond_leggings", "diamond_boots"]
        #id предметов из майнкрафта.
        # "iron_helmet" - железный шлем может присутствовать, а может и нет в одном из доступных слотов
        # "*iron_helmet" - железный шлем обязательно должен присутствовать в одном из доступных слотов
        # "!iron_helmet" - железный шлем не должен присутствовать в одном из доступных слотов
        slots: ["FEED", "CHEST", "HEAD", "LEGS"] #"FEED", "CHEST", "HEAD", "LEGS", "OFF_HAND", "HAND", "INVENTORY"
        # "helmet" - заполнение слота зависит от настроек fill-slot
        # "*helmet" - слот шлема обязательно должен быть заполнен и не зависит от fill-slot
        # "!helmet" - слот шлема должен оставаться пустым и не зависит от fill-slot
        fill-slot: 3 #кол-во минимальных слотов из slots, которые должны быть заполнены, чтобы сработал бонус
        # "all" - все слоты из slots
        # цифра - кол-во слотов для заполнения. Не должен привышать длинну списка slots
        additional-roll: 3 #прибавление к роллам
      shield:
        items: "*shield"
        slots: "off_hand"
        fill-slot: 1
        additional-roll: 1
    potion:
      strength-effect: #любое название
        effects: ["*strength:1", "!weakness:1"]
        #*effect:x - effect от x-ого уровня и выше обязательно должен быть наложен на игрока
        #!effect:x - effect от x-ого уровня и выше не должен быть наложен на игрока
        #effect:x - effect от x-ого уровня и выше может быть наложен, а может и нет.
        count-effects: 1 #кол-во минимальных эффектов из effects которые должны быть наложены на игрока.
        additional-roll: 1
    rider:
      horse-rider:
        animals: ["horse"] #текстовый ID животного.
        armor: ["leather_horse_armor"] #броня животного, на которого сел игрок.
        #не имеет модификаторов приоритета.
        additional-roll: 1
```

Внизу будут представлены несколько примеров этого конфига, на которых будет объесняна работа плагина.
###Примеры
И так, первый пример:
```yaml
...
  attack:
    items: ["DIAMOND_SWORD", "NETHERITE_SWORD"]
    slots: "hand"
    fill-slot: 1
    additional-roll: 1
```
На данном примере мы видим, что плюс к роллу будет начислен тогда, когда у игрока в ОСНОВНОЙ руке будет либо алмазный меч, либо незеритовый.
<br>При этом мы указываем `fill-slot: 1`, чтобы показать, что хотябы 1 слот из `slot` (он у нас и так один) должен быть заполнен предметом из `items`
<br>
<br>
Следующий пример:
```yaml
...
  defend:
    items: ["iron_helmet", "iron_chestplate", "iron_leggings", "iron_boots"]
    slots: ["FEED", "CHEST", "HEAD", "LEGS"]
    fill-slot: 3
    additional-roll: 1
```
На данном примере видно, что если у игрока заполнено не меньше 3-ех слотов из `slots` предметами из `items` то ролл начислится.
<br>Попробуем немного изменить условие:
```yaml
...
  defend:
    items: ["iron_helmet", "iron_chestplate", "iron_leggings", "iron_boots"]
    slots: ["*FEED", "*CHEST", "HEAD", "LEGS"]
    fill-slot: 3
    additional-roll: 1
```
Здесь уже для начисления ролла, нужно чтобы было обязательно заполнен слот `FEED` и `CHEST` а так же один из двух оставшихся слотов в поле `slots`.
<br>
<br>3-тий пример:
```yaml
...
  defend:
    items: ["iron_helmet", "iron_chestplate", "iron_leggings", "iron_boots"]
    slots: ["*FEED", "*CHEST", "HEAD", "!LEGS"]
    fill-slot: 3
    additional-roll: 1
```
Тут уже мы можем заметить знак `!` возле `LEGS`. Это значит, что этот слот заполнять предметами из `items` нельзя! (Обратите внимание, что если этот слот будет заполнен любыми другими предметами, которых нет в `items`, то условие проигнорируется.) 
<br>
<br>ну и последний пример:
```yaml
...
  defend:
    items: ["iron_helmet", "iron_chestplate", "!iron_leggings", "iron_boots"]
    slots: ["*FEED", "*CHEST", "HEAD", "LEGS"]
    fill-slot: 3
    additional-roll: 1
```
Если предмет `iron_leggings` будет в каком-то из слотов `slots` то плюс к роллу гарантированно не начислится на игрока, внезависимости выполнения других условий.
<br>
<br>Это были самые основные примеры. Теперь, давайте посмотрим примеры, условия в которых противоречат друг-другу. Ну или просто примеры, в которых не понятно, как будет действовать плагин.
```yaml
...
  primer-1:
    items: ["!iron_helmet"]
    slots: ["*FEED"]
    fill-slot: 1
    additional-roll: 1
```
При данном условии, плюс к роллу игрока никогда не начислится, чтобы он не надел.
<br>в `items` указан один предмет, который никогда нельзя экипировать на `slots`. Если его одеть - условие не будет выполнено.
<br>Но при этом, в `slots` указана `*` перед единственным слотом `FEED` - значит он должен быть заполнен предметом из `items`.
<br>Но так как в `items` нет предметов, которые были бы необязательными, либо обязательными к экипировке, то это условие тоже никогда не будет выполнено.
<br>
<br>2-ой пример:
```yaml
...
  primer-2:
    items: ["iron_helmet"]
    slots: ["FEED"]
    fill-slot: 2
    additional-roll: 1
```
Здесь кол-во требуемых к заполнению слотов, больше кол-ва слотов указанных в `slots` - соответственно условие тоже никогда не выполнится.
<br>
<br>3-ий пример
```yaml
...
  primer-3:
    items: ["iron_helmet"]
    slots: ["FEED"]
    fill-slot: 0
    additional-roll: 1
```
Здесь кол-во требуемых к заполнению слотов равняется нулю, а слот и предмет указанный в `slots` и в `items` соответственно, являются не обязательными. Такое условие будет выполнено всегда.
<br>
<br>Список примеров еще будет дополняться. А пока, на этом все.
<br>
### Данный раздел еще будет дополнятся.