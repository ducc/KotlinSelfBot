Runtime = Java.type("java.lang.Runtime");
IOUtils = Java.type("org.apache.commons.io.IOUtils");
FileUtils = Java.type("org.apache.commons.io.FileUtils");
StringWriter = Java.type("java.io.StringWriter");
System = Java.type("java.lang.System");
String = Java.type("java.lang.String");
File = Java.type("java.io.File");

function sendLongMsg(ctx, msg) {
    if (msg === null || msg.length === 0 || msg.equals("ERR:")) {
        return;
    }
    if (msg.length <= 1990) {
        ctx.msg("```\n" + msg + "\n```")
        return
    }
    while (msg.length > 1990) {
        m = "```\n" + msg.substring(0, 1990) + "\n```";
        msg = msg.substring(1990);
        ctx.msg(m);
    }
    sendLongMsg(ctx, msg)
}

function run(ctx) {
    runtime = Runtime.getRuntime();
    command = "module selfbot.Eval\n" + String.join(" ", ctx.getArgs());
    file = new File("exec.golo");
    if (file.exists()) {
        file.delete();
    }
    FileUtils.writeStringToFile(file, command);
    process = runtime.exec("/usr/bin/golo/bin/golo golo --files  exec.golo");
    errWriter = new StringWriter();
    IOUtils.copy(process.getErrorStream(), errWriter, "UTF-8");
    sendLongMsg(ctx, "ERR:" + errWriter.toString());
    inWriter = new StringWriter();
    IOUtils.copy(process.getInputStream(), inWriter, "UTF-8");
    sendLongMsg(ctx, inWriter.toString());
}
