# Changelog

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
