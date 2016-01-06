# emoji-java

[![Build Status](https://travis-ci.org/vdurmont/emoji-java.svg?branch=master)](https://travis-ci.org/vdurmont/emoji-java)
[![Coverage Status](https://img.shields.io/coveralls/vdurmont/emoji-java.svg)](https://coveralls.io/r/vdurmont/emoji-java?branch=master)
[![Dependencies.co — Track your dependencies!](http://api.dependencies.co/projects/github/vdurmont/emoji-java/badge.svg?cache_buster=1443666302294)](http://dependencies.co/projects/github/vdurmont/emoji-java)
[![License Info](http://img.shields.io/badge/license-The%20MIT%20License-brightgreen.svg)](https://github.com/vdurmont/emoji-java/blob/master/LICENSE.md)

*The missing emoji library for java.*

**emoji-java** is a lightweight java library that helps you use Emojis in your java applications.

## How to get it?

##### Via Maven:
```xml
<dependency>
  <groupId>com.vdurmont</groupId>
  <artifactId>emoji-java</artifactId>
  <version>2.2.1</version>
</dependency>
```

You can also download the project, build it with `mvn clean install` and add the generated jar to your buildpath.

##### Via Gradle:
```gradle
compile 'com.vdurmont:emoji-java:2.2.1'
```

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
| 😦 | frowning | 📼 | vhs |
| 🍷 | wine_glass | 🙎 | person_with_pouting_face |
| 🍳 | egg | 🎪 | circus_tent |
| 🎷 | saxophone | 😸 | smile_cat |
| 🍵 | tea | 🐼 | panda_face |
| ⤵️ | arrow_heading_down | 💩 | hankey, poop, shit |
| 🐰 | rabbit | 🚤 | speedboat |
| 🛃 | customs | 🍶 | sake |
| 🐏 | ram | 📪 | mailbox_closed |
| 😎 | sunglasses | 🎲 | game_die |
| 🐀 | rat | 👨👩👧👧 | family_man_woman_girl_girl |
| 💮 | white_flower | 🐄 | cow2 |
| 🍗 | poultry_leg | 👤 | bust_in_silhouette |
| 😉 | wink | 🔴 | red_circle |
| 🎽 | running_shirt_with_sash | 📰 | newspaper |
| 📇 | card_index | 😫 | tired_face |
| 👠 | high_heel | 🌂 | closed_umbrella |
| 🔃 | arrows_clockwise | 💵 | dollar |
| 🚝 | monorail | 🐖 | pig2 |
| ⛳ | golf | 😅 | sweat_smile |
| 🔢 | 1234 | 🈴 | u5408 |
| ⛅ | partly_sunny | 🚯 | do_not_litter |
| 😯 | hushed | ✋ | hand, raised_hand |
| 🙈 | see_no_evil | 🅰️ | a |
| ☝️ | point_up | 🅱️ | b |
| 🔤 | abc | 🀄 | mahjong |
| 🚭 | no_smoking | 🍫 | chocolate_bar |
| 🎥 | movie_camera | Ⓜ️ | m |
| ⭕ | o | 😻 | heart_eyes_cat |
| 🔆 | high_brightness | 👄 | lips |
| 👜 | handbag | 🌝 | full_moon_with_face |
| ✌️ | v | 📲 | calling |
| ⚪ | white_circle | ❌ | x |
| 📊 | bar_chart | 🐁 | mouse2 |
| 9️⃣ | nine | 👫 | couple |
| 📹 | video_camera | 🎵 | musical_note |
| 🎶 | notes | 👯 | dancers |
| 📥 | inbox_tray | ‼️ | bangbang |
| 💽 | minidisc | 🚛 | articulated_lorry |
| 😊 | blush | 🎤 | microphone |
| 🍈 | melon | ➗ | heavy_division_sign |
| ✋ | hand, raised_hand | 🗽 | statue_of_liberty |
| 💩 | hankey, poop, shit | ❕ | grey_exclamation |
| 📙 | orange_book | 🌔 | moon, waxing_gibbous_moon |
| ℹ️ | information_source | 🐚 | shell |
| 👩👩👧👧 | family_woman_woman_girl_girl | 👷 | construction_worker |
| 📴 | mobile_phone_off | 😺 | smiley_cat |
| 🔊 | loud_sound | 😴 | sleeping |
| 👖 | jeans | 🍐 | pear |
| 🚓 | police_car | 🚏 | busstop |
| 🚙 | blue_car | 🔚 | end |
| 📡 | satellite | 🐋 | whale2 |
| 🎐 | wind_chime | 🔂 | repeat_one |
| 🎫 | ticket | ☔ | umbrella |
| 🍚 | rice | ✂️ | scissors |
| 🈷️ | u6708 | ♒ | aquarius |
| 🈶 | u6709 | ☕ | coffee |
| ✈️ | airplane | 😓 | sweat |
| ♋ | cancer | 👪 | family |
| 😘 | kissing_heart | 🎁 | gift |
| ↕️ | arrow_up_down | ⛲ | fountain |
| ©️ | copyright | 🌒 | waxing_crescent_moon |
| 💟 | heart_decoration | 💊 | pill |
| 🍢 | oden | 🐗 | boar |
| 🐫 | camel | 📧 | e-mail |
| ◻️ | white_medium_square | 🍊 | tangerine |
| 💡 | bulb | 👲 | man_with_gua_pi_mao |
| 🐆 | leopard | 🌎 | earth_americas |
| 📫 | mailbox | 🍏 | green_apple |
| 💤 | zzz | 💠 | diamond_shape_with_a_dot_inside |
| #️⃣ | hash | 🔫 | gun |
| 😑 | expressionless | 🚩 | triangular_flag_on_post |
| 👩 | woman | 🚠 | mountain_cableway |
| 🐠 | tropical_fish | 🍩 | doughnut |
| 😒 | unamused | ⚫ | black_circle |
| 👙 | bikini | 🔝 | top |
| 🚟 | suspension_railway | ⬛ | black_large_square |
| 🙋 | raising_hand | 🚻 | restroom |
| ❄️ | snowflake | 🎩 | tophat |
| ↙️ | arrow_lower_left | ❓ | question |
| 🚺 | womens | 😽 | kissing_cat |
| ⛵ | boat, sailboat | 💳 | credit_card |
| 🎱 | 8ball | 🍘 | rice_cracker |
| ⚓ | anchor | 🌹 | rose |
| ♠️ | spades | 🔷 | large_blue_diamond |
| 👰 | bride_with_veil | 🕣 | clock830 |
| 👮 | cop | 📮 | postbox |
| 🍖 | meat_on_bone | 🐮 | cow |
| 🐲 | dragon_face | 🙏 | pray |
| 😐 | neutral_face | 🐷 | pig |
| 🔘 | radio_button | 📟 | pager |
| ♨️ | hotsprings | 🎡 | ferris_wheel |
| 🌳 | deciduous_tree | ↩️ | leftwards_arrow_with_hook |
| 🏄 | surfer | 🆕 | new |
| ◾ | black_medium_small_square | 🎋 | tanabata_tree |
| 👨❤️💋👨 | couplekiss_man_man | 💭 | thought_balloon |
| 🐎 | racehorse | 🙅 | no_good |
| 🚐 | minibus | 🕦 | clock1130 |
| 🎠 | carousel_horse | ◼️ | black_medium_square |
| 🐔 | chicken | 📳 | vibration_mode |
| ◽ | white_medium_small_square | 🏃 | runner, running |
| 🐞 | beetle | 🍱 | bento |
| ⛎ | ophiuchus | 😝 | stuck_out_tongue_closed_eyes |
| 🎴 | flower_playing_cards | 💪 | muscle |
| 🚧 | construction | 💀 | skull |
| 👍 | +1, thumbsup | 😙 | kissing_smiling_eyes |
| 💙 | blue_heart | 🌌 | milky_way |
| 🌼 | blossom | 🐜 | ant |
| 🎑 | rice_scene | 📬 | mailbox_with_mail |
| 😢 | cry | 📝 | memo, pencil |
| 🈯 | u6307 | 🍍 | pineapple |
| 🈁 | koko | 🎼 | musical_score |
| 💧 | droplet | 💬 | speech_balloon |
| 🈚 | u7121 | 💍 | ring |
| 🐙 | octopus | 🗾 | japan |
| 🕡 | clock630 | 🚢 | ship |
| 📛 | name_badge | 🌜 | last_quarter_moon_with_face |
| 🔏 | lock_with_ink_pen | 🍰 | cake |
| ♌ | leo | ♻️ | recycle |
| ↪️ | arrow_right_hook | 🚍 | oncoming_bus |
| ♏ | scorpius | 🔇 | mute |
| 🔌 | electric_plug | 🏊 | swimmer |
| ❎ | negative_squared_cross_mark | 😶 | no_mouth |
| 👣 | footprints | 🎍 | bamboo |
| 🚽 | toilet | ✉️ | email, envelope |
| 🌛 | first_quarter_moon_with_face | 🔭 | telescope |
| 🏬 | department_store | 🌅 | sunrise |
| 🌉 | bridge_at_night | 🔋 | battery |
| 2️⃣ | two | 🚁 | helicopter |
| 😧 | anguished | 🚜 | tractor |
| 🉐 | ideograph_advantage | 🃏 | black_joker |
| 🐕 | dog2 | 😟 | worried |
| 🍉 | watermelon | 🏆 | trophy |
| 🔦 | flashlight | 👩👩👧👦 | family_woman_woman_girl_boy |
| 🌴 | palm_tree | 😤 | triumph |
| 🎨 | art | 🔞 | underage |
| 👡 | sandal | 🚸 | children_crossing |
| 😛 | stuck_out_tongue | 🙊 | speak_no_evil |
| 💘 | cupid | 🚦 | vertical_traffic_light |
| 💩 | hankey, poop, shit | 👕 | shirt, tshirt |
| ⌚ | watch | 👏 | clap |
| ➡️ | arrow_right | 💹 | chart |
| 🕟 | clock430 | 👇 | point_down |
| 🎄 | christmas_tree | 💚 | green_heart |
| 💣 | bomb | 👨👨👧👧 | family_man_man_girl_girl |
| 🐭 | mouse | 😞 | disappointed |
| 🐣 | hatching_chick | 🏫 | school |
| 🈵 | u6e80 | ♉ | taurus |
| ⬅️ | arrow_left | 🏧 | atm |
| 🍀 | four_leaf_clover | 🔓 | unlock |
| 💌 | love_letter | 🌟 | star2 |
| ☎️ | phone, telephone | 👧 | girl |
| 👒 | womans_hat | 🚃 | railway_car |
| 🔖 | bookmark | 🍦 | icecream |
| 🎺 | trumpet | 🎮 | video_game |
| 🐊 | crocodile | 💥 | boom, collision |
| 🐇 | rabbit2 | 📖 | book, open_book |
| 🚚 | truck | 📈 | chart_with_upwards_trend |
| 💄 | lipstick | 🏠 | house |
| 👋 | wave | 👨👨👦 | family_man_man_boy |
| 🍕 | pizza | 🍸 | cocktail |
| 🌺 | hibiscus | 👢 | boot |
| 👞 | mans_shoe, shoe | 🎣 | fishing_pole_and_fish |
| 💴 | yen | 🔱 | trident |
| 🎰 | slot_machine | 🌠 | stars |
| 😰 | cold_sweat | 🕒 | clock3 |
| 🕑 | clock2 | ◀️ | arrow_backward |
| 🕔 | clock5 | 👺 | japanese_goblin |
| 🕓 | clock4 | 🕐 | clock1 |
| 🔟 | keycap_ten | ▶️ | arrow_forward |
| 🕖 | clock7 | 🕕 | clock6 |
| 🕘 | clock9 | 🕗 | clock8 |
| 🅿️ | parking | 🕝 | clock230 |
| 🙉 | hear_no_evil | 💓 | heartbeat |
| ⬜ | white_large_square | 3️⃣ | three |
| 💗 | heartpulse | 💁 | information_desk_person |
| 👸 | princess | 📠 | fax |
| 🚊 | tram | 🚑 | ambulance |
| ✅ | white_check_mark | 🐈 | cat2 |
| 🌔 | moon, waxing_gibbous_moon | ↘️ | arrow_lower_right |
| ✔️ | heavy_check_mark | 🍜 | ramen |
| ☎️ | phone, telephone | 🚇 | metro |
| 🐃 | water_buffalo | 👹 | japanese_ogre |
| 👔 | necktie | 🎎 | dolls |
| 🐵 | monkey_face | 🔠 | capital_abcd |
| 🎏 | flags | 😔 | pensive |
| 👾 | space_invader | 🍭 | lollipop |
| 🚆 | train2 | 👽 | alien |
| 📁 | file_folder | 💻 | computer |
| 🍥 | fish_cake | ↔️ | left_right_arrow |
| 🍟 | fries | 🛅 | left_luggage |
| 😌 | relieved | 😁 | grin |
| 📒 | ledger | 👬 | two_men_holding_hands |
| ☑️ | ballot_box_with_check | 🍓 | strawberry |
| ⤴️ | arrow_heading_up | 🎾 | tennis |
| 👨👩👦👦 | family_man_woman_boy_boy | 👕 | shirt, tshirt |
| 😡 | rage | 🌘 | waning_crescent_moon |
| 😹 | joy_cat | 😣 | persevere |
| 😿 | crying_cat_face | 🚪 | door |
| ♦️ | diamonds | 🐒 | monkey |
| 🌀 | cyclone | 👨👨👦👦 | family_man_man_boy_boy |
| ☁️ | cloud | 💱 | currency_exchange |
| 🗻 | mount_fuji | 🚖 | oncoming_taxi |
| 🐳 | whale | ✏️ | pencil2 |
| 🐻 | bear | 🎉 | tada |
| 🍌 | banana | 〰️ | wavy_dash |
| 🎻 | violin | 📦 | package |
| 📂 | open_file_folder | 👊 | facepunch, punch |
| ↖️ | arrow_upper_left | 😍 | heart_eyes |
| 🌈 | rainbow | ➖ | heavy_minus_sign |
| 📢 | loudspeaker | 🌍 | earth_africa |
| ⛺ | tent | 👨👨👧👦 | family_man_man_girl_boy |
| 🔬 | microscope | 🚲 | bike |
| ☺️ | relaxed | 😂 | joy |
| ⏫ | arrow_double_up | ✳️ | eight_spoked_asterisk |
| 🏥 | hospital | 🍯 | honey_pot |
| 🐑 | sheep | ⏳ | hourglass_flowing_sand |
| ⚽ | soccer | 📉 | chart_with_downwards_trend |
| 🌚 | new_moon_with_face | 🌖 | waning_gibbous_moon |
| 🚒 | fire_engine | 🌏 | earth_asia |
| 📕 | closed_book | 😇 | innocent |
| 💋 | kiss | 👨❤️👨 | couple_with_heart_man_man |
| 😳 | flushed | 🍔 | hamburger |
| 🚴 | bicyclist | ⌛ | hourglass |
| 🚥 | traffic_light | 🍮 | custard |
| 🎦 | cinema | 🚔 | oncoming_police_car |
| 🍺 | beer | 😃 | smiley |
| 🆎 | ab | 🇦🇩 | ad |
| 🇦🇪 | ae | 🇦🇫 | af |
| 🇦🇬 | ag | 🍑 | peach |
| 🏮 | izakaya_lantern, lantern | 🇦🇮 | ai |
| 💑 | couple_with_heart | 🚱 | non-potable_water |
| 😠 | angry | 🇦🇱 | al |
| 🇦🇲 | am | 🇦🇴 | ao |
| 📍 | round_pushpin | 🇦🇷 | ar |
| 🇦🇸 | as | 🇦🇹 | at |
| 🇦🇺 | au | 🇦🇼 | aw |
| 🔐 | closed_lock_with_key | 🇦🇿 | az |
| 💖 | sparkling_heart | 🇧🇦 | ba |
| 🇧🇧 | bb | 📣 | mega |
| 🇧🇩 | bd | 🍝 | spaghetti |
| 🇧🇪 | be | 🇧🇫 | bf |
| 🇧🇬 | bg | 🇧🇭 | bh |
| 🚡 | aerial_tramway | 🇧🇮 | bi |
| 💔 | broken_heart | 🇧🇯 | bj |
| 🐥 | hatched_chick | 🍅 | tomato |
| 🇧🇲 | bm | 🇧🇳 | bn |
| 👀 | eyes | 🇧🇴 | bo |
| 🇧🇷 | br | 🇧🇸 | bs |
| 🇧🇹 | bt | 🌑 | new_moon |
| 🇧🇼 | bw | 🇧🇾 | by |
| 🇧🇿 | bz | 🏨 | hotel |
| 🛄 | baggage_claim | 🌋 | volcano |
| 💒 | wedding | 🏡 | house_with_garden |
| 🇨🇦 | ca | 🇨🇩 | cd |
| 🇨🇫 | cf | 🇨🇬 | cg |
| 🇨🇭 | ch | 🇨🇮 | ci |
| 🇨🇰 | ck | 🇨🇱 | cl |
| 🇨🇲 | cm | 🇨🇳 | cn |
| 🌰 | chestnut | 🇨🇴 | co |
| 🐪 | dromedary_camel | 🌻 | sunflower |
| 🔡 | abcd | 🇨🇰 | cr |
| 🐡 | blowfish | 👨👨👧 | family_man_man_girl |
| 🇨🇺 | cu | 🇨🇻 | cv |
| 🇨🇼 | cw | 🇨🇾 | cy |
| 👨👩👦 | family_man_woman_boy | 🇨🇿 | cz |
| 🎃 | jack_o_lantern | ▪️ | black_small_square |
| 😲 | astonished | 👛 | purse |
| 🐧 | penguin | 🇩🇪 | de |
| 🐝 | bee, honeybee | 👩👩👧 | family_woman_woman_girl |
| ↗️ | arrow_upper_right | 🇩🇯 | dj |
| 🚵 | mountain_bicyclist | 🇩🇰 | dk |
| 🖕 | middle_finger | 🇩🇲 | dm |
| ♈ | aries | 🇩🇴 | do |
| 👵 | older_woman | 🎳 | bowling |
| 😚 | kissing_closed_eyes | 👥 | busts_in_silhouette |
| 🔰 | beginner | 🇩🇿 | dz |
| 🐦 | bird | ⚠️ | warning |
| 🔲 | black_square_button | 🇪🇨 | ec |
| 🚋 | train | 🏪 | convenience_store |
| 🇪🇪 | ee | 🐬 | dolphin, flipper |
| 🔳 | white_square_button | 🇪🇬 | eg |
| 📏 | straight_ruler | 🚫 | no_entry_sign |
| 🎀 | ribbon | 🎓 | mortar_board |
| 🇪🇷 | er | 🇪🇸 | es |
| 🇪🇹 | et | ♑ | capricorn |
| 🍼 | baby_bottle | 🚣 | rowboat |
| 🔪 | hocho, knife | 🌆 | city_sunset |
| 🐅 | tiger2 | 🚂 | steam_locomotive |
| 🔩 | nut_and_bolt | 🐘 | elephant |
| 💜 | purple_heart | 💂 | guardsman |
| ✨ | sparkles | 🎇 | sparkler |
| 🇫🇮 | fi | 🇫🇯 | fj |
| 🔙 | back | 🇫🇴 | fo |
| 🇫🇷 | fr | 🎅 | santa |
| 😋 | yum | 💷 | pound |
| 🍋 | lemon | 🇬🇦 | ga |
| 🇬🇧 | gb | 🇬🇩 | gd |
| 🇬🇪 | ge | 🇬🇫 | gf |
| 💲 | heavy_dollar_sign | 🇬🇭 | gh |
| 🇬🇮 | gi | 6️⃣ | six |
| 🙆 | ok_woman | 🇬🇲 | gm |
| 🇬🇳 | gn | 🇬🇵 | gp |
| 🇬🇶 | gq | 🏈 | football |
| 🇬🇷 | gr | 🇬🇹 | gt |
| 🇬🇺 | gu | 🇬🇼 | gw |
| 🌞 | sun_with_face | 🇬🇾 | gy |
| 🍴 | fork_and_knife | ⏪ | rewind |
| 📭 | mailbox_with_no_mail | 😜 | stuck_out_tongue_winking_eye |
| 👶 | baby | 🖖 | vulcan_salute |
| 🔕 | no_bell | ❇️ | sparkle |
| 🇭🇰 | hk | 🏤 | european_post_office |
| 🇭🇳 | hn | 📝 | memo, pencil |
| 🇭🇷 | hr | 🇭🇹 | ht |
| 👉 | point_right | 📋 | clipboard |
| 🇭🇺 | hu | 🔜 | soon |
| 🎿 | ski | 👟 | athletic_shoe |
| 4️⃣ | four | 🔒 | lock |
| 🎬 | clapper | 🇮🇩 | id |
| 🇮🇪 | ie | 👑 | crown |
| 🇮🇱 | il | 🍪 | cookie |
| ⬆️ | arrow_up | 🇮🇳 | in |
| 😆 | laughing, satisfied | 👩❤️💋👩 | couplekiss_woman_woman |
| 🇮🇶 | iq | 🇮🇷 | ir |
| ➰ | curly_loop | 🇮🇸 | is |
| 📐 | triangular_ruler | 🔀 | twisted_rightwards_arrows |
| 🇮🇹 | it | 🔔 | bell |
| 🍛 | curry | 👳 | man_with_turban |
| 🕛 | clock12 | 🕚 | clock11 |
| 🕙 | clock10 | 📞 | telephone_receiver |
| 🈸 | u7533 | ⚾️ | baseball |
| 🍙 | rice_ball | 💏 | couplekiss |
| 🐶 | dog | 🇯🇲 | jm |
| 🇯🇴 | jo | 🇯🇵 | jp |
| 😏 | smirk | 8️⃣ | eight |
| 🔍 | mag | 😬 | grimacing |
| 🌲 | evergreen_tree | 📃 | page_with_curl |
| ♎ | libra | 🇰🇪 | ke |
| 👨 | man | 📷 | camera |
| 🇰🇬 | kg | 🇰🇭 | kh |
| 🇰🇮 | ki | 🌓 | first_quarter_moon |
| 🍞 | bread | 🇰🇲 | km |
| 🚷 | no_pedestrians | 🇰🇵 | kp |
| 👍 | +1, thumbsup | 🇰🇷 | kr |
| 🔅 | low_brightness | ♓ | pisces |
| 🚎 | trolleybus | 🇰🇼 | kw |
| 🏮 | izakaya_lantern, lantern | 🚹 | mens |
| 🇰🇾 | ky | 🇰🇿 | kz |
| 📗 | green_book | 🇱🇦 | la |
| 🇱🇧 | lb | 🕧 | clock1230 |
| ✖️ | heavy_multiplication_x | 👨👩👧 | family_man_woman_girl |
| 🎌 | crossed_flags | 📔 | notebook_with_decorative_cover |
| 🇱🇮 | li | 🇱🇰 | lk |
| 🐾 | feet, paw_prints | ⛄ | snowman |
| ▫️ | white_small_square | 🇱🇷 | lr |
| 😭 | sob | 🇱🇸 | ls |
| 🇱🇹 | lt | 🇱🇺 | lu |
| 🇱🇻 | lv | 👼 | angel |
| 🎒 | school_satchel | 🏢 | office |
| ✴️ | eight_pointed_black_star | 💢 | anger |
| 🇱🇾 | ly | 💦 | sweat_drops |
| 😄 | smile | 📻 | radio |
| 😕 | confused | 🇲🇦 | ma |
| 🆘 | sos | 🍡 | dango |
| 🇲🇩 | md | 🇲🇪 | me |
| 🍣 | sushi | 🇲🇬 | mg |
| 🇲🇰 | mk | 🇲🇱 | ml |
| 🇲🇲 | mm | 🇲🇳 | mn |
| 🇲🇴 | mo | 🅾️ | o2 |
| 🇲🇵 | mp | 👎 | -1, thumbsdown |
| 🐢 | turtle | 🇲🇶 | mq |
| 🇲🇷 | mr | 🇲🇸 | ms |
| 🇲🇹 | mt | 🇲🇻 | mv |
| 🇲🇼 | mw | 🚰 | potable_water |
| 🇲🇽 | mx | 🇲🇾 | my |
| 🇲🇿 | mz | ❤️ | heart |
| 🔧 | wrench | 🐤 | baby_chick |
| 🇳🇦 | na | 🇳🇨 | nc |
| 🇳🇪 | ne | 🇳🇬 | ng |
| 🇳🇮 | ni | 👎 | -1, thumbsdown |
| 🇳🇱 | nl | 🎂 | birthday |
| 🌄 | sunrise_over_mountains | 🇳🇴 | no |
| 🇳🇵 | np | 🔨 | hammer |
| 🍬 | candy | 🐽 | pig_nose |
| 🇳🇺 | nu | 📵 | no_mobile_phones |
| 🙇 | bow | 🕤 | clock930 |
| 🇳🇿 | nz | 👦 | boy |
| 🔣 | symbols | 👘 | kimono |
| 💉 | syringe | 📨 | incoming_envelope |
| 🚬 | smoking | 🔁 | repeat |
| 💝 | gift_heart | 🈲 | u7981 |
| 🆗 | ok | 🇴🇲 | om |
| 🔛 | on | 🐓 | rooster |
| 🐩 | poodle | 💾 | floppy_disk |
| 📯 | postal_horn | 🔯 | six_pointed_star |
| 🐂 | ox | 🏇 | horse_racing |
| ❔ | grey_question | 💯 | 100 |
| 🇵🇦 | pa | 🇵🇪 | pe |
| 🍧 | shaved_ice | 🇵🇬 | pg |
| 🇵🇭 | ph | 🚳 | no_bicycles |
| 🇵🇰 | pk | 🇵🇱 | pl |
| 🕥 | clock1030 | ⛔ | no_entry |
| ⛵ | boat, sailboat | 💛 | yellow_heart |
| 🇵🇷 | pr | 🏯 | japanese_castle |
| 🇵🇸 | ps | 🇵🇹 | pt |
| 🚀 | rocket | 🇵🇼 | pw |
| ®️ | registered | 🇵🇾 | py |
| 😱 | scream | 👩👩👦👦 | family_woman_woman_boy_boy |
| 🇶🇦 | qa | 🌗 | last_quarter_moon |
| 🛁 | bathtub | 🏦 | bank |
| 📀 | dvd | ❗ | exclamation, heavy_exclamation_mark |
| 🔥 | fire | 🏰 | european_castle |
| 🏃 | runner, running | 🍄 | mushroom |
| 💕 | two_hearts | 🈺 | u55b6 |
| 👈 | point_left | 👱 | person_with_blond_hair |
| 🐹 | hamster | ⚡ | zap |
| 🚕 | taxi | 💇 | haircut |
| 🐯 | tiger | 🉑 | accept |
| 🐴 | horse | 💺 | seat |
| 🔶 | large_orange_diamond | 🌾 | ear_of_rice |
| 🇷🇪 | re | 🙍 | person_frowning |
| 🇷🇴 | ro | ⏩ | fast_forward |
| 🚿 | shower | 🇷🇸 | rs |
| 🍤 | fried_shrimp | 🇷🇺 | ru |
| 🕢 | clock730 | 🇷🇼 | rw |
| 💅 | nail_care | ✊ | fist |
| 🇸🇦 | sa | 😮 | open_mouth |
| 🇸🇧 | sb | 🇸🇨 | sc |
| 🇸🇩 | sd | 🇸🇪 | se |
| 🇸🇬 | sg | 🇸🇮 | si |
| 🌵 | cactus | 🇸🇰 | sk |
| 🇸🇱 | sl | 🇸🇲 | sm |
| 🇸🇳 | sn | 🆓 | free |
| ⁉️ | interrobang | 🇸🇴 | so |
| 🔑 | key | ✉️ | email, envelope |
| 🇸🇷 | sr | 📆 | calendar |
| 🇸🇸 | ss | 🇸🇹 | st |
| 🇸🇻 | sv | ⭐ | star |
| 🐺 | wolf | 😩 | weary |
| 🇸🇾 | sy | 1️⃣ | one |
| 🇸🇿 | sz | 🏣 | post_office |
| 😾 | pouting_cat | 〽️ | part_alternation_mark |
| ⛽ | fuelpump | 🇹🇨 | tc |
| 🇹🇫 | tf | 🇹🇬 | tg |
| 🍹 | tropical_drink | 🇹🇭 | th |
| 🐛 | bug | 🇹🇯 | tj |
| 🐟 | fish | 🇹🇱 | tl |
| 🇹🇲 | tm | 😼 | smirk_cat |
| 🇹🇳 | tn | 👓 | eyeglasses |
| 🇹🇴 | to | 🇹🇷 | tr |
| 😆 | laughing, satisfied | 🇹🇹 | tt |
| 🚌 | bus | 🇹🇻 | tv |
| 😵 | dizzy_face | 🎧 | headphones |
| 🇹🇿 | tz | ⏬ | arrow_double_down |
| 7️⃣ | seven | ❗ | exclamation, heavy_exclamation_mark |
| 🚈 | light_rail | 🇺🇦 | ua |
| 0️⃣ | zero | 💞 | revolving_hearts |
| 🇺🇬 | ug | 🍃 | leaves |
| 🚮 | put_litter_in_its_place | ➿ | loop |
| 🔎 | mag_right | 🆙 | up |
| 5️⃣ | five | 🇺🇸 | us |
| 🔮 | crystal_ball | 🈹 | u5272 |
| 🇺🇾 | uy | 🇺🇿 | uz |
| ✒️ | black_nib | 🚼 | baby_symbol |
| 🇻🇪 | ve | 🇻🇬 | vg |
| 🇻🇮 | vi | 🌊 | ocean |
| 🙌 | raised_hands | 🇻🇳 | vn |
| 🌕 | full_moon | 🔽 | arrow_down_small |
| 🆚 | vs | 🔻 | small_red_triangle_down |
| 🍇 | grapes | 🇻🇺 | vu |
| ♿ | wheelchair | 💶 | euro |
| 🌃 | night_with_stars | 🕠 | clock530 |
| 👴 | older_man | 🚾 | wc |
| 🍎 | apple | 🚨 | rotating_light |
| 😪 | sleepy | 🇼🇸 | ws |
| 🛀 | bath | 📌 | pushpin |
| 😷 | mask | 🍲 | stew |
| 📑 | bookmark_tabs | 🌷 | tulip |
| 🌱 | seedling | 🎸 | guitar |
| 👩❤️👩 | couple_with_heart_woman_woman | ⏰ | alarm_clock |
| 👞 | mans_shoe, shoe | 🈳 | u7a7a |
| 🐉 | dragon | 🇾🇪 | ye |
| 🍨 | ice_cream | 🔄 | arrows_counterclockwise |
| 😗 | kissing | 🍁 | maple_leaf |
| 📎 | paperclip | 🎭 | performing_arts |
| 🏉 | rugby_football | 🍆 | eggplant |
| 💫 | dizzy | 👌 | ok_hand |
| 🇿🇦 | za | 📜 | scroll |
| 🐐 | goat | 📱 | iphone |
| 📩 | envelope_with_arrow | 💃 | dancer |
| 📚 | books | 🇿🇲 | zm |
| 🌐 | globe_with_meridians | 🌸 | cherry_blossom |
| 👩👩👦 | family_woman_woman_boy | 🇿🇼 | zw |
| 👐 | open_hands | 🕞 | clock330 |
| 😖 | confounded | 👅 | tongue |
| ⛪ | church | 🌇 | city_sunrise |
| 🚞 | mountain_railway | 🗿 | moyai |
| ㊙️ | secret | 🎆 | fireworks |
| 💎 | gem | 👊 | facepunch, punch |
| 🎯 | dart | 👻 | ghost |
| 👂 | ear | 💈 | barber |
| 💸 | money_with_wings | 😈 | smiling_imp |
| 🛂 | passport_control | 👝 | pouch |
| 🆒 | cool | 🎢 | roller_coaster |
| 💰 | moneybag | 🎊 | confetti_ball |
| 📅 | date | 👗 | dress |
| 🐌 | snail | 📤 | outbox_tray |
| 📘 | blue_book | 🔹 | small_blue_diamond |
| ♐ | sagittarius | 🔸 | small_orange_diamond |
| 🍒 | cherries | 🌽 | corn |
| 🔉 | sound | 😀 | grinning |
| 🏂 | snowboarder | 👚 | womans_clothes |
| 🚅 | bullettrain_front | 🍂 | fallen_leaf |
| ➕ | heavy_plus_sign | 🏀 | basketball |
| 🐬 | dolphin, flipper | 🐸 | frog |
| 🚘 | oncoming_automobile | 📶 | signal_strength |
| 💆 | massage | ☀️ | sunny |
| 📖 | book, open_book | 📓 | notebook |
| 🐨 | koala | 💼 | briefcase |
| 👆 | point_up_2 | 👭 | two_women_holding_hands |
| 💐 | bouquet | 🍠 | sweet_potato |
| 🚗 | car, red_car | 🐝 | bee, honeybee |
| 🔪 | hocho, knife | 🔼 | arrow_up_small |
| ♊ | gemini | 💥 | boom, collision |
| 🎹 | musical_keyboard | 🔈 | speaker |
| 🔺 | small_red_triangle | 💨 | dash |
| 🕜 | clock130 | 🙀 | scream_cat |
| 🐍 | snake | ㊗️ | congratulations |
| 🔗 | link | 👿 | imp |
| 🔵 | large_blue_circle | 🏁 | checkered_flag |
| 🚗 | car, red_car | 🚶 | walking |
| 🐱 | cat | 🍻 | beers |
| 🚉 | station | ♥️ | hearts |
| ♣️ | clubs | 🐾 | feet, paw_prints |
| 🌿 | herb | 📄 | page_facing_up |
| 👃 | nose | 🌙 | crescent_moon |
| 🌁 | foggy | 🏭 | factory |
| 🎈 | balloon | 🚄 | bullettrain_side |
| 😥 | disappointed_relieved | 😨 | fearful |
| 🏩 | love_hotel | ♍ | virgo |
| 🗼 | tokyo_tower | ⬇️ | arrow_down |
