import grails.util.BuildSettings
import grails.util.Environment
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.Charset

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

// See http://logback.qos.ch/manual/groovy.html for details on configuration
def basicPattern = "%level %logger - %msg%n "
def defaultPattern = " %level %logger - %msg%n"

// For production, set the log directory in ~home/kbe/log, otherwise, use the setting from BuildSettings
def targetDir = BuildSettings.TARGET_DIR
def grails_env = System.getProperty("grails.env")
println("Configuring logging for $grails_env environment")
if(grails_env == "prod") {
    def userHome = System.getProperty("user.home")
    targetDir = "$userHome/kbe/logs"
}
println("Setting log directory to $targetDir")

println("creating stdout appender")
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')
        pattern = basicPattern
    }
}

println("creating error file appender")
appender('ERRORFILE', FileAppender) {
    file = "${targetDir}/error.log"
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = defaultPattern
    }
}

println("creating info file appender")
appender('INFOFILE', FileAppender) {
    file = "${targetDir}/info.log"
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = defaultPattern
    }
}


println("creating stacktrace appender")
if (Environment.isDevelopmentMode() && targetDir != null) {
    appender("FULL_STACKTRACE", FileAppender) {
        file = "${targetDir}/stacktrace.log"
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = "%level %logger - %msg%n"
        }
    }
    println("creating logger")
    logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
}
println("creating root.error")
root(ERROR, ['ERRORFILE'])
println("creating root.info")
root(INFO, ['INFOFILE'])
println("All done.")
