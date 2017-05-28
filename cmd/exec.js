Runtime = Java.type("java.lang.Runtime");
IOUtils = Java.type("org.apache.commons.io.IOUtils");
StringWriter = Java.type("java.io.StringWriter");
System = Java.type("java.lang.System");
String = Java.type("java.lang.String");

function execute(ctx) {
    runtime = Runtime.getRuntime();
    command = String.join(" ", ctx.getArgs());
    process = runtime.exec(command);
    writer = new StringWriter();
    IOUtils.copy(process.getInputStream(), writer, "UTF-8");
    response = writer.toString();
    while (response.length > 1990) {
        msg = "```\n" + response.substring(0, 1990) + "\n```";
        response = response.substring(1990);
        ctx.msg(msg);
    }
    ctx.msg("```\n" + response + "\n```");
}