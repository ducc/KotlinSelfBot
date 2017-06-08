module cmd.LineNumbers

import java.lang.String

function run = |ctx| {
  let content = ctx: getEvent(): getMessage(): getContentDisplay()
  let builder = java.lang.StringBuilder()
  var first = true
  var lang = ""
  var num = 1
  foreach line in content: split("\n") {
    if (first) {
      first = false
      let args = line: split(" ")
      if (args: length() > 1) {
        lang = args: get(1)
      }
      continue
    }
    let prefix = "/* " + format("%02d", num) + " */ "
    builder: append(prefix): append(line): append("\n")
    num = num + 1
  }
  ctx: getEvent(): getMessage(): editMessage("```" + lang + "\n" + builder: toString() + "\n```"): queue()
}

