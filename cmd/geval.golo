module cmd.GoloEval

function run = |ctx| {
    let env = gololang.EvaluationEnvironment()
    let code = java.lang.String.join(" ", ctx: getArgs())
    let values = java.util.HashMap(): add("ctx", ctx): add("jda", ctx: getJda()): add("event", ctx: getEvent())
    try {
	let result = env: run(code, values)
        println("goloeval result: " + result)
	if (result != null) {
	    ctx: msg(result: toString())
	}
    } catch (e) {
	ctx: msg("ERR:" + e: getMessage())
	e: printStackTrace()
    }
}
