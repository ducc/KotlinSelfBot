module selfbot.Eval
function cmd = |name| -> |f| -> |args...| { println(name + " " + f: invoke(args)) } @cmd("dab") function dab = { return 5 } function main = |args| { dab() }