// Script to convert a JSON file with "character" emojis to a JSON file with "source code" emojis
// This was probably a one-time thing but I'll commit the code in case it is useful in the future.
//
// Usage: `node charConverter.js`

const fs = require('fs');
const path = './src/main/resources/emojis.json';
const path2 = './src/main/resources/emojis2.json';
const f = fs.readFileSync(path, 'utf8');
const json = JSON.parse(f);

const newJson = json.reduce((acc, e) => {
  acc.push({
    emojiChar: e.emoji,
    emoji: convertCharStr2jEsc(e.emoji, ''),
    description: e.description,
    supports_fitzpatrick: e.supports_fitzpatrick || undefined,
    aliases: e.aliases,
    tags: e.tags,
  });
  return acc;
}, []);

const newFile = JSON.stringify(newJson, (key, value) => value, 2);
fs.writeFileSync(path2, newFile);



///////////////////////////////////////////////////////////////////
// Following functions copied from https://github.com/r12a/app-conversion/blob/gh-pages/conversionfunctions.js
// http://rishida.net/
///////////////////////////////////////////////////////////////////



function convertCharStr2jEsc(str, parameters) {
  // Converts a string of characters to JavaScript escapes
  // str: sequence of Unicode characters
  // parameters: a semicolon separated string showing ids for checkboxes that are turned on
  var highsurrogate = 0;
  var suppCP;
  var pad;
  var n = 0;
  var pars = parameters.split(';');
  var outputString = '';
  for (var i = 0; i < str.length; i++) {
    var cc = str.charCodeAt(i);
    if (cc < 0 || cc > 0xffff) {
      outputString +=
        '!Error in convertCharStr2UTF16: unexpected charCodeAt result, cc=' +
        cc +
        '!';
    }
    if (highsurrogate != 0) {
      // this is a supp char, and cc contains the low surrogate
      if (0xdc00 <= cc && cc <= 0xdfff) {
        suppCP = 0x10000 + ((highsurrogate - 0xd800) << 10) + (cc - 0xdc00);
        if (parameters.match(/cstyleSC/)) {
          pad = suppCP.toString(16);
          while (pad.length < 8) {
            pad = '0' + pad;
          }
          outputString += '\\U' + pad;
        } else if (parameters.match(/es6styleSC/)) {
          pad = suppCP.toString(16).toUpperCase();
          outputString += '\\u{' + pad + '}';
        } else {
          suppCP -= 0x10000;
          outputString +=
            '\\u' +
            dec2hex4(0xd800 | (suppCP >> 10)) +
            '\\u' +
            dec2hex4(0xdc00 | (suppCP & 0x3ff));
        }
        highsurrogate = 0;
        continue;
      } else {
        outputString +=
          'Error in convertCharStr2UTF16: low surrogate expected, cc=' +
          cc +
          '!';
        highsurrogate = 0;
      }
    }
    if (0xd800 <= cc && cc <= 0xdbff) {
      // start of supplementary character
      highsurrogate = cc;
    } else {
      // this is a BMP character
      //outputString += dec2hex(cc) + ' ';
      switch (cc) {
        case 0:
          outputString += '\\0';
          break;
        case 8:
          outputString += '\\b';
          break;
        case 9:
          if (parameters.match(/noCR/)) {
            outputString += '\\t';
          } else {
            outputString += '\t';
          }
          break;
        case 10:
          if (parameters.match(/noCR/)) {
            outputString += '\\n';
          } else {
            outputString += '\n';
          }
          break;
        case 13:
          if (parameters.match(/noCR/)) {
            outputString += '\\r';
          } else {
            outputString += '\r';
          }
          break;
        case 11:
          outputString += '\\v';
          break;
        case 12:
          outputString += '\\f';
          break;
        case 34:
          if (parameters.match(/noCR/)) {
            outputString += '\\"';
          } else {
            outputString += '"';
          }
          break;
        case 39:
          if (parameters.match(/noCR/)) {
            outputString += "\\'";
          } else {
            outputString += "'";
          }
          break;
        case 92:
          outputString += '\\\\';
          break;
        default:
          if (cc > 0x1f && cc < 0x7f) {
            outputString += String.fromCharCode(cc);
          } else {
            pad = cc.toString(16).toUpperCase();
            while (pad.length < 4) {
              pad = '0' + pad;
            }
            outputString += '\\u' + pad;
          }
      }
    }
  }
  return outputString;
}

function dec2hex4(textString) {
  var hexequiv = new Array(
    '0',
    '1',
    '2',
    '3',
    '4',
    '5',
    '6',
    '7',
    '8',
    '9',
    'A',
    'B',
    'C',
    'D',
    'E',
    'F'
  );
  return (
    hexequiv[(textString >> 12) & 0xf] +
    hexequiv[(textString >> 8) & 0xf] +
    hexequiv[(textString >> 4) & 0xf] +
    hexequiv[textString & 0xf]
  );
}
