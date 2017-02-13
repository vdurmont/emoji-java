# Changelog

## v3.2.0

- Fixed Poland flag (thanks @Sheigutn)
- Improvements to the smile emojis (thanks @miquelbeltran)
- Add a bunch of emojis from Apple iOS 10.2 release
- Fix some missing fitzpatrick modifiers
- Add an `EmojiManager.isOnlyEmojis()` method

## v3.1.3

- Removed all variance selectors from the JSON database. Thanks @roberterdin !

## v3.1.2

- Additions and updates to the emoji database (victory hand now supports fitzpatrick, adds Saint Vincent Grenadines' flag, add the regional indicator symbols). Thanks @lologist !
- Force the database to be loaded in UTF-8.
- Enable the extension of the `EmojiParser` class.

## v3.1.1

- Add the ability to provide a custom `EmojiTransformer` that will enable developers to add their custom emoji replacement methods. Thanks @freva !

## v3.1.0

- Add fitzpatrick support for 👃 ("nose") and 👂 ("ear")
- Fix duplicated "sunglasses" alias
- Performance improvements (using a Trie structure)
- Parsing support for multiple emojis (such as "family_man_woman_boy")
- Fix `EmojiManager.getAll()` that returned some duplicates
- Use a BufferedReader to load the database

## v3.0.0

Update the emoji database to support the additions of iOS 9.1

## v2.2.1

Fix the `htmlDec` and `htmlHex` codes for the multiple emojis (such as `family (man, man, girl, boy)`)

## v2.2.0

Rollback dependency org.json:json to 20140107 to keep the compatibility with Java 6 & 7

## v2.1.0

* Add methods:
  * `EmojiParser#removeAllEmojis(String)`
  * `EmojiParser#removeAllEmojisExcept(String, Collection<Emoji>)`
  * `EmojiParser#removeEmojis(String, Collection<Emoji>)`
* Upgrade dependency org.json:json

## v2.0.1

Bug fix on the :-1: emoji

## v2.0.0

* Update of the emoji database
  * Add 14 new family emojis (man_man_boy, woman_woman_girl, etc.)
  * Add 4 new couple emojis
  * Add the "vulcan_salute" and "middle_finger" emojis
  * Add 198 flags
* Addition of the support for the diversity emojis (Fitzpatrick modifiers)
* Removal of the deprecated methods `Emoji#getHtml` and `EmojiParser#parseToHtml`
* Improvements in the javadoc

## v1.1.1

Closing the stream used to read the emoji database in `EmojiManager.java`

## v1.1.0

* Update of the emoji database
* Adding support for HTML hexadecimal:
  * `Emoji#getHtmlHexadecimal`
  * `EmojiParser#parseToHtmlHexadecimal`
* The old HTML support is now HTML decimal:
  * Deprecating `Emoji#getHtml` (replaced by `Emoji#getHtmlDecimal`)
  * Deprecating `EmojiParser#parseToHtml` (replaced by `EmojiParser#parseToHtmlDecimal`)

## v1.0.1

Bug fix on the :+1: emoji

## v1.0.0

First release.
