# Changelog

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
