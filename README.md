# emoji-java

[![Build Status](https://travis-ci.org/vdurmont/emoji-java.svg?branch=master)](https://travis-ci.org/vdurmont/emoji-java)
[![Coverage Status](https://img.shields.io/coveralls/vdurmont/emoji-java.svg)](https://coveralls.io/r/vdurmont/emoji-java?branch=master)
[![License Info](http://img.shields.io/badge/license-The%20MIT%20License-brightgreen.svg)](https://github.com/vdurmont/emoji-java/blob/master/LICENSE.md)

*The missing emoji library for java.*

**emoji-java** is a lightweight java library that helps you use Emojis in your java applications.

## How to get it?

##### Via Maven:
```xml
<dependency>
  <groupId>com.vdurmont</groupId>
  <artifactId>emoji-java</artifactId>
  <version>3.3.0</version>
</dependency>
```

You can also download the project, build it with `mvn clean install` and add the generated jar to your buildpath.

##### Via Gradle:
```gradle
compile 'com.vdurmont:emoji-java:3.3.0'
```

##### Via Direct Download:

* Use [releases](https://github.com/vdurmont/emoji-java/releases) tab to download the jar directly.
* Download JSON-java dependency from http://mvnrepository.com/artifact/org.json/json.

## How to use it?



### EmojiManager

The `EmojiManager` provides several static methods to search through the emojis database:

* `getForTag` returns all the emojis for a given tag
* `getForAlias` returns the emoji for an alias
* `getAll` returns all the emojis
* `isEmoji` checks if a string is an emoji

You can also query the metadata:

* `getAllTags` returns the available tags

Or get everything:

* `getAll` returns all the emojis

### Emoji model

An `Emoji` is a POJO (plain old java object), which provides the following methods:

* `getUnicode` returns the unicode representation of the emoji
* `getUnicode(Fitzpatrick)` returns the unicode representation of the emoji with the provided Fitzpatrick modifier. If the emoji doesn't support the Fitzpatrick modifiers, this method will throw an `UnsupportedOperationException`. If the provided Fitzpatrick is null, this method will return the unicode of the emoji.
* `getDescription` returns the (optional) description of the emoji
* `getAliases` returns a list of aliases for this emoji
* `getTags` returns a list of tags for this emoji
* `getHtmlDecimal` returns an html decimal representation of the emoji
* `getHtmlHexadecimal` returns an html decimal representation of the emoji
* `supportsFitzpatrick` returns true if the emoji supports the Fitzpatrick modifiers, else false

### Fitzpatrick modifiers

Some emojis now support the use of Fitzpatrick modifiers that gives the choice between 5 shades of skin tones:

| Modifier | Type |
| :---: | ------- |
| 🏻 | type_1_2 |
| 🏼 | type_3 |
| 🏽 | type_4 |
| 🏾 | type_5 |
| 🏿 | type_6 |

We defined the format of the aliases including a Fitzpatrick modifier as:

```
:ALIAS|TYPE:
```

A few examples:

```
:boy|type_1_2:
:swimmer|type_4:
:santa|type_6:
```

### EmojiParser

#### To unicode

To replace all the aliases and the html representations found in a string by their unicode, use `EmojiParser#parseToUnicode(String)`.

For example:

```java
String str = "An :grinning:awesome :smiley:string &#128516;with a few :wink:emojis!";
String result = EmojiParser.parseToUnicode(str);
System.out.println(result);
// Prints:
// "An 😀awesome 😃string 😄with a few 😉emojis!"
```

#### To aliases

To replace all the emoji's unicodes found in a string by their aliases, use `EmojiParser#parseToAliases(String)`.

For example:

```java
String str = "An 😀awesome 😃string with a few 😉emojis!";
String result = EmojiParser.parseToAliases(str);
System.out.println(result);
// Prints:
// "An :grinning:awesome :smiley:string with a few :wink:emojis!"
```

By default, the aliases will parse and include any Fitzpatrick modifier that would be provided. If you want to remove or ignore the Fitzpatrick modifiers, use `EmojiParser#parseToAliases(String, FitzpatrickAction)`. Examples:

```java
String str = "Here is a boy: \uD83D\uDC66\uD83C\uDFFF!";
System.out.println(EmojiParser.parseToAliases(str));
System.out.println(EmojiParser.parseToAliases(str, FitzpatrickAction.PARSE));
// Prints twice: "Here is a boy: :boy|type_6:!"
System.out.println(EmojiParser.parseToAliases(str, FitzpatrickAction.REMOVE));
// Prints: "Here is a boy: :boy:!"
System.out.println(EmojiParser.parseToAliases(str, FitzpatrickAction.IGNORE));
// Prints: "Here is a boy: :boy:🏿!"
```

#### To html

To replace all the emoji's unicodes found in a string by their html representation, use `EmojiParser#parseToHtmlDecimal(String)` or `EmojiParser#parseToHtmlHexadecimal(String)`.

For example:

```java
String str = "An 😀awesome 😃string with a few 😉emojis!";

String resultDecimal = EmojiParser.parseToHtmlDecimal(str);
System.out.println(resultDecimal);
// Prints:
// "An &#128512;awesome &#128515;string with a few &#128521;emojis!"

String resultHexadecimal = EmojiParser.parseToHtmlHexadecimal(str);
System.out.println(resultHexadecimal);
// Prints:
// "An &#x1f600;awesome &#x1f603;string with a few &#x1f609;emojis!"
```

By default, any Fitzpatrick modifier will be removed. If you want to ignore the Fitzpatrick modifiers, use `EmojiParser#parseToAliases(String, FitzpatrickAction)`. Examples:

```java
String str = "Here is a boy: \uD83D\uDC66\uD83C\uDFFF!";
System.out.println(EmojiParser.parseToHtmlDecimal(str));
System.out.println(EmojiParser.parseToHtmlDecimal(str, FitzpatrickAction.PARSE));
System.out.println(EmojiParser.parseToHtmlDecimal(str, FitzpatrickAction.REMOVE));
// Print 3 times: "Here is a boy: &#128102;!"
System.out.println(EmojiParser.parseToHtmlDecimal(str, FitzpatrickAction.IGNORE));
// Prints: "Here is a boy: &#128102;🏿!"
```

The same applies for the methods `EmojiParser#parseToHtmlHexadecimal(String)` and `EmojiParser#parseToHtmlHexadecimal(String, FitzpatrickAction)`.

#### Remove emojis

You can easily remove emojis from a string using one of the following methods:

* `EmojiParser#removeAllEmojis(String)`: removes all the emojis from the String
* `EmojiParser#removeAllEmojisExcept(String, Collection<Emoji>)`: removes all the emojis from the String, except the ones in the Collection
* `EmojiParser#removeEmojis(String, Collection<Emoji>)`: removes the emojis in the Collection from the String

For example:

```java
String str = "An 😀awesome 😃string with a few 😉emojis!";
Collection<Emoji> collection = new ArrayList<Emoji>();
collection.add(EmojiManager.getForAlias("wink")); // This is 😉

System.out.println(EmojiParser.removeAllEmojis(str));
System.out.println(EmojiParser.removeAllEmojisExcept(str, collection));
System.out.println(EmojiParser.removeEmojis(str, collection));

// Prints:
// "An awesome string with a few emojis!"
// "An awesome string with a few 😉emojis!"
// "An 😀awesome 😃string with a few emojis!"
```

## Credits

**emoji-java** originally used the data provided by the [github/gemoji project](https://github.com/github/gemoji). It is still based on it but has evolved since.

## Available Emojis

Here is a table of the available emojis and their aliases.

| Emoji | Aliases | Emoji | Aliases |
| :---: | ------- | :---: | ------- |
| 😄 | smile | 😃 | smiley |
| 😀 | grinning | 😊 | blush |
| ☺ | relaxed | 😉 | wink |
| 😍 | heart_eyes | 😘 | kissing_heart |
| 😚 | kissing_closed_eyes | 😗 | kissing |
| 😙 | kissing_smiling_eyes | 😜 | stuck_out_tongue_winking_eye |
| 😝 | stuck_out_tongue_closed_eyes | 😛 | stuck_out_tongue |
| 😳 | flushed | 😁 | grin |
| 😔 | pensive | 😌 | relieved |
| 😒 | unamused | 😞 | disappointed |
| 😣 | persevere | 😢 | cry |
| 😂 | joy | 😭 | sob |
| 😪 | sleepy | 😥 | disappointed_relieved |
| 😰 | cold_sweat | 😅 | sweat_smile |
| 😓 | sweat | 😩 | weary |
| 😫 | tired_face | 😨 | fearful |
| 😱 | scream | 😠 | angry |
| 😡 | rage | 😤 | triumph |
| 😖 | confounded | 😆 | laughing, satisfied |
| 😋 | yum | 😷 | mask |
| 😎 | sunglasses | 😴 | sleeping |
| 😵 | dizzy_face | 😲 | astonished |
| 😟 | worried | 😦 | frowning |
| 😧 | anguished | 😈 | smiling_imp |
| 👿 | imp | 😮 | open_mouth |
| 😬 | grimacing | 😐 | neutral_face |
| 😕 | confused | 😯 | hushed |
| 😶 | no_mouth | 😇 | innocent |
| 😏 | smirk | 😑 | expressionless |
| 👲 | man_with_gua_pi_mao | 👳 | man_with_turban |
| 👮 | cop | 👷 | construction_worker |
| 💂 | guardsman | 👶 | baby |
| 👦 | boy | 👧 | girl |
| 👨 | man | 👩 | woman |
| 👴 | older_man | 👵 | older_woman |
| 👱 | person_with_blond_hair | 👼 | angel |
| 👸 | princess | 😺 | smiley_cat |
| 😸 | smile_cat | 😻 | heart_eyes_cat |
| 😽 | kissing_cat | 😼 | smirk_cat |
| 🙀 | scream_cat | 😿 | crying_cat_face |
| 😹 | joy_cat | 😾 | pouting_cat |
| 👹 | japanese_ogre | 👺 | japanese_goblin |
| 🙈 | see_no_evil | 🙉 | hear_no_evil |
| 🙊 | speak_no_evil | 💀 | skull |
| 👽 | alien | 💩 | hankey, poop, shit |
| 🔥 | fire | ✨ | sparkles |
| 🌟 | star2 | 💫 | dizzy |
| 💥 | boom, collision | 💢 | anger |
| 💦 | sweat_drops | 💧 | droplet |
| 💤 | zzz | 💨 | dash |
| 👂 | ear | 👀 | eyes |
| 👃 | nose | 👅 | tongue |
| 👄 | lips | 👍 | +1, thumbsup |
| 👎 | -1, thumbsdown | 👌 | ok_hand |
| 👊 | facepunch, punch | ✊ | fist |
| ✌ | v | 👋 | wave |
| ✋ | hand, raised_hand | 👐 | open_hands |
| 👆 | point_up_2 | 👇 | point_down |
| 👉 | point_right | 👈 | point_left |
| 🙌 | raised_hands | 🙏 | pray |
| ☝ | point_up | 👏 | clap |
| 💪 | muscle | 🚶 | walking |
| 🏃 | runner, running | 💃 | dancer |
| 👫 | couple | 👪 | family |
| 👬 | two_men_holding_hands | 👭 | two_women_holding_hands |
| 💏 | couplekiss | 💑 | couple_with_heart |
| 👯 | dancers | 🙆 | ok_woman |
| 🙅 | no_good | 💁 | information_desk_person |
| 🙋 | raising_hand | 💆 | massage |
| 💇 | haircut | 💅 | nail_care |
| 👰 | bride_with_veil | 🙎 | person_with_pouting_face |
| 🙍 | person_frowning | 🙇 | bow |
| 🙇‍♀️ | woman_bow, female_bow | 🙇‍♂️ | man_bow, male_bow |
| 🎩 | tophat | 👑 | crown |
| 👒 | womans_hat | 👟 | athletic_shoe |
| 👞 | mans_shoe, shoe | 👡 | sandal |
| 👠 | high_heel | 👢 | boot |
| 👕 | shirt, tshirt | 👔 | necktie |
| 👚 | womans_clothes | 👗 | dress |
| 🎽 | running_shirt_with_sash | 👖 | jeans |
| 👘 | kimono | 👙 | bikini |
| 💼 | briefcase | 👜 | handbag |
| 👝 | pouch | 👛 | purse |
| 👓 | eyeglasses | 🎀 | ribbon |
| 🌂 | closed_umbrella | 💄 | lipstick |
| 💛 | yellow_heart | 💙 | blue_heart |
| 💜 | purple_heart | 💚 | green_heart |
| ❤ | heart | 💔 | broken_heart |
| 💗 | heartpulse | 💓 | heartbeat |
| 💕 | two_hearts | 💖 | sparkling_heart |
| 💞 | revolving_hearts | 💘 | cupid |
| 💌 | love_letter | 💋 | kiss |
| 💍 | ring | 💎 | gem |
| 👤 | bust_in_silhouette | 👥 | busts_in_silhouette |
| 💬 | speech_balloon | 👣 | footprints |
| 💭 | thought_balloon | 🐶 | dog |
| 🐺 | wolf | 🐱 | cat |
| 🐭 | mouse | 🐹 | hamster |
| 🐰 | rabbit | 🐸 | frog |
| 🐯 | tiger | 🐨 | koala |
| 🐻 | bear | 🐷 | pig |
| 🐽 | pig_nose | 🐮 | cow |
| 🐗 | boar | 🐵 | monkey_face |
| 🐒 | monkey | 🐴 | horse |
| 🐑 | sheep | 🐘 | elephant |
| 🐼 | panda_face | 🐧 | penguin |
| 🐦 | bird | 🐤 | baby_chick |
| 🐥 | hatched_chick | 🐣 | hatching_chick |
| 🐔 | chicken | 🐍 | snake |
| 🐢 | turtle | 🐛 | bug |
| 🐝 | bee, honeybee | 🐜 | ant |
| 🐞 | beetle | 🐌 | snail |
| 🐙 | octopus | 🐚 | shell |
| 🐠 | tropical_fish | 🐟 | fish |
| 🐬 | dolphin, flipper | 🐳 | whale |
| 🐋 | whale2 | 🐄 | cow2 |
| 🐏 | ram | 🐀 | rat |
| 🐃 | water_buffalo | 🐅 | tiger2 |
| 🐇 | rabbit2 | 🐉 | dragon |
| 🐎 | racehorse | 🐐 | goat |
| 🐓 | rooster | 🐕 | dog2 |
| 🐖 | pig2 | 🐁 | mouse2 |
| 🐂 | ox | 🐲 | dragon_face |
| 🐡 | blowfish | 🐊 | crocodile |
| 🐫 | camel | 🐪 | dromedary_camel |
| 🐆 | leopard | 🐈 | cat2 |
| 🐩 | poodle | 🐾 | feet, paw_prints |
| 💐 | bouquet | 🌸 | cherry_blossom |
| 🌷 | tulip | 🍀 | four_leaf_clover |
| 🌹 | rose | 🌻 | sunflower |
| 🌺 | hibiscus | 🍁 | maple_leaf |
| 🍃 | leaves | 🍂 | fallen_leaf |
| 🌿 | herb | 🌾 | ear_of_rice |
| 🍄 | mushroom | 🌵 | cactus |
| 🌴 | palm_tree | 🌲 | evergreen_tree |
| 🌳 | deciduous_tree | 🌰 | chestnut |
| 🌱 | seedling | 🌼 | blossom |
| 🌐 | globe_with_meridians | 🌞 | sun_with_face |
| 🌝 | full_moon_with_face | 🌚 | new_moon_with_face |
| 🌑 | new_moon | 🌒 | waxing_crescent_moon |
| 🌓 | first_quarter_moon | 🌔 | moon, waxing_gibbous_moon |
| 🌕 | full_moon | 🌖 | waning_gibbous_moon |
| 🌗 | last_quarter_moon | 🌘 | waning_crescent_moon |
| 🌜 | last_quarter_moon_with_face | 🌛 | first_quarter_moon_with_face |
| 🌙 | crescent_moon | 🌍 | earth_africa |
| 🌎 | earth_americas | 🌏 | earth_asia |
| 🌋 | volcano | 🌌 | milky_way |
| 🌠 | stars | ⭐ | star |
| ☀ | sunny | ⛅ | partly_sunny |
| ☁ | cloud | ⚡ | zap |
| ☔ | umbrella | ❄ | snowflake |
| ⛄ | snowman | 🌀 | cyclone |
| 🌁 | foggy | 🌈 | rainbow |
| 🌊 | ocean | 🎍 | bamboo |
| 💝 | gift_heart | 🎎 | dolls |
| 🎒 | school_satchel | 🎓 | mortar_board |
| 🎏 | flags | 🎆 | fireworks |
| 🎇 | sparkler | 🎐 | wind_chime |
| 🎑 | rice_scene | 🎃 | jack_o_lantern |
| 👻 | ghost | 🎅 | santa |
| 🎄 | christmas_tree | 🎁 | gift |
| 🎋 | tanabata_tree | 🎉 | tada |
| 🎊 | confetti_ball | 🎈 | balloon |
| 🎌 | crossed_flags | 🔮 | crystal_ball |
| 🎥 | movie_camera | 📷 | camera |
| 📹 | video_camera | 📼 | vhs |
| 💿 | cd | 📀 | dvd |
| 💽 | minidisc | 💾 | floppy_disk |
| 💻 | computer | 📱 | iphone |
| ☎ | phone, telephone | 📞 | telephone_receiver |
| 📟 | pager | 📠 | fax |
| 📡 | satellite_antenna | 📺 | tv |
| 📻 | radio | 🔊 | loud_sound |
| 🔉 | sound | 🔈 | speaker |
| 🔇 | mute | 🔔 | bell |
| 🔕 | no_bell | 📢 | loudspeaker |
| 📣 | mega | ⏳ | hourglass_flowing_sand |
| ⌛ | hourglass | ⏰ | alarm_clock |
| ⌚ | watch | 🔓 | unlock |
| 🔒 | lock | 🔏 | lock_with_ink_pen |
| 🔐 | closed_lock_with_key | 🔑 | key |
| 🔎 | mag_right | 💡 | bulb |
| 🔦 | flashlight | 🔆 | high_brightness |
| 🔅 | low_brightness | 🔌 | electric_plug |
| 🔋 | battery | 🔍 | mag |
| 🛁 | bathtub | 🛀 | bath |
| 🚿 | shower | 🚽 | toilet |
| 🔧 | wrench | 🔩 | nut_and_bolt |
| 🔨 | hammer | 🚪 | door |
| 🚬 | smoking | 💣 | bomb |
| 🔫 | gun | 🔪 | hocho, knife |
| 💊 | pill | 💉 | syringe |
| 💰 | moneybag | 💴 | yen |
| 💵 | dollar | 💷 | pound |
| 💶 | euro | 💳 | credit_card |
| 💸 | money_with_wings | 📲 | calling |
| 📧 | e-mail | 📥 | inbox_tray |
| 📤 | outbox_tray | ✉ | email, envelope |
| 📩 | envelope_with_arrow | 📨 | incoming_envelope |
| 📯 | postal_horn | 📫 | mailbox |
| 📪 | mailbox_closed | 📬 | mailbox_with_mail |
| 📭 | mailbox_with_no_mail | 📮 | postbox |
| 📦 | package | 📝 | memo, pencil |
| 📄 | page_facing_up | 📃 | page_with_curl |
| 📑 | bookmark_tabs | 📊 | bar_chart |
| 📈 | chart_with_upwards_trend | 📉 | chart_with_downwards_trend |
| 📜 | scroll | 📋 | clipboard |
| 📅 | date | 📆 | calendar |
| 📇 | card_index | 📁 | file_folder |
| 📂 | open_file_folder | ✂ | scissors |
| 📌 | pushpin | 📎 | paperclip |
| ✒ | black_nib | ✏ | pencil2 |
| 📏 | straight_ruler | 📐 | triangular_ruler |
| 📕 | closed_book | 📗 | green_book |
| 📘 | blue_book | 📙 | orange_book |
| 📓 | notebook | 📔 | notebook_with_decorative_cover |
| 📒 | ledger | 📚 | books |
| 📖 | book, open_book | 🔖 | bookmark |
| 📛 | name_badge | 🔬 | microscope |
| 🔭 | telescope | 📰 | newspaper |
| 🎨 | art | 🎬 | clapper |
| 🎤 | microphone | 🎧 | headphones |
| 🎼 | musical_score | 🎵 | musical_note |
| 🎶 | notes | 🎹 | musical_keyboard |
| 🎻 | violin | 🎺 | trumpet |
| 🎷 | saxophone | 🎸 | guitar |
| 👾 | space_invader | 🎮 | video_game |
| 🃏 | black_joker | 🎴 | flower_playing_cards |
| 🀄 | mahjong | 🎲 | game_die |
| 🎯 | dart | 🏈 | football |
| 🏀 | basketball | ⚽ | soccer |
| ⚾ | baseball | 🎾 | tennis |
| 🎱 | 8ball | 🏉 | rugby_football |
| 🎳 | bowling | ⛳ | golf |
| 🚵 | mountain_bicyclist | 🚴 | bicyclist |
| 🏁 | checkered_flag | 🏇 | horse_racing |
| 🏆 | trophy | 🎿 | ski |
| 🏂 | snowboarder | 🏊 | swimmer |
| 🏄 | surfer | 🎣 | fishing_pole_and_fish |
| ☕ | coffee | 🍵 | tea |
| 🍶 | sake | 🍼 | baby_bottle |
| 🍺 | beer | 🍻 | beers |
| 🍸 | cocktail | 🍹 | tropical_drink |
| 🍷 | wine_glass | 🍴 | fork_and_knife |
| 🍕 | pizza | 🍔 | hamburger |
| 🍟 | fries | 🍗 | poultry_leg |
| 🍖 | meat_on_bone | 🍝 | spaghetti |
| 🍛 | curry | 🍤 | fried_shrimp |
| 🍱 | bento | 🍣 | sushi |
| 🍥 | fish_cake | 🍙 | rice_ball |
| 🍘 | rice_cracker | 🍚 | rice |
| 🍜 | ramen | 🍲 | stew |
| 🍢 | oden | 🍡 | dango |
| 🍳 | cooking | 🍞 | bread |
| 🍩 | doughnut | 🍮 | custard |
| 🍦 | icecream | 🍨 | ice_cream |
| 🍧 | shaved_ice | 🎂 | birthday |
| 🍰 | cake | 🍪 | cookie |
| 🍫 | chocolate_bar | 🍬 | candy |
| 🍭 | lollipop | 🍯 | honey_pot |
| 🍎 | apple | 🍏 | green_apple |
| 🍊 | tangerine | 🍋 | lemon |
| 🍒 | cherries | 🍇 | grapes |
| 🍉 | watermelon | 🍓 | strawberry |
| 🍑 | peach | 🍈 | melon |
| 🍌 | banana | 🍐 | pear |
| 🍍 | pineapple | 🍠 | sweet_potato |
| 🍆 | eggplant | 🍅 | tomato |
| 🌽 | corn | 🏠 | house |
| 🏡 | house_with_garden | 🏫 | school |
| 🏢 | office | 🏣 | post_office |
| 🏥 | hospital | 🏦 | bank |
| 🏪 | convenience_store | 🏩 | love_hotel |
| 🏨 | hotel | 💒 | wedding |
| ⛪ | church | 🏬 | department_store |
| 🏤 | european_post_office | 🌇 | city_sunrise |
| 🌆 | city_sunset | 🏯 | japanese_castle |
| 🏰 | european_castle | ⛺ | tent |
| 🏭 | factory | 🗼 | tokyo_tower |
| 🗾 | japan | 🗻 | mount_fuji |
| 🌄 | sunrise_over_mountains | 🌅 | sunrise |
| 🌃 | night_with_stars | 🗽 | statue_of_liberty |
| 🌉 | bridge_at_night | 🎠 | carousel_horse |
| 🎡 | ferris_wheel | ⛲ | fountain |
| 🎢 | roller_coaster | 🚢 | ship |
| ⛵ | boat, sailboat | 🚤 | speedboat |
| 🚣 | rowboat | ⚓ | anchor |
| 🚀 | rocket | ✈ | airplane |
| 💺 | seat | 🚁 | helicopter |
| 🚂 | steam_locomotive | 🚊 | tram |
| 🚉 | station | 🚞 | mountain_railway |
| 🚆 | train2 | 🚄 | bullettrain_side |
| 🚅 | bullettrain_front | 🚈 | light_rail |
| 🚇 | metro | 🚝 | monorail |
| 🚋 | train | 🚃 | railway_car |
| 🚎 | trolleybus | 🚌 | bus |
| 🚍 | oncoming_bus | 🚙 | blue_car |
| 🚘 | oncoming_automobile | 🚗 | car, red_car |
| 🚕 | taxi | 🚖 | oncoming_taxi |
| 🚛 | articulated_lorry | 🚚 | truck |
| 🚨 | rotating_light | 🚓 | police_car |
| 🚔 | oncoming_police_car | 🚒 | fire_engine |
| 🚑 | ambulance | 🚐 | minibus |
| 🚲 | bike | 🚡 | aerial_tramway |
| 🚟 | suspension_railway | 🚠 | mountain_cableway |
| 🚜 | tractor | 💈 | barber |
| 🚏 | busstop | 🎫 | ticket |
| 🚦 | vertical_traffic_light | 🚥 | traffic_light |
| ⚠ | warning | 🚧 | construction |
| 🔰 | beginner | ⛽ | fuelpump |
| 🏮 | izakaya_lantern, lantern | 🎰 | slot_machine |
| ♨ | hotsprings | 🗿 | moyai |
| 🎪 | circus_tent | 🎭 | performing_arts |
| 📍 | round_pushpin | 🚩 | triangular_flag_on_post |
| 1⃣ | one | 2⃣ | two |
| 3⃣ | three | 4⃣ | four |
| 5⃣ | five | 6⃣ | six |
| 7⃣ | seven | 8⃣ | eight |
| 9⃣ | nine | 0⃣ | zero |
| 🔟 | keycap_ten | 🔢 | 1234 |
| #⃣ | hash | 🔣 | symbols |
| ⬆ | arrow_up | ⬇ | arrow_down |
| ⬅ | arrow_left | ➡ | arrow_right |
| 🔠 | capital_abcd | 🔡 | abcd |
| 🔤 | abc | ↗ | arrow_upper_right |
| ↖ | arrow_upper_left | ↘ | arrow_lower_right |
| ↙ | arrow_lower_left | ↔ | left_right_arrow |
| ↕ | arrow_up_down | 🔄 | arrows_counterclockwise |
| ◀ | arrow_backward | ▶ | arrow_forward |
| 🔼 | arrow_up_small | 🔽 | arrow_down_small |
| ↩ | leftwards_arrow_with_hook | ↪ | arrow_right_hook |
| ℹ | information_source | ⏪ | rewind |
| ⏩ | fast_forward | ⏫ | arrow_double_up |
| ⏬ | arrow_double_down | ⤵ | arrow_heading_down |
| ⤴ | arrow_heading_up | 🆗 | ok |
| 🔀 | twisted_rightwards_arrows | 🔁 | repeat |
| 🔂 | repeat_one | 🆕 | new |
| 🆙 | up | 🆒 | cool |
| 🆓 | free | 🆖 | squared_ng |
| 📶 | signal_strength | 🎦 | cinema |
| 🈁 | koko | 🈯 | u6307 |
| 🈳 | u7a7a | 🈵 | u6e80 |
| 🈴 | u5408 | 🈲 | u7981 |
| 🉐 | ideograph_advantage | 🈹 | u5272 |
| 🈺 | u55b6 | 🈶 | u6709 |
| 🈚 | u7121 | 🚻 | restroom |
| 🚹 | mens | 🚺 | womens |
| 🚼 | baby_symbol | 🚾 | wc |
| 🚰 | potable_water | 🚮 | put_litter_in_its_place |
| 🅿 | parking | ♿ | wheelchair |
| 🚭 | no_smoking | 🈷 | u6708 |
| 🈸 | u7533 | 🈂 | sa |
| Ⓜ | m | 🛂 | passport_control |
| 🛄 | baggage_claim | 🛅 | left_luggage |
| 🛃 | customs | 🉑 | accept |
| ㊙ | secret | ㊗ | congratulations |
| 🆑 | cl | 🆘 | sos |
| 🆔 | id | 🚫 | no_entry_sign |
| 🔞 | underage | 📵 | no_mobile_phones |
| 🚯 | do_not_litter | 🚱 | non-potable_water |
| 🚳 | no_bicycles | 🚷 | no_pedestrians |
| 🚸 | children_crossing | ⛔ | no_entry |
| ✳ | eight_spoked_asterisk | ❇ | sparkle |
| ❎ | negative_squared_cross_mark | ✅ | white_check_mark |
| ✴ | eight_pointed_black_star | 💟 | heart_decoration |
| 🆚 | vs | 📳 | vibration_mode |
| 📴 | mobile_phone_off | 🅰 | a |
| 🅱 | b | 🆎 | ab |
| 🅾 | o2 | 💠 | diamond_shape_with_a_dot_inside |
| ➿ | loop | ♻ | recycle |
| ♈ | aries | ♉ | taurus |
| ♊ | gemini | ♋ | cancer |
| ♌ | leo | ♍ | virgo |
| ♎ | libra | ♏ | scorpius |
| ♐ | sagittarius | ♑ | capricorn |
| ♒ | aquarius | ♓ | pisces |
| ⛎ | ophiuchus | 🔯 | six_pointed_star |
| 🏧 | atm | 💹 | chart |
| 💲 | heavy_dollar_sign | 💱 | currency_exchange |
| © | copyright | ® | registered |
| ™ | tm | ❌ | x |
| ‼ | bangbang | ⁉ | interrobang |
| ❗ | exclamation, heavy_exclamation_mark | ❓ | question |
| ❕ | grey_exclamation | ❔ | grey_question |
| ⭕ | o | 🔝 | top |
| 🔚 | end | 🔙 | back |
| 🔛 | on | 🔜 | soon |
| 🔃 | arrows_clockwise | 🕛 | clock12 |
| 🕧 | clock1230 | 🕐 | clock1 |
| 🕜 | clock130 | 🕑 | clock2 |
| 🕝 | clock230 | 🕒 | clock3 |
| 🕞 | clock330 | 🕓 | clock4 |
| 🕟 | clock430 | 🕔 | clock5 |
| 🕠 | clock530 | 🕕 | clock6 |
| 🕖 | clock7 | 🕗 | clock8 |
| 🕘 | clock9 | 🕙 | clock10 |
| 🕚 | clock11 | 🕡 | clock630 |
| 🕢 | clock730 | 🕣 | clock830 |
| 🕤 | clock930 | 🕥 | clock1030 |
| 🕦 | clock1130 | ✖ | heavy_multiplication_x |
| ➕ | heavy_plus_sign | ➖ | heavy_minus_sign |
| ➗ | heavy_division_sign | ♠ | spades |
| ♥ | hearts | ♣ | clubs |
| ♦ | diamonds | 💮 | white_flower |
| 💯 | 100 | ✔ | heavy_check_mark |
| ☑ | ballot_box_with_check | 🔘 | radio_button |
| 🔗 | link | ➰ | curly_loop |
| 〰 | wavy_dash | 〽 | part_alternation_mark |
| 🔱 | trident | ◼ | black_medium_square |
| ◻ | white_medium_square | ◾ | black_medium_small_square |
| ◽ | white_medium_small_square | ▪ | black_small_square |
| ▫ | white_small_square | 🔺 | small_red_triangle |
| 🔲 | black_square_button | 🔳 | white_square_button |
| ⚫ | black_circle | ⚪ | white_circle |
| 🔴 | red_circle | 🔵 | large_blue_circle |
| 🔻 | small_red_triangle_down | ⬜ | white_large_square |
| ⬛ | black_large_square | 🔶 | large_orange_diamond |
| 🔷 | large_blue_diamond | 🔸 | small_orange_diamond |
| 🔹 | small_blue_diamond | 🇦🇫 | af |
| 🇦🇱 | al | 🇩🇿 | dz |
| 🇦🇸 | as | 🇦🇩 | ad |
| 🇦🇴 | ao | 🇦🇮 | ai |
| 🇦🇬 | ag | 🇦🇷 | ar |
| 🇦🇲 | am | 🇦🇼 | aw |
| 🇦🇺 | au | 🇦🇹 | at |
| 🇦🇿 | az | 🇧🇸 | bs |
| 🇧🇭 | bh | 🇧🇩 | bd |
| 🇧🇧 | bb | 🇧🇾 | by |
| 🇧🇪 | be | 🇧🇿 | bz |
| 🇧🇯 | bj | 🇧🇲 | bm |
| 🇧🇹 | bt | 🇧🇴 | bo |
| 🇧🇦 | ba | 🇧🇼 | bw |
| 🇧🇷 | br | 🇻🇬 | vg |
| 🇧🇳 | bn | 🇧🇬 | bg |
| 🇧🇫 | bf | 🇧🇮 | bi |
| 🇰🇭 | kh | 🇨🇲 | cm |
| 🇨🇦 | ca | 🇨🇻 | cv |
| 🇰🇾 | ky | 🇨🇫 | cf |
| 🇨🇱 | cl_flag | 🇨🇳 | cn |
| 🇨🇴 | co | 🇰🇲 | km |
| 🇨🇩 | cd_flag | 🇨🇬 | cg |
| 🇨🇰 | ck | 🇨🇷 | cr |
| 🇭🇷 | hr | 🇨🇺 | cu |
| 🇨🇼 | cw | 🇨🇾 | cy |
| 🇨🇿 | cz | 🇩🇰 | dk |
| 🇩🇯 | dj | 🇩🇲 | dm |
| 🇩🇴 | do | 🇪🇨 | ec |
| 🇪🇬 | eg | 🇸🇻 | sv |
| 🇬🇶 | gq | 🇪🇷 | er |
| 🇪🇪 | ee | 🇪🇹 | et |
| 🇫🇴 | fo | 🇫🇯 | fj |
| 🇫🇮 | fi | 🇫🇷 | fr |
| 🇬🇫 | gf | 🇹🇫 | tf |
| 🇬🇦 | ga | 🇬🇲 | gm |
| 🇬🇪 | ge | 🇩🇪 | de |
| 🇬🇭 | gh | 🇬🇮 | gi |
| 🇬🇷 | gr | 🇬🇩 | gd |
| 🇬🇵 | gp | 🇬🇺 | gu |
| 🇬🇹 | gt | 🇬🇳 | gn |
| 🇬🇼 | gw | 🇬🇾 | gy |
| 🇭🇹 | ht | 🇭🇳 | hn |
| 🇭🇰 | hk | 🇭🇺 | hu |
| 🇮🇸 | is | 🇮🇳 | in |
| 🇮🇩 | id_flag | 🇮🇷 | ir |
| 🇮🇶 | iq | 🇮🇪 | ie |
| 🇮🇱 | il | 🇮🇹 | it |
| 🇨🇮 | ci | 🇯🇲 | jm |
| 🇯🇵 | jp | 🇯🇴 | jo |
| 🇰🇿 | kz | 🇰🇪 | ke |
| 🇰🇮 | ki | 🇰🇼 | kw |
| 🇰🇬 | kg | 🇱🇦 | la |
| 🇱🇻 | lv | 🇱🇧 | lb |
| 🇱🇸 | ls | 🇱🇷 | lr |
| 🇱🇾 | ly | 🇱🇮 | li |
| 🇱🇹 | lt | 🇱🇺 | lu |
| 🇲🇴 | mo | 🇲🇰 | mk |
| 🇲🇬 | mg | 🇲🇼 | mw |
| 🇲🇾 | my | 🇲🇻 | mv |
| 🇲🇱 | ml | 🇲🇹 | mt |
| 🇲🇶 | mq | 🇲🇷 | mr |
| 🇲🇽 | mx | 🇲🇩 | md |
| 🇲🇳 | mn | 🇲🇪 | me |
| 🇲🇸 | ms | 🇲🇦 | ma |
| 🇲🇿 | mz | 🇲🇲 | mm |
| 🇳🇦 | na | 🇳🇵 | np |
| 🇳🇱 | nl | 🇳🇨 | nc |
| 🇳🇿 | nz | 🇳🇮 | ni |
| 🇳🇪 | ne | 🇳🇬 | ng |
| 🇳🇺 | nu | 🇰🇵 | kp |
| 🇲🇵 | mp | 🇳🇴 | no |
| 🇴🇲 | om | 🇵🇰 | pk |
| 🇵🇼 | pw | 🇵🇸 | ps |
| 🇵🇦 | pa | 🇵🇬 | pg |
| 🇵🇾 | py | 🇵🇪 | pe |
| 🇵🇭 | ph | 🇵🇱 | pl |
| 🇵🇹 | pt | 🇵🇷 | pr |
| 🇶🇦 | qa | 🇷🇪 | re |
| 🇷🇴 | ro | 🇷🇺 | ru |
| 🇷🇼 | rw | 🇼🇸 | ws |
| 🇸🇲 | sm | 🇸🇹 | st |
| 🇸🇦 | sa_flag | 🇸🇳 | sn |
| 🇷🇸 | rs | 🇸🇨 | sc |
| 🇸🇱 | sl | 🇸🇬 | sg |
| 🇸🇰 | sk | 🇸🇮 | si |
| 🇸🇧 | sb | 🇸🇴 | so |
| 🇿🇦 | za | 🇰🇷 | kr |
| 🇸🇸 | ss | 🇪🇸 | es |
| 🇱🇰 | lk | 🇸🇩 | sd |
| 🇸🇷 | sr | 🇸🇿 | sz |
| 🇸🇪 | se | 🇨🇭 | ch |
| 🇸🇾 | sy | 🇹🇯 | tj |
| 🇹🇿 | tz | 🇹🇭 | th |
| 🇹🇱 | tl | 🇹🇬 | tg |
| 🇹🇴 | to | 🇹🇹 | tt |
| 🇹🇳 | tn | 🇹🇷 | tr |
| 🇹🇲 | tm_flag | 🇹🇨 | tc |
| 🇹🇻 | tv_flag | 🇺🇬 | ug |
| 🇺🇦 | ua | 🇦🇪 | ae |
| 🇬🇧 | gb | 🇺🇾 | uy |
| 🇺🇸 | us | 🇻🇮 | vi |
| 🇺🇿 | uz | 🇻🇨 | vc |
| 🇻🇺 | vu | 🇻🇪 | ve |
| 🇻🇳 | vn | 🇾🇪 | ye |
| 🇿🇲 | zm | 🇿🇼 | zw |
| 🇦 | regional_indicator_symbol_a | 🇧 | regional_indicator_symbol_b |
| 🇨 | regional_indicator_symbol_c | 🇩 | regional_indicator_symbol_d |
| 🇪 | regional_indicator_symbol_e | 🇫 | regional_indicator_symbol_f |
| 🇬 | regional_indicator_symbol_g | 🇭 | regional_indicator_symbol_h |
| 🇮 | regional_indicator_symbol_i | 🇯 | regional_indicator_symbol_j |
| 🇰 | regional_indicator_symbol_k | 🇱 | regional_indicator_symbol_l |
| 🇲 | regional_indicator_symbol_m | 🇳 | regional_indicator_symbol_n |
| 🇴 | regional_indicator_symbol_o | 🇵 | regional_indicator_symbol_p |
| 🇶 | regional_indicator_symbol_q | 🇷 | regional_indicator_symbol_r |
| 🇸 | regional_indicator_symbol_s | 🇹 | regional_indicator_symbol_t |
| 🇺 | regional_indicator_symbol_u | 🇻 | regional_indicator_symbol_v |
| 🇼 | regional_indicator_symbol_w | 🇽 | regional_indicator_symbol_x |
| 🇾 | regional_indicator_symbol_y | 🇿 | regional_indicator_symbol_z |
| 👨‍👩‍👦 | family_man_woman_boy | 👨‍👩‍👧 | family_man_woman_girl |
| 👨‍👩‍👦‍👦 | family_man_woman_boy_boy | 👨‍👩‍👧‍👧 | family_man_woman_girl_girl |
| 👨‍👩‍👧‍👦 | family_man_woman_girl_boy | 👩‍👩‍👦 | family_woman_woman_boy |
| 👩‍👩‍👧 | family_woman_woman_girl | 👩‍👩‍👧‍👦 | family_woman_woman_girl_boy |
| 👩‍👩‍👦‍👦 | family_woman_woman_boy_boy | 👩‍👩‍👧‍👧 | family_woman_woman_girl_girl |
| 👨‍👨‍👦 | family_man_man_boy | 👨‍👨‍👧 | family_man_man_girl |
| 👨‍👨‍👧‍👦 | family_man_man_girl_boy | 👨‍👨‍👦‍👦 | family_man_man_boy_boy |
| 👨‍👨‍👧‍👧 | family_man_man_girl_girl | 👩‍❤‍👩 | couple_with_heart_woman_woman |
| 👨‍❤‍👨 | couple_with_heart_man_man | 👩‍❤️‍💋‍👩 | couplekiss_woman_woman |
| ‍👨❤️💋‍👨 | couplekiss_man_man | 🖖 | vulcan_salute |
| 🖕 | middle_finger | 🙂 | slightly_smiling, slight_smile |
| 🤗 | hugging, hug, hugs | 🤔 | thinking, think, thinker |
| 🙄 | eye_roll, rolling_eyes | 🤐 | zipper_mouth, zip_it, sealed_lips, lips_sealed |
| 🤓 | nerd, nerdy | ☹ | frowning_face |
| 🙁 | slightly_frowning | 🙃 | upside_down, flipped_face |
| 🤒 | sick, ill, thermometer_face | 🤕 | injured, head_bandage, head_bandaged, bandaged |
| 🤑 | money_mouth, money_face | ⛑ | helmet_white_cross |
| 🕵 | detective, sleuth, private_eye, spy | 🗣 | speaking_head_in_silhouette |
| 🕴 | hovering_man, levitating_man | 🤘 | horns_sign, rock_on, heavy_metal, devil_fingers |
| 🖐 | raised_hand_with_fingers_splayed, splayed_hand | ✍ | writing, writing_hand |
| 👁 | eye | ❣ | exclamation_heart |
| 🕳 | hole | 🗯 | right_anger_bubble, zig_zag_bubble |
| 🕶 | dark_sunglasses | 🛍 | shopping_bags |
| 📿 | prayer_beads, dhikr_beads, rosary_beads | ☠ | skull_crossbones |
| 🤖 | robot_face, bot_face | 🦁 | lion_face, cute_lion, timid_lion |
| 🦄 | unicorn_face | 🐿 | chipmunk, squirrel |
| 🦃 | turkey | 🕊 | dove, dove_peace |
| 🦀 | crab | 🕷 | spider |
| 🕸 | spider_web, cobweb | 🦂 | scorpion |
| 🏵 | rosette | ☘ | shamrock, st_patrick |
| 🌶 | hot_pepper, chili_pepper, spice, spicy | 🧀 | cheese |
| 🌭 | hot_dog | 🌮 | taco |
| 🌯 | burrito, wrap | 🍿 | popcorn |
| 🍾 | champagne, sparkling_wine | 🍽 | fork_knife_plate |
| 🏺 | amphora, jar, vase | 🗺 | world_map |
| 🏔 | snow_capped_mountain, mont_fuji | ⛰ | mountain |
| 🏕 | camping, campsite | 🏖 | breach |
| 🏜 | desert | 🏝 | desert_island |
| 🏞 | national_park | 🏟 | stadium |
| 🏛 | classical_building | 🏗 | building_construction, crane |
| 🏘 | house_buildings, multiple_houses | 🏙 | cityscape |
| 🏚 | derelict_house, old_house, abandoned_house | 🛐 | worship_building, worship_place, religious_building, religious_place |
| 🕋 | kaaba, mecca | 🕌 | mosque, minaret, domed_roof |
| 🕍 | synagogue, temple, jewish | 🖼 | picture_frame, painting, gallery |
| 🛢 | oil_drum | 🛣 | motorway, highway, road, interstate, freeway |
| 🛤 | railway_track | 🛳 | passenger_ship |
| ⛴ | ferry | 🛥 | motor_boat |
| 🛩 | small_airplane | 🛫 | airplane_departure, take_off |
| 🛬 | airplane_arriving, airplane_arrival, landing | 🛰 | satellite |
| 🛎 | bellhop_bell | 🛌 | sleeping_accommodation |
| 🛏 | bed, bedroom | 🛋 | couch_lamp, couch, sofa, lounge |
| ⏱ | stopwatch | ⏲ | timer_clock |
| 🕰 | mantelpiece_clock | 🌡 | thermometer, hot_weather, temperature |
| ⛈ | thunder_cloud_rain | 🌤 | white_sun_small_cloud |
| 🌥 | white_sun_behind_cloud | 🌦 | white_sun_behind_cloud_rain |
| 🌧 | cloud_rain | 🌨 | cloud_snow |
| 🌩 | cloud_lightning | 🌪 | cloud_tornado |
| 🌫 | fog | 🌬 | wind_blowing_face, mother_nature, blowing_wind |
| ☂ | open_umbrella | ⛱ | planted_umbrella, umbrella_on_ground |
| ☃ | snowman_with_snow, snowing_snowman | ☄ | comet, light_beam, blue_beam |
| 🕎 | menorah, candelabrum, chanukiah | 🎖 | military_medal, military_decoration |
| 🎗 | reminder_ribbon, awareness_ribbon | 🎞 | film_frames |
| 🎟 | admission_ticket | 🏷 | label |
| 🏌 | golfer, golf_club | 🏌♂️ | man_golfer, male_golfer, man_golfing, male_golfing |
| 🏌‍♀️ | woman_golfer, female_golfer, woman_golfing, female_golfing | ⛸ | ice_skate, ice_skating |
| ⛷ | skier | ⛹ | person_with_ball |
| 🏋 | weight_lifter | 🏎 | racing_car, formula_one, f1 |
| 🏍 | racing_motorcycle, motorcycle, motorbike | 🏅 | sports_medal, sports_decoration |
| 🏏 | cricket | 🏐 | volleyball |
| 🏑 | field_hockey | 🏒 | ice_hockey |
| 🏓 | table_tennis, ping_pong | 🏸 | badminton |
| 🕹 | joystick | ⏭ | black_right_pointing_double_triangle_with_vertical_bar |
| ⏯ | black_right_pointing_triangle_with_double_vertical_bar | ⏮ | black_left_pointing_double_triangle_with_vertical_bar |
| ⏸ | double_vertical_bar | ⏹ | black_square_for_stop |
| ⏺ | black_circle_for_record | 🎙 | studio_microphone |
| 🎚 | level_slider | 🎛 | control_knobs |
| *⃣ | keycap_asterisk, star_keycap | 🖥 | desktop_computer, pc_tower, imac |
| 🖨 | printer | ⌨ | keyboard |
| 🖱 | computer_mouse, three_button_mouse | 🖲 | trackball |
| 📽 | film_projector | 📸 | camera_flash |
| 🕯 | candle | 🗞 | rolled_up_newspaper, newspaper_delivery |
| 🗳 | ballot, ballot_box | 🖋 | lower_left_fountain_pen |
| 🖊 | lower_left_ballpoint_pen | 🖌 | lower_left_paintbrush |
| 🖍 | lower_left_crayon | 🗂 | card_index_dividers |
| 🗒 | spiral_note_pad | 🗓 | spiral_calendar_pad |
| 🖇 | linked_paperclips | 🗃 | card_file_box |
| 🗄 | file_cabinet | 🗑 | wastebasket |
| 🗝 | old_key | ⛏ | pick |
| ⚒ | hammer_and_pick | 🛠 | hammer_and_wrench |
| ⚙ | gear | 🗜 | compression |
| ⚗ | alembic | ⚖ | scales, scales_of_justice |
| ⛓ | chains | 🗡 | dagger, dagger_knife, knife_weapon |
| ⚔ | crossed_swords | 🛡 | shield |
| 🏹 | bow_and_arrow, bow_arrow, archery | ⚰ | coffin, funeral, casket |
| ⚱ | funeral_urn | 🏳 | waving_white_flag |
| 🏴 | waving_black_flag | ⚜ | fleur_de_lis, scouts |
| ⚛ | atom, atom_symbol | 🕉 | om_symbol, pranava, aumkara, omkara |
| ✡ | star_of_david | ☸ | wheel_of_dharma |
| ☯ | yin_yang | ✝ | latin_cross, christian_cross |
| ☦ | orthodox_cross | ⛩ | shinto_shrine, kami_no_michi |
| ☪ | star_and_crescent, star_crescent | ☮ | peace_symbol, peace_sign |
| ☢ | radioactive, radioactive_symbol, radioactive_sign | ☣ | biohazard, biohazard_symbol, biohazard_sign |
| 🗨 | left_speech_bubble | 👁‍🗨 | eye_in_speech_bubble, i_am_a_witness |
| 🤣 | rolling_on_the_floor_laughing, rofl | 🤠 | face_with_cowboy_hat, cowboy |
| 🤡 | clown_face, clown | 🤥 | lying_face |
| 🤤 | drooling_face | 🤢 | nauseated_face |
| 🤧 | sneezing_face | 🤴 | prince |
| 🤶 | mother_christmas | 🤵 | man_in_tuxedo |
| 🤷 | shrug | 🤦 | face_palm |
| 🤰 | pregnant_woman | 🕺 | man_dancing |
| 🤳 | selfie | 🤞 | hand_with_index_and_middle_fingers_crossed |
| 🤙 | call_me_hand | 🤛 | left-facing_fist |
| 🤜 | right-facing_fist | 🤚 | raised_back_of_hand |
| 🤝 | handshake | 🖤 | black_heart |
| 🦍 | gorilla | 🦊 | fox_face |
| 🦌 | deer | 🦏 | rhinoceros |
| 🦇 | bat | 🦅 | eagle |
| 🦆 | duck | 🦉 | owl |
| 🦎 | lizard | 🦈 | shark |
| 🦐 | shrimp | 🦑 | squid |
| 🦋 | butterfly | 🥀 | wilted_flower |
| 🥝 | kiwifruit | 🥑 | avocado |
| 🥔 | potato | 🥕 | carrot |
| 🥒 | cucumber | 🥜 | peanuts |
| 🥐 | croissant | 🥖 | baguette_bread |
| 🥞 | pancakes | 🥓 | bacon |
| 🥙 | stuffed_flatbread | 🥚 | egg |
| 🥘 | shallow_pan_of_food | 🥗 | green_salad |
| 🥛 | glass_of_milk | 🥂 | clinking_glasses |
| 🥃 | tumbler_glass | 🥄 | spoon |
| 🛑 | octagonal_sign, stop_sign | 🛴 | scooter |
| 🛵 | motor_scooter | 🛶 | canoe |
| 🥇 | first_place_medal | 🥈 | second_place_medal |
| 🥉 | third_place_medal | 🥊 | boxing_glove |
| 🥋 | martial_arts_uniform | 🤸 | person_doing_cartwheel |
| 🤸‍♂️ | man_doing_cartwheel, male_doing_cartwheel | 🤸‍♀️ | woman_doing_cartwheel, female_doing_cartwheel |
| 🤼 | wrestlers | 🤼‍♂️ | man_wrestlers, male_wrestlers |
| 🤼‍♀️ | woman_wrestlers, female_wrestlers | 🤽 | water_polo |
| 🤽‍♂️ | man_water_polo, male_water_polo | 🤽‍♀️ | woman_water_polo, female_water_polo |
| 🤾 | handball | 🤾‍♂️ | man_handball, male_handball |
| 🤾‍♀️ | woman_handball, female_handball | 🤺 | fencer |
| 🥅 | goal_net | 🤹 | juggling |
| 🤹‍♂️ | man_juggling, male_juggling | 🤹‍♀️ | woman_juggling, female_juggling |
| 🥁 | drum_with_drumsticks | 🛒 | shopping_trolley, shopping_cart |
| 👨‍⚕️ | male_health_worker, man_health_worker | 👩‍⚕️ | female_health_worker, woman_health_worker |
| 👨‍🎓 | male_student, man_student | 👩‍🎓 | female_student, woman_student |
| 👨‍🏫 | male_teacher, man_teacher | 👩‍🏫 | female_teacher, woman_teacher |
| 👨‍🌾 | male_farmer, man_farmer | 👩‍🌾 | female_farmer, woman_farmer |
| 👨‍🍳 | male_cook, man_cook | 👩‍🍳 | female_cook, woman_cook |
| 👨‍🔧 | male_mechanic, man_mechanic | 👩‍🔧 | female_mechanic, woman_mechanic |
| 👨‍🏭 | male_factory_worker, man_factory_worker | 👩‍🏭 | female_factory_worker, woman_factory_worker |
| 👨‍💼 | male_office_worker, man_office_worker | 👩‍💼 | female_office_worker, woman_office_worker |
| 👨‍🔬 | male_scientist, man_scientist | 👩‍🔬 | female_scientist, woman_scientist |
| 👨‍💻 | male_technologist, man_technologist | 👩‍💻 | female_technologist, woman_technologist |
| 👨‍🎤 | male_singer, man_singer | 👩‍🎤 | female_singer, woman_singer |
| 👨‍🎨 | male_artist, man_artist | 👩‍🎨 | female_artist, woman_artist |
| 👨‍✈️ | male_pilot, man_pilot | 👩‍✈️ | female_pilot, woman_pilot |
| 👨‍🚀 | male_astronaut, man_astronaut | 👩‍🚀 | female_astronaut, woman_astronaut |
| 👨‍🚒 | male_firefighter, man_firefighter | 👩‍🚒 | female_firefighter, woman_firefighter |
| 🤦‍♀️ | female_facepalm, woman_facepalm | 🤷‍♂️ | male_shrug, man_shrug |
| 🤷‍♀️ | female_shrug, woman_shrug | ⚕️ | medical_symbol, staff_of_aesculapius |
| 👨‍⚖️ | man_judge, male_judge | 👩‍⚖️ | woman_judge, female_judge |
