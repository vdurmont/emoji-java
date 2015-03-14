# Changelog

## v1.1.0

* Update of the emoji database
* Adding support for HTML hexadecimal:
  * `Emoji#getHtmlHexadecimal`
  * `EmojiParser#parseToHtmlHexadecimal`
* The old HTML support is now HTML decimal:
  * Deprecating `Emoji#getHtml` (replaced by `Emoji#getHtmlDecimal`)
  * Deprecating `EmojiParser#parseToHtml` (replaced by `EmojiParser#parseToHtmlDecimal`)

## v1.0.0

First release.
