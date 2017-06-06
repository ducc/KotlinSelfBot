ScriptEngineManager = Java.type("javax.script.ScriptEngineManager");
String = Java.type("java.lang.String");
System = Java.type("java.lang.System");

function execute(ctx) {
    engine = new ScriptEngineManager().getEngineByName("nashorn");
    engine.put("ctx", ctx);
    code = String.join(" ", ctx.getArgs());
    try {
        result = engine.eval(code);
        System.out.println(result);
        if (result === null || result === undefined || result === "null" || result === "undefined") {
            return;
        }
        ctx.msg(result);
    } catch (e) {
        ctx.msg(e.getMessage());
        e.printStackTrace();
    }
}