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

// For production, set the log directory in ~home/kbe/logs, otherwise, use the setting from BuildSettings
def targetDir = BuildSettings.TARGET_DIR
def grails_env = System.getProperty("grails.env")
println("Configuring logging for $grails_env environment")
if(grails_env == "prod") {
    def userHome = System.getProperty("user.home")
    targetDir = "$userHome/kbe/logs"
}
println("Setting the log directory to $targetDir")

appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName('UTF-8')
        pattern = basicPattern
    }
}

appender('ERRORFILE', FileAppender) {
    file = "${targetDir}/error.log"
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = defaultPattern
    }
}

appender('INFOFILE', FileAppender) {
    file = "${targetDir}/info.log"
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = defaultPattern
    }
}

if (Environment.isDevelopmentMode() && targetDir != null) {
    appender("FULL_STACKTRACE", FileAppender) {
        file = "${targetDir}/stacktrace.log"
        append = true
        encoder(PatternLayoutEncoder) {
            pattern = "%level %logger - %msg%n"
        }
    }
    logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
}
root(ERROR, ['ERRORFILE'])

root(INFO, ['INFOFILE'])
println("Log configuration complete")
