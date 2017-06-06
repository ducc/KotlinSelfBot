module cmd.GoloAnon

let std = """
function channel = |f| -> |args...| {
  args: get(0): getEvent(): getChannel(): sendMessage(f: invoke(args)): queue()
}
"""

function run = |ctx| {
    let env = gololang.EvaluationEnvironment()
    let code = std + "\n" + java.lang.String.join(" ", ctx: getArgs())
    let mod = env: anonymousModule(code)
    let execute = fun("run", mod) 
    try {
	let result = execute(ctx)
        println("result: " + result)
	if (result != null) {
	    ctx: msg(result: toString())
	}
    } catch (e) {
	ctx: msg("ERR: " + e: getMessage())
	e: printStackTrace()
    }
}
