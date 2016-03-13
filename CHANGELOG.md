# Changelog

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
