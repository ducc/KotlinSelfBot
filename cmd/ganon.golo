module cmd.GoloAnon

function run = |ctx| {
    let env = gololang.EvaluationEnvironment()
    let code = java.lang.String.join(" ", ctx: getArgs())
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
