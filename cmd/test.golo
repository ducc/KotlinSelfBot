module cmd.Test

function run = |ctx| {
    ctx: getEvent(): getChannel(): sendMessage("Testing, 1 2 3 `" + ctx + "`"): queue()
    println("Testing, 1 2 3" + ctx)
}
