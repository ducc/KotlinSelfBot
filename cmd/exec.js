Runtime = Java.type("java.lang.Runtime");
IOUtils = Java.type("org.apache.commons.io.IOUtils");
FileUtils = Java.type("org.apache.commons.io.FileUtils");
StringWriter = Java.type("java.io.StringWriter");
System = Java.type("java.lang.System");
String = Java.type("java.lang.String");
File = Java.type("java.io.File");

function run(ctx) {
    runtime = Runtime.getRuntime();
    command = String.join(" ", ctx.getArgs());
    file = new File("exec.sh");
    if (file.exists()) {
        file.delete();
    }
    FileUtils.writeStringToFile(file, command);
    process = runtime.exec("sudo sh exec.sh");
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
