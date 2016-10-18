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
  <version>3.1.3</version>
</dependency>
```

You can also download the project, build it with `mvn clean install` and add the generated jar to your buildpath.

##### Via Gradle:
```gradle
compile 'com.vdurmont:emoji-java:3.1.3'
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
| 😦 | frowning | 📼 | vhs |
| 🍷 | wine_glass | 🙎 | person_with_pouting_face |
| 🍳 | egg | 🎪 | circus_tent |
| 🎷 | saxophone | 😸 | smile_cat |
| 🍵 | tea | 🤐 | zipper_mouth, zip_it, sealed_lips, lips_sealed |
| 🐼 | panda_face | ⤵️ | arrow_heading_down |
| 💩 | hankey, poop, shit | ⛏ | pick |
| 🐰 | rabbit | 🚤 | speedboat |
| 🛃 | customs | 🍶 | sake |
| ☃ | snowman_with_snow, snowing_snowman | 🛐 | worship_building, worship_place, religious_building, religious_place |
| 🐏 | ram | 📪 | mailbox_closed |
| 🕶 | sunglasses | 🎲 | game_die |
| 🛋 | couch_lamp, couch, sofa, lounge | 🐀 | rat |
| 👨👩👧👧 | family_man_woman_girl_girl | 💮 | white_flower |
| 🐄 | cow2 | 🍗 | poultry_leg |
| 👤 | bust_in_silhouette | 😉 | wink |
| 🔴 | red_circle | 🎽 | running_shirt_with_sash |
| 🙃 | upside_down, flipped_face | 📰 | newspaper |
| 🙃 | upside_down, flipped_face | 📇 | card_index |
| 🏵 | rosette | 🖍 | lower_left_crayon |
| 😫 | tired_face | 🌶 | hot_pepper, chili_pepper, spice, spicy |
| 🏴 | waving_black_flag | 👠 | high_heel |
| 🛣 | motorway, highway, road, interstate, freeway | 🌂 | closed_umbrella |
| 🔃 | arrows_clockwise | 💵 | dollar |
| 🚝 | monorail | 🐖 | pig2 |
| ⛳ | golf | 😅 | sweat_smile |
| 🔢 | 1234 | 🈴 | u5408 |
| 🛍 | shopping_bags | ⛅ | partly_sunny |
| ☄ | comet, light_beam, blue_beam | 🚯 | do_not_litter |
| 😯 | hushed | ✋ | hand, raised_hand |
| 🙈 | see_no_evil | 🅰️ | a |
| ☝️ | point_up | 🅱️ | b |
| 🔤 | abc | 🀄 | mahjong |
| 🚭 | no_smoking | 🍫 | chocolate_bar |
| 🎥 | movie_camera | Ⓜ️ | m |
| 🕎 | menorah, candelabrum, chanukiah | ⭕ | o |
| 😻 | heart_eyes_cat | 🔆 | high_brightness |
| 👄 | lips | 👜 | handbag |
| 🌝 | full_moon_with_face | ✌️ | v |
| 📲 | calling | ⚪ | white_circle |
| 🖥 | desktop_computer, pc_tower, imac | ❌ | x |
| 📊 | bar_chart | 🌨 | cloud_snow |
| 🌯 | burrito, wrap | 🐁 | mouse2 |
| 🏋 | weight_lifter | 9️⃣ | nine |
| 👫 | couple | 📹 | video_camera |
| 🎵 | musical_note | 🕎 | menorah, candelabrum, chanukiah |
| 🎶 | notes | 👯 | dancers |
| 📿 | prayer_beads, dhikr_beads, rosary_beads | 📥 | inbox_tray |
| ‼️ | bangbang | ⛸ | ice_skate, ice_skating |
| 💽 | minidisc | 🚛 | articulated_lorry |
| 😊 | blush | 🎟 | admission_ticket |
| 🎤 | microphone | 🍈 | melon |
| 🌬 | wind_blowing_face, mother_nature, blowing_wind | ➗ | heavy_division_sign |
| ✋ | hand, raised_hand | 🗽 | statue_of_liberty |
| 💩 | hankey, poop, shit | ❕ | grey_exclamation |
| 📙 | orange_book | 🌔 | moon, waxing_gibbous_moon |
| ℹ️ | information_source | ☢ | radioactive, radioactive_symbol, radioactive_sign |
| 🕴 | hovering_man, levitating_man | 🐚 | shell |
| 🛩 | small_airplane | 👩👩👧👧 | family_woman_woman_girl_girl |
| 👷 | construction_worker | 📴 | mobile_phone_off |
| 😺 | smiley_cat | 🔊 | loud_sound |
| 🤐 | zipper_mouth, zip_it, sealed_lips, lips_sealed | 🏔 | snow_capped_mountain, mont_fuji |
| 😴 | sleeping | 👖 | jeans |
| ⚜ | fleur_de_lis, scouts | 🍐 | pear |
| 🚓 | police_car | 🚏 | busstop |
| 🚙 | blue_car | 🔚 | end |
| 🛰 | satellite | 🐋 | whale2 |
| 🎐 | wind_chime | 🔂 | repeat_one |
| 🖐 | raised_hand_with_fingers_splayed, splayed_hand | 🎫 | ticket |
| ☔ | umbrella | ⛩ | shinto_shrine, kami_no_michi |
| 🏚 | derelict_house, old_house, abandoned_house | 🗒 | spiral_note_pad |
| 🏷 | label | 🍚 | rice |
| ✂️ | scissors | 🦄 | unicorn_face |
| 🈷️ | u6708 | ♒ | aquarius |
| 🈶 | u6709 | ☕ | coffee |
| ✈️ | airplane | 😓 | sweat |
| 🦀 | crab, cancer | 👪 | family |
| 🕵 | detective, sleuth, private_eye, spy | 🦀 | crab, cancer |
| 😘 | kissing_heart | 🎁 | gift |
| 🛣 | motorway, highway, road, interstate, freeway | ↕️ | arrow_up_down |
| ⛲ | fountain | ©️ | copyright |
| 🏕 | camping, campsite, tent | 🖊 | lower_left_ballpoint_pen |
| 🌒 | waxing_crescent_moon | 🍿 | popcorn |
| 💟 | heart_decoration | 💊 | pill |
| 🍢 | oden | ⏮ | black_left_pointing_double_triangle_with_vertical_bar |
| 🐗 | boar | 🗳 | ballot, ballot_box |
| 🐫 | camel | ⛸ | ice_skate, ice_skating |
| 📧 | e-mail | 🕊 | dove, dove_peace |
| ✡ | star_of_david | 🤗 | hugging, hug, hugs |
| ◻️ | white_medium_square | 🍊 | tangerine |
| 🕸 | spider_web, cobweb | 💡 | bulb |
| 🕹 | joystick | 🎚 | level_slider |
| 👲 | man_with_gua_pi_mao | 🐆 | leopard |
| 🌎 | earth_americas | 📫 | mailbox |
| 🍏 | green_apple | 🏐 | volleyball |
| 💤 | zzz | 💠 | diamond_shape_with_a_dot_inside |
| #️⃣ | hash | 🔫 | gun |
| 🛐 | worship_building, worship_place, religious_building, religious_place | 😑 | expressionless |
| 🚩 | triangular_flag_on_post | ⚛ | atom, atom_symbol |
| 👩 | woman | 🚠 | mountain_cableway |
| 🏒 | ice_hockey | 🐠 | tropical_fish |
| 🍩 | doughnut | 😒 | unamused |
| ⚫ | black_circle | 🤕 | injured, head_bandage, head_bandaged, bandaged |
| 👙 | bikini | 🔝 | top |
| 🚟 | suspension_railway | 🖌 | lower_left_paintbrush |
| ⬛ | black_large_square | 🙋 | raising_hand |
| 🚻 | restroom | ❄️ | snowflake |
| 🎩 | tophat | ↙️ | arrow_lower_left |
| ❓ | question | *⃣ | keycap_asterisk, star_keycap |
| 🚺 | womens | 😽 | kissing_cat |
| ⛵ | boat, sailboat | 💳 | credit_card |
| 🎱 | 8ball | 🍘 | rice_cracker |
| ⚓ | anchor | 🌹 | rose |
| ♠️ | spades | 🔷 | large_blue_diamond |
| ⚛ | atom, atom_symbol | 👰 | bride_with_veil |
| 🕣 | clock830 | 👮 | cop |
| 📮 | postbox | 🍖 | meat_on_bone |
| 🌶 | hot_pepper, chili_pepper, spice, spicy | 🌶 | hot_pepper, chili_pepper, spice, spicy |
| 🐮 | cow | 🐲 | dragon_face |
| 🦁 | lion_face, cute_lion, timid_lion | 🙏 | pray |
| 😐 | neutral_face | 🐷 | pig |
| 🔘 | radio_button | 📟 | pager |
| ♨️ | hotsprings | 🎡 | ferris_wheel |
| 🤕 | injured, head_bandage, head_bandaged, bandaged | 🎙 | studio_microphone |
| 📽 | film_projector | 🌳 | deciduous_tree |
| ↩️ | leftwards_arrow_with_hook | 🏄 | surfer |
| 🆕 | new | ◾ | black_medium_small_square |
| ⚜ | fleur_de_lis, scouts | 🎋 | tanabata_tree |
| 👨❤️💋👨 | couplekiss_man_man | 🌶 | hot_pepper, chili_pepper, spice, spicy |
| 🏚 | derelict_house, old_house, abandoned_house | 💭 | thought_balloon |
| 🐎 | racehorse | ⚖ | scales, scales_of_justice |
| 🙅 | no_good | 🚐 | minibus |
| 🙁 | slightly_frowning | 🕦 | clock1130 |
| ⚙ | gear | 🎠 | carousel_horse |
| ◼️ | black_medium_square | 🐔 | chicken |
| 📳 | vibration_mode | ◽ | white_medium_small_square |
| 🗣 | speaking_head_in_silhouette | 🏃 | runner, running |
| 🏎 | racing_car, formula_one, f1 | 🐞 | beetle |
| 🍱 | bento | ⛎ | ophiuchus |
| 😝 | stuck_out_tongue_closed_eyes | 🖼 | picture_frame, painting, gallery |
| 🎴 | flower_playing_cards | 💪 | muscle |
| 🚧 | construction | 💀 | skull |
| 👍 | +1, thumbsup | 😙 | kissing_smiling_eyes |
| 💙 | blue_heart | 🌌 | milky_way |
| 🌼 | blossom | 🐜 | ant |
| 🎑 | rice_scene | 📬 | mailbox_with_mail |
| 😢 | cry | 📝 | memo, pencil |
| 🈯 | u6307 | 🍍 | pineapple |
| 🈁 | koko | 🎼 | musical_score |
| 🦁 | lion_face, cute_lion, timid_lion | 🗡 | dagger, dagger_knife, knife_weapon |
| 💧 | droplet | 💬 | speech_balloon |
| 🈚 | u7121 | 💍 | ring |
| 🐙 | octopus | 🗾 | japan |
| 🕡 | clock630 | 🚢 | ship |
| 🛋 | couch_lamp, couch, sofa, lounge | 📛 | name_badge |
| 🧀 | cheese | 🖐 | raised_hand_with_fingers_splayed, splayed_hand |
| ⛰ | mountain | 🌜 | last_quarter_moon_with_face |
| ☮ | peace_symbol, peace_sign | 🔏 | lock_with_ink_pen |
| 🍰 | cake | ♌ | leo |
| ♻️ | recycle | ↪️ | arrow_right_hook |
| 🤓 | nerd, nerdy | 🏺 | amphora, jar, vase |
| 🚍 | oncoming_bus | 🏑 | field_hockey |
| ♏ | scorpius | 🤐 | zipper_mouth, zip_it, sealed_lips, lips_sealed |
| 🤕 | injured, head_bandage, head_bandaged, bandaged | 🕍 | synagogue, temple, jewish |
| 🔇 | mute | 🔌 | electric_plug |
| 🏊 | swimmer | ❎ | negative_squared_cross_mark |
| 😶 | no_mouth | 👣 | footprints |
| 🎍 | bamboo | 👁 | eye |
| 🚽 | toilet | ✉️ | email, envelope |
| 🌛 | first_quarter_moon_with_face | 🦃 | turkey |
| ⛑ | helmet_white_cross | 🔭 | telescope |
| 🏹 | bow_and_arrow, bow_arrow, archery | 🏬 | department_store |
| 🌅 | sunrise | 🗯 | right_anger_bubble, zig_zag_bubble |
| 🕌 | mosque, minaret, domed_roof | 🛬 | airplane_arriving, airplane_arrival, landing |
| 🌉 | bridge_at_night | 🔋 | battery |
| 🖲 | trackball | 2️⃣ | two |
| 🚁 | helicopter | 😧 | anguished |
| 🚜 | tractor | 🉐 | ideograph_advantage |
| 🃏 | black_joker | 📸 | camera_flash |
| 🐕 | dog2 | 😟 | worried |
| 🍉 | watermelon | 🏆 | trophy |
| 🔦 | flashlight | 👩👩👧👦 | family_woman_woman_girl_boy |
| 🌴 | palm_tree | 🤔 | thinking, think, thinker |
| 😤 | triumph | 🎨 | art |
| 🔞 | underage | 👡 | sandal |
| 🚸 | children_crossing | 😛 | stuck_out_tongue |
| 🙊 | speak_no_evil | 🌧 | cloud_rain |
| 💘 | cupid | 🚦 | vertical_traffic_light |
| 🕉 | om_symbol, pranava, aumkara, omkara | 💩 | hankey, poop, shit |
| 👕 | shirt, tshirt | ⌚ | watch |
| 👏 | clap | ➡️ | arrow_right |
| 💹 | chart | 🕟 | clock430 |
| 👇 | point_down | 🎄 | christmas_tree |
| 🤗 | hugging, hug, hugs | 💚 | green_heart |
| 💣 | bomb | 👨👨👧👧 | family_man_man_girl_girl |
| 🐭 | mouse | 😞 | disappointed |
| 🐣 | hatching_chick | 🏫 | school |
| 🈵 | u6e80 | ♉ | taurus |
| ⬅️ | arrow_left | 🏧 | atm |
| 🕌 | mosque, minaret, domed_roof | 🏌 | golfer, golf_club |
| 🍀 | four_leaf_clover | 🔓 | unlock |
| 🕵 | detective, sleuth, private_eye, spy | 💌 | love_letter |
| ☮ | peace_symbol, peace_sign | 🌟 | star2 |
| ☎️ | phone, telephone | 👧 | girl |
| 👒 | womans_hat | 🚃 | railway_car |
| ☹ | frowning_face | 🔖 | bookmark |
| 🍦 | icecream | 🎺 | trumpet |
| 🤖 | robot_face, bot_face | 🖥 | desktop_computer, pc_tower, imac |
| 🎮 | video_game | 🐊 | crocodile |
| ☂ | open_umbrella | 💥 | boom, collision |
| 🐇 | rabbit2 | 📖 | book, open_book |
| 🚚 | truck | 📈 | chart_with_upwards_trend |
| 🕴 | hovering_man, levitating_man | 🕍 | synagogue, temple, jewish |
| 💄 | lipstick | 🏠 | house |
| 👋 | wave | 👨👨👦 | family_man_man_boy |
| 🍕 | pizza | 🍸 | cocktail |
| 🌺 | hibiscus | 🛎 | bellhop_bell |
| 🤔 | thinking, think, thinker | 👢 | boot |
| 👞 | mans_shoe, shoe | 🏝 | desert_island |
| 🎣 | fishing_pole_and_fish | 💴 | yen |
| 🔱 | trident | 🎰 | slot_machine |
| 🌠 | stars | 😰 | cold_sweat |
| 🕒 | clock3 | 🕑 | clock2 |
| ◀️ | arrow_backward | 🕔 | clock5 |
| 👺 | japanese_goblin | 🕓 | clock4 |
| 🕐 | clock1 | 🔟 | keycap_ten |
| ▶️ | arrow_forward | 🕖 | clock7 |
| 🕕 | clock6 | 🕘 | clock9 |
| 🕗 | clock8 | 📿 | prayer_beads, dhikr_beads, rosary_beads |
| 🅿️ | parking | 🕝 | clock230 |
| 🙉 | hear_no_evil | 💓 | heartbeat |
| 🖱 | computer_mouse, three_button_mouse | 🙂 | slightly_smiling |
| ⛈ | thunder_cloud_rain | ⬜ | white_large_square |
| 3️⃣ | three | 🤑 | money_mouth, money_face |
| 💗 | heartpulse | 🎖 | military_medal, military_decoration |
| 💁 | information_desk_person | 👸 | princess |
| 📠 | fax | 🚊 | tram |
| 🖼 | picture_frame, painting, gallery | 🕎 | menorah, candelabrum, chanukiah |
| 🚑 | ambulance | ✅ | white_check_mark |
| 🤘 | horns_sign, rock_on, heavy_metal, devil_fingers | 🏗 | building_construction, crane |
| 🐈 | cat2 | 🌔 | moon, waxing_gibbous_moon |
| ↘️ | arrow_lower_right | ✔️ | heavy_check_mark |
| 🍜 | ramen | ☎️ | phone, telephone |
| 🚇 | metro | 🤘 | horns_sign, rock_on, heavy_metal, devil_fingers |
| 🐃 | water_buffalo | 🗞 | rolled_up_newspaper, newspaper_delivery |
| 👹 | japanese_ogre | 🌡 | thermometer, hot_weather, temperature |
| 👔 | necktie | 🎎 | dolls |
| 🐵 | monkey_face | 🔠 | capital_abcd |
| 🎏 | flags | 😔 | pensive |
| 👾 | space_invader | 🍭 | lollipop |
| 🚆 | train2 | ☘ | shamrock, st_patrick |
| 👽 | alien | 📁 | file_folder |
| 💻 | computer | 🍥 | fish_cake |
| 🏔 | snow_capped_mountain, mont_fuji | ↔️ | left_right_arrow |
| 🍟 | fries | 🛅 | left_luggage |
| 👁🗨 | eye_in_speech_bubble, i_am_a_witness | 😌 | relieved |
| 🍾 | champagne, sparkling_wine | 🦂 | scorpion |
| 😁 | grin | 📒 | ledger |
| 👬 | two_men_holding_hands | ☠ | skull_crossbones |
| ☑️ | ballot_box_with_check | 🍓 | strawberry |
| 🏹 | bow_and_arrow, bow_arrow, archery | ⤴️ | arrow_heading_up |
| 🎾 | tennis | 👨👩👦👦 | family_man_woman_boy_boy |
| ☸ | wheel_of_dharma | 👕 | shirt, tshirt |
| ☣ | biohazard, biohazard_symbol, biohazard_sign | 🏳 | waving_white_flag |
| 😡 | rage | 🌘 | waning_crescent_moon |
| 😹 | joy_cat | 😣 | persevere |
| 😿 | crying_cat_face | 🚪 | door |
| ♦️ | diamonds | ⛹ | person_with_ball |
| 🐒 | monkey | 🗯 | right_anger_bubble, zig_zag_bubble |
| 🌀 | cyclone | 👨👨👦👦 | family_man_man_boy_boy |
| ☁️ | cloud | 💱 | currency_exchange |
| 🗻 | mount_fuji | 🚖 | oncoming_taxi |
| 🐳 | whale | ✏️ | pencil2 |
| 🏞 | national_park | ⏲ | timer_clock |
| 🐻 | bear | 🎉 | tada |
| 🍌 | banana | 〰️ | wavy_dash |
| 🎻 | violin | 📦 | package |
| 📂 | open_file_folder | 🛋 | couch_lamp, couch, sofa, lounge |
| 👊 | facepunch, punch | ↖️ | arrow_upper_left |
| 🌮 | taco | 🛤 | railway_track |
| 😍 | heart_eyes | 🌈 | rainbow |
| ➖ | heavy_minus_sign | 🕵 | detective, sleuth, private_eye, spy |
| 📢 | loudspeaker | 🌍 | earth_africa |
| 🙄 | eye_roll, rolling_eyes | ✝ | latin_cross, christian_cross |
| 🏕 | camping, campsite, tent | 🎖 | military_medal, military_decoration |
| 👨👨👧👦 | family_man_man_girl_boy | 🌭 | hot_dog |
| 🌬 | wind_blowing_face, mother_nature, blowing_wind | 🔬 | microscope |
| 🚲 | bike | ☺️ | relaxed |
| 😂 | joy | ⏫ | arrow_double_up |
| ✳️ | eight_spoked_asterisk | 🏥 | hospital |
| 🍯 | honey_pot | 🐑 | sheep |
| ⏳ | hourglass_flowing_sand | ⚽ | soccer |
| 🤒 | sick, ill, thermometer_face | 🏖 | breach |
| 📉 | chart_with_downwards_trend | 🌚 | new_moon_with_face |
| 🌖 | waning_gibbous_moon | 🚒 | fire_engine |
| 🌏 | earth_asia | 📕 | closed_book |
| ☢ | radioactive, radioactive_symbol, radioactive_sign | 😇 | innocent |
| 🗡 | dagger, dagger_knife, knife_weapon | 💋 | kiss |
| 👨❤️👨 | couple_with_heart_man_man | 🍽 | fork_knife_plate |
| 😳 | flushed | 🍔 | hamburger |
| 🤑 | money_mouth, money_face | 🚴 | bicyclist |
| ⌛ | hourglass | 🚥 | traffic_light |
| 🏙 | cityscape | ⚰ | coffin, funeral, casket |
| 🍮 | custard | 🎦 | cinema |
| 🚔 | oncoming_police_car | 🍺 | beer |
| 😃 | smiley | 🛫 | airplane_departure, take_off |
| 🆎 | ab | 🛋 | couch_lamp, couch, sofa, lounge |
| 🇦🇩 | ad | 🇦🇪 | ae |
| 🇦🇫 | af | 🇦🇬 | ag |
| 🍑 | peach | 🏮 | izakaya_lantern, lantern |
| 🇦🇮 | ai | 💑 | couple_with_heart |
| 🚱 | non-potable_water | 😠 | angry |
| 🇦🇱 | al | 🇦🇲 | am |
| 🇦🇴 | ao | 📍 | round_pushpin |
| 🇦🇷 | ar | 🇦🇸 | as |
| 🇦🇹 | at | 🇦🇺 | au |
| 🇦🇼 | aw | 🔐 | closed_lock_with_key |
| 🇦🇿 | az | 🗨 | left_speech_bubble |
| 💖 | sparkling_heart | 🗜 | compression |
| 🇧🇦 | ba | 🇧🇧 | bb |
| 📣 | mega | ☪ | star_and_crescent, star_crescent |
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
| 🇧🇼 | bw | 🕊 | dove, dove_peace |
| 🇧🇾 | by | ☄ | comet, light_beam, blue_beam |
| 🇧🇿 | bz | 🏨 | hotel |
| 🛄 | baggage_claim | 🌋 | volcano |
| 💒 | wedding | 🏡 | house_with_garden |
| 🇨🇦 | ca | 🛠 | hammer_and_wrench |
| 🇨🇩 | cd | 🇨🇫 | cf |
| 🇨🇬 | cg | 🇨🇭 | ch |
| 🇨🇮 | ci | 🇨🇰 | ck |
| 🇨🇱 | cl | 🇨🇲 | cm |
| 🇨🇳 | cn | 🌰 | chestnut |
| 🇨🇴 | co | 🐪 | dromedary_camel |
| 🌻 | sunflower | 🔡 | abcd |
| 🇨🇰 | cr | 🐡 | blowfish |
| 👨👨👧 | family_man_man_girl | 🇨🇺 | cu |
| 🇨🇻 | cv | 🇨🇼 | cw |
| 🛥 | motor_boat | 🇨🇾 | cy |
| 👨👩👦 | family_man_woman_boy | 🇨🇿 | cz |
| 🎃 | jack_o_lantern | ▪️ | black_small_square |
| 🛫 | airplane_departure, take_off | 😲 | astonished |
| 🏜 | desert | 🕷 | spider |
| 👛 | purse | 🐧 | penguin |
| ⏱ | stopwatch | 🛏 | bed, bedroom |
| 🇩🇪 | de | 🐝 | bee, honeybee |
| 👩👩👧 | family_woman_woman_girl | ↗️ | arrow_upper_right |
| 🇩🇯 | dj | 🚵 | mountain_bicyclist |
| 🇩🇰 | dk | 🖕 | middle_finger |
| 🇩🇲 | dm | ♈ | aries |
| 🇩🇴 | do | 🏎 | racing_car, formula_one, f1 |
| 👵 | older_woman | 🎳 | bowling |
| 😚 | kissing_closed_eyes | 👥 | busts_in_silhouette |
| 🌪 | cloud_tornado | 🔰 | beginner |
| 🇩🇿 | dz | 🐦 | bird |
| 🐿 | chipmunk, squirrel | ⚠️ | warning |
| 🔲 | black_square_button | 🛣 | motorway, highway, road, interstate, freeway |
| 🇪🇨 | ec | 🚋 | train |
| 🏪 | convenience_store | 🇪🇪 | ee |
| 🐬 | dolphin, flipper | 🔳 | white_square_button |
| 🇪🇬 | eg | 📏 | straight_ruler |
| ⚒ | hammer_and_pick | 🚫 | no_entry_sign |
| 🎀 | ribbon | 🌩 | cloud_lightning |
| 🎓 | mortar_board | 🇪🇷 | er |
| 🛣 | motorway, highway, road, interstate, freeway | 🇪🇸 | es |
| 🇪🇹 | et | ♑ | capricorn |
| 🏌 | golfer, golf_club | 🍼 | baby_bottle |
| 🚣 | rowboat | 🔪 | hocho, knife |
| ☪ | star_and_crescent, star_crescent | ⚖ | scales, scales_of_justice |
| 🌆 | city_sunset | 🐅 | tiger2 |
| 🚂 | steam_locomotive | 🔩 | nut_and_bolt |
| 🐘 | elephant | 💜 | purple_heart |
| 🌫 | fog | 💂 | guardsman |
| ✨ | sparkles | 🎇 | sparkler |
| 🇫🇮 | fi | 🇫🇯 | fj |
| 🔙 | back | 🇫🇴 | fo |
| 🇫🇷 | fr | 🎅 | santa |
| 😋 | yum | ⚱ | funeral_urn |
| 💷 | pound | 🍋 | lemon |
| 🕌 | mosque, minaret, domed_roof | ⛴ | ferry |
| 🇬🇦 | ga | 🇬🇧 | gb |
| 🇬🇩 | gd | 🇬🇪 | ge |
| 🇬🇫 | gf | 💲 | heavy_dollar_sign |
| 🏘 | house_buildings, multiple_houses | 🗳 | ballot, ballot_box |
| 🇬🇭 | gh | 🇬🇮 | gi |
| 6️⃣ | six | 🙆 | ok_woman |
| 🇬🇲 | gm | 🇬🇳 | gn |
| 🇬🇵 | gp | 🇬🇶 | gq |
| 🏈 | football | 🇬🇷 | gr |
| 🇬🇹 | gt | 🇬🇺 | gu |
| 🇬🇼 | gw | 🕳 | hole |
| 🌞 | sun_with_face | 🇬🇾 | gy |
| 🍴 | fork_and_knife | ⏪ | rewind |
| 🗂 | card_index_dividers | 📭 | mailbox_with_no_mail |
| 😜 | stuck_out_tongue_winking_eye | 👶 | baby |
| 🖖 | vulcan_salute | 🤗 | hugging, hug, hugs |
| 🔕 | no_bell | ❇️ | sparkle |
| 🇭🇰 | hk | 🏤 | european_post_office |
| 🇭🇳 | hn | 📝 | memo, pencil |
| 🇭🇷 | hr | 🤒 | sick, ill, thermometer_face |
| 🇭🇹 | ht | 👉 | point_right |
| 📋 | clipboard | 🇭🇺 | hu |
| 🔜 | soon | ☣ | biohazard, biohazard_symbol, biohazard_sign |
| 🎿 | ski | 👟 | athletic_shoe |
| 4️⃣ | four | 🔒 | lock |
| 🏟 | stadium | 🎬 | clapper |
| 🇮🇩 | id | 🇮🇪 | ie |
| 👑 | crown | 🤐 | zipper_mouth, zip_it, sealed_lips, lips_sealed |
| 🇮🇱 | il | 🍪 | cookie |
| ⬆️ | arrow_up | 🇮🇳 | in |
| 😆 | laughing, satisfied | 👩❤️💋👩 | couplekiss_woman_woman |
| 🖨 | printer | 🇮🇶 | iq |
| 🇮🇷 | ir | ☃ | snowman_with_snow, snowing_snowman |
| ➰ | curly_loop | 🇮🇸 | is |
| 📐 | triangular_ruler | 🔀 | twisted_rightwards_arrows |
| 🇮🇹 | it | 🕉 | om_symbol, pranava, aumkara, omkara |
| 🔔 | bell | 🍛 | curry |
| 👳 | man_with_turban | 🕛 | clock12 |
| 🕚 | clock11 | 🎞 | film_frames |
| 🕙 | clock10 | ⚔ | crossed_swords |
| 📞 | telephone_receiver | 🈸 | u7533 |
| ⚾️ | baseball | 🍙 | rice_ball |
| 💏 | couplekiss | 🐶 | dog |
| 🛡 | shield | 🇯🇲 | jm |
| ☘ | shamrock, st_patrick | 🇯🇴 | jo |
| 🇯🇵 | jp | 🕋 | kaaba, mecca |
| 🎗 | reminder_ribbon, awareness_ribbon | 🤔 | thinking, think, thinker |
| 🤓 | nerd, nerdy | 😏 | smirk |
| 8️⃣ | eight | 🔍 | mag |
| 😬 | grimacing | 🌲 | evergreen_tree |
| 📃 | page_with_curl | ♎ | libra |
| 🇰🇪 | ke | 👨 | man |
| 🕋 | kaaba, mecca | 📷 | camera |
| 🇰🇬 | kg | 🇰🇭 | kh |
| 🇰🇮 | ki | 🌓 | first_quarter_moon |
| 🍞 | bread | 🇰🇲 | km |
| 🚷 | no_pedestrians | 🇰🇵 | kp |
| 👍 | +1, thumbsup | 🇰🇷 | kr |
| 🔅 | low_brightness | ♓ | pisces |
| 🚎 | trolleybus | 🇰🇼 | kw |
| 🤘 | horns_sign, rock_on, heavy_metal, devil_fingers | 🏮 | izakaya_lantern, lantern |
| 🎗 | reminder_ribbon, awareness_ribbon | 🚹 | mens |
| 🇰🇾 | ky | 🇰🇿 | kz |
| 📗 | green_book | 🇱🇦 | la |
| 🇱🇧 | lb | 🕯 | candle |
| 🕧 | clock1230 | ✖️ | heavy_multiplication_x |
| 👨👩👧 | family_man_woman_girl | 🎌 | crossed_flags |
| 📔 | notebook_with_decorative_cover | 🇱🇮 | li |
| 🖋 | lower_left_fountain_pen | 🇱🇰 | lk |
| 🕰 | mantelpiece_clock | 🐾 | feet, paw_prints |
| ⛄ | snowman | 🖥 | desktop_computer, pc_tower, imac |
| ▫️ | white_small_square | 🇱🇷 | lr |
| 😭 | sob | 🇱🇸 | ls |
| 🇱🇹 | lt | 🇱🇺 | lu |
| 🇱🇻 | lv | 🙄 | eye_roll, rolling_eyes |
| 🗝 | old_key | 👼 | angel |
| 🎒 | school_satchel | 🏢 | office |
| ✴️ | eight_pointed_black_star | 💢 | anger |
| 🇱🇾 | ly | 💦 | sweat_drops |
| 😄 | smile | 📻 | radio |
| 😕 | confused | 🇲🇦 | ma |
| 🆘 | sos | 🍡 | dango |
| 🇲🇩 | md | 🇲🇪 | me |
| 🍣 | sushi | 🍾 | champagne, sparkling_wine |
| 🇲🇬 | mg | 🇲🇰 | mk |
| 🇲🇱 | ml | 🏚 | derelict_house, old_house, abandoned_house |
| 🇲🇲 | mm | 🇲🇳 | mn |
| 🇲🇴 | mo | 🅾️ | o2 |
| 🇲🇵 | mp | 👎 | -1, thumbsdown |
| 🐢 | turtle | 🇲🇶 | mq |
| 🇲🇷 | mr | *⃣ | keycap_asterisk, star_keycap |
| 🇲🇸 | ms | 🇲🇹 | mt |
| 🇲🇻 | mv | 🇲🇼 | mw |
| 🚰 | potable_water | 🇲🇽 | mx |
| 🇲🇾 | my | 🇲🇿 | mz |
| ❤️ | heart | 🔧 | wrench |
| ⛱ | planted_umbrella, umbrella_on_ground | 🐤 | baby_chick |
| 🇳🇦 | na | 🇳🇨 | nc |
| 🇳🇪 | ne | 🇳🇬 | ng |
| 🇳🇮 | ni | 🌡 | thermometer, hot_weather, temperature |
| 👎 | -1, thumbsdown | 🕵 | detective, sleuth, private_eye, spy |
| 🇳🇱 | nl | 🎂 | birthday |
| 🌄 | sunrise_over_mountains | 🇳🇴 | no |
| 🇳🇵 | np | 🔨 | hammer |
| 🍬 | candy | 🐽 | pig_nose |
| 🇳🇺 | nu | ☦ | orthodox_cross |
| 📵 | no_mobile_phones | 🙇 | bow |
| 🕤 | clock930 | 🇳🇿 | nz |
| 👦 | boy | 🔣 | symbols |
| 👘 | kimono | 💉 | syringe |
| 📨 | incoming_envelope | 🚬 | smoking |
| 🔁 | repeat | 💝 | gift_heart |
| 🈲 | u7981 | 🆗 | ok |
| 🇴🇲 | om | 🗺 | world_map |
| 🔛 | on | ⌨ | keyboard |
| 🐓 | rooster | 🐩 | poodle |
| 💾 | floppy_disk | 📯 | postal_horn |
| 🔯 | six_pointed_star | 🐂 | ox |
| 🏇 | horse_racing | ❔ | grey_question |
| 💯 | 100 | 🇵🇦 | pa |
| 🗓 | spiral_calendar_pad | 🇵🇪 | pe |
| 🖼 | picture_frame, painting, gallery | 🍧 | shaved_ice |
| 🇵🇬 | pg | 🇵🇭 | ph |
| 🚳 | no_bicycles | 🇵🇰 | pk |
| 🇵🇱 | pl | 🕥 | clock1030 |
| 🕸 | spider_web, cobweb | ⛔ | no_entry |
| ⛵ | boat, sailboat | 💛 | yellow_heart |
| 🇵🇷 | pr | 🏯 | japanese_castle |
| 🇵🇸 | ps | 🇵🇹 | pt |
| ☯ | yin_yang | 🚀 | rocket |
| 🇵🇼 | pw | ®️ | registered |
| 🇵🇾 | py | 😱 | scream |
| 🏺 | amphora, jar, vase | 🏘 | house_buildings, multiple_houses |
| 🕉 | om_symbol, pranava, aumkara, omkara | 👩👩👦👦 | family_woman_woman_boy_boy |
| 🇶🇦 | qa | 🌗 | last_quarter_moon |
| 🛁 | bathtub | 🏦 | bank |
| 📀 | dvd | ❗ | exclamation, heavy_exclamation_mark |
| 🔥 | fire | 🏰 | european_castle |
| 📿 | prayer_beads, dhikr_beads, rosary_beads | ⏭ | black_right_pointing_double_triangle_with_vertical_bar |
| ✝ | latin_cross, christian_cross | 🏃 | runner, running |
| 🍄 | mushroom | 💕 | two_hearts |
| 🈺 | u55b6 | 🤕 | injured, head_bandage, head_bandaged, bandaged |
| 🏍 | racing_motorcycle, motorcycle, motorbike | 👈 | point_left |
| 👱 | person_with_blond_hair | 🐹 | hamster |
| ⚡ | zap | 🚕 | taxi |
| 💇 | haircut | 🐯 | tiger |
| 🉑 | accept | ⏯ | black_right_pointing_triangle_with_double_vertical_bar |
| 🐴 | horse | 💺 | seat |
| 🔶 | large_orange_diamond | 🌾 | ear_of_rice |
| 🐿 | chipmunk, squirrel | 🇷🇪 | re |
| 🛐 | worship_building, worship_place, religious_building, religious_place | ⚰ | coffin, funeral, casket |
| 🙍 | person_frowning | 🇷🇴 | ro |
| ⏩ | fast_forward | 🚿 | shower |
| 🇷🇸 | rs | 🍤 | fried_shrimp |
| 🇷🇺 | ru | 🕢 | clock730 |
| 🇷🇼 | rw | 💅 | nail_care |
| ✊ | fist | 🇸🇦 | sa |
| 😮 | open_mouth | 🇸🇧 | sb |
| 🇸🇨 | sc | 🇸🇩 | sd |
| 🇸🇪 | se | 🇸🇬 | sg |
| 🌬 | wind_blowing_face, mother_nature, blowing_wind | 🇸🇮 | si |
| 🌵 | cactus | 🇸🇰 | sk |
| 🌡 | thermometer, hot_weather, temperature | 🇸🇱 | sl |
| 🇸🇲 | sm | 🇸🇳 | sn |
| 🆓 | free | ⁉️ | interrobang |
| 🇸🇴 | so | 🔑 | key |
| ✉️ | email, envelope | 🇸🇷 | sr |
| 📆 | calendar | 🇸🇸 | ss |
| 🇸🇹 | st | 🇸🇻 | sv |
| ⭐ | star | 🐺 | wolf |
| 😩 | weary | 🇸🇾 | sy |
| 1️⃣ | one | 🇸🇿 | sz |
| 🏣 | post_office | 🏸 | badminton |
| 😾 | pouting_cat | 〽️ | part_alternation_mark |
| ⛽ | fuelpump | 🇹🇨 | tc |
| 🇹🇫 | tf | 🇹🇬 | tg |
| 🍹 | tropical_drink | 🇹🇭 | th |
| 🐛 | bug | 🇹🇯 | tj |
| 🏏 | cricket | 🐟 | fish |
| 🇹🇱 | tl | 🇹🇲 | tm |
| 😼 | smirk_cat | 🇹🇳 | tn |
| 👓 | eyeglasses | 🇹🇴 | to |
| 🇹🇷 | tr | 😆 | laughing, satisfied |
| 🇹🇹 | tt | 🤘 | horns_sign, rock_on, heavy_metal, devil_fingers |
| 🚌 | bus | 🇹🇻 | tv |
| 😵 | dizzy_face | 🎧 | headphones |
| 🇹🇿 | tz | ⏬ | arrow_double_down |
| 7️⃣ | seven | ❗ | exclamation, heavy_exclamation_mark |
| 🚈 | light_rail | 🇺🇦 | ua |
| 0️⃣ | zero | 🏍 | racing_motorcycle, motorcycle, motorbike |
| 💞 | revolving_hearts | 🇺🇬 | ug |
| 🌦 | white_sun_behind_cloud_rain | 🌥 | white_sun_behind_cloud |
| 🍃 | leaves | 🚮 | put_litter_in_its_place |
| ➿ | loop | 🔎 | mag_right |
| 🆙 | up | 5️⃣ | five |
| 🇺🇸 | us | 🔮 | crystal_ball |
| 🏗 | building_construction, crane | 🈹 | u5272 |
| 🇺🇾 | uy | 🇺🇿 | uz |
| 🛌 | sleeping_accommodation | ✒️ | black_nib |
| 🚼 | baby_symbol | 🏺 | amphora, jar, vase |
| 🇻🇪 | ve | 🇻🇬 | vg |
| ☄ | comet, light_beam, blue_beam | 🇻🇮 | vi |
| 🌊 | ocean | 🙌 | raised_hands |
| 🇻🇳 | vn | 🌕 | full_moon |
| 🔽 | arrow_down_small | 🆚 | vs |
| 🔻 | small_red_triangle_down | 🍇 | grapes |
| 🇻🇺 | vu | ♿ | wheelchair |
| 💶 | euro | 🌃 | night_with_stars |
| 🕠 | clock530 | 👴 | older_man |
| 🚾 | wc | 🏅 | sports_medal, sports_decoration |
| 🍎 | apple | 🤖 | robot_face, bot_face |
| 🏹 | bow_and_arrow, bow_arrow, archery | 🚨 | rotating_light |
| 🏅 | sports_medal, sports_decoration | 😪 | sleepy |
| 🇼🇸 | ws | 🛀 | bath |
| 📌 | pushpin | 😷 | mask |
| 🍲 | stew | 📑 | bookmark_tabs |
| 🌷 | tulip | 🌱 | seedling |
| 🛏 | bed, bedroom | 🎸 | guitar |
| 🛬 | airplane_arriving, airplane_arrival, landing | 🗞 | rolled_up_newspaper, newspaper_delivery |
| 🏕 | camping, campsite, tent | 🛐 | worship_building, worship_place, religious_building, religious_place |
| 👩❤️👩 | couple_with_heart_woman_woman | 🖇 | linked_paperclips |
| ⏰ | alarm_clock | 👞 | mans_shoe, shoe |
| 🈳 | u7a7a | ⛓ | chains |
| ⏹ | black_square_for_stop | 🐉 | dragon |
| 🇾🇪 | ye | 🍨 | ice_cream |
| 🔄 | arrows_counterclockwise | 🛣 | motorway, highway, road, interstate, freeway |
| 😗 | kissing | 🍁 | maple_leaf |
| 📎 | paperclip | 🎭 | performing_arts |
| 🏉 | rugby_football | 🍆 | eggplant |
| 💫 | dizzy | 👌 | ok_hand |
| 🇿🇦 | za | 📜 | scroll |
| 🖱 | computer_mouse, three_button_mouse | 🐐 | goat |
| 📱 | iphone | 📩 | envelope_with_arrow |
| 💃 | dancer | ☣ | biohazard, biohazard_symbol, biohazard_sign |
| 📚 | books | 🇿🇲 | zm |
| 🛢 | oil_drum | 🌐 | globe_with_meridians |
| 🌸 | cherry_blossom | 👩👩👦 | family_woman_woman_boy |
| 🇿🇼 | zw | 👐 | open_hands |
| 🕞 | clock330 | 😖 | confounded |
| 🏍 | racing_motorcycle, motorcycle, motorbike | ⚗ | alembic |
| 👅 | tongue | ⛪ | church |
| 🌇 | city_sunrise | 🚞 | mountain_railway |
| 🗿 | moyai | ㊙️ | secret |
| 🎆 | fireworks | 💎 | gem |
| 👊 | facepunch, punch | 🎯 | dart |
| 👻 | ghost | 🏎 | racing_car, formula_one, f1 |
| 👂 | ear | ✍ | writing, writing_hand |
| ❣ | exclamation_heart | 💈 | barber |
| 💸 | money_with_wings | 😈 | smiling_imp |
| 🛂 | passport_control | ⏺ | black_circle_for_record |
| 👝 | pouch | 🆒 | cool |
| ⚰ | coffin, funeral, casket | 🎢 | roller_coaster |
| ⛩ | shinto_shrine, kami_no_michi | 🏓 | table_tennis, ping_pong |
| 💰 | moneybag | 🎊 | confetti_ball |
| 🕉 | om_symbol, pranava, aumkara, omkara | 📅 | date |
| 🌤 | white_sun_small_cloud | 👗 | dress |
| 🐌 | snail | 📤 | outbox_tray |
| 📘 | blue_book | 🔹 | small_blue_diamond |
| ♐ | sagittarius | 🔸 | small_orange_diamond |
| 🍒 | cherries | 🌽 | corn |
| 🔉 | sound | 😀 | grinning |
| 🏂 | snowboarder | 👚 | womans_clothes |
| 🚅 | bullettrain_front | 🍂 | fallen_leaf |
| ➕ | heavy_plus_sign | 🦁 | lion_face, cute_lion, timid_lion |
| 🏀 | basketball | 🐬 | dolphin, flipper |
| 🐸 | frog | 🚘 | oncoming_automobile |
| 📶 | signal_strength | 💆 | massage |
| ☀️ | sunny | 📖 | book, open_book |
| 📓 | notebook | 🗡 | dagger, dagger_knife, knife_weapon |
| 🐨 | koala | 💼 | briefcase |
| 👆 | point_up_2 | 👭 | two_women_holding_hands |
| 🤒 | sick, ill, thermometer_face | 💐 | bouquet |
| 🍠 | sweet_potato | 🛬 | airplane_arriving, airplane_arrival, landing |
| 🚗 | car, red_car | 🐝 | bee, honeybee |
| ⏸ | double_vertical_bar | ☢ | radioactive, radioactive_symbol, radioactive_sign |
| 🔪 | hocho, knife | 🔼 | arrow_up_small |
| ♊ | gemini | 💥 | boom, collision |
| 🎹 | musical_keyboard | 🌯 | burrito, wrap |
| 🔈 | speaker | 🔺 | small_red_triangle |
| 💨 | dash | 🛳 | passenger_ship |
| 🗃 | card_file_box | 🕜 | clock130 |
| 🙀 | scream_cat | 🐍 | snake |
| ㊗️ | congratulations | 🔗 | link |
| 🏛 | classical_building | 👿 | imp |
| 🏓 | table_tennis, ping_pong | 🗄 | file_cabinet |
| 👁🗨 | eye_in_speech_bubble, i_am_a_witness | 🔵 | large_blue_circle |
| 🏁 | checkered_flag | 🚗 | car, red_car |
| 🚶 | walking | 🐱 | cat |
| 🍻 | beers | 🚉 | station |
| ♥️ | hearts | ♣️ | clubs |
| 🐾 | feet, paw_prints | 🌿 | herb |
| 📄 | page_facing_up | 👃 | nose |
| 🌙 | crescent_moon | 🌁 | foggy |
| 🏭 | factory | 🎈 | balloon |
| 🚄 | bullettrain_side | 😥 | disappointed_relieved |
| ⛱ | planted_umbrella, umbrella_on_ground | 🗑 | wastebasket |
| 😨 | fearful | 🎛 | control_knobs |
| 🏩 | love_hotel | ✍ | writing, writing_hand |
| ♍ | virgo | 🗼 | tokyo_tower |
| ⛷ | skier | ⬇️ | arrow_down |
| 🕍 | synagogue, temple, jewish |
