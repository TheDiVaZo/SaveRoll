package saveroll.logging;


import java.util.logging.Level;
import java.util.logging.Logger;

public class JULHandler implements LoggerHandler {

        private Logger logger;

        private boolean isDebug = false;

        public JULHandler(Logger logger) {
                this.logger = logger;
        }

        @Override
        public void setDebugMode(boolean debugMode) {
                isDebug = debugMode;
        }

        @Override
        public void debug(Object message, Throwable throwable, Object... placeholders) {
                if(!isDebug) return;
                logger.log(Level.INFO, message.toString(), placeholders);
                if (throwable != null) throwable.printStackTrace();

                }

        @Override
        public void info(Object message, Throwable throwable, Object... placeholders) {
                logger.log(Level.INFO, message.toString(), placeholders);
                if (throwable != null) throwable.printStackTrace();
                }

        @Override
        public void warn(Object message, Throwable throwable, Object... placeholders) {
                logger.log(Level.WARNING, message.toString(), placeholders);
                if (throwable != null) throwable.printStackTrace();
                }

        @Override
        public void error(Object message, Throwable throwable, Object... placeholders) {
                logger.log(Level.SEVERE, message.toString(), placeholders);
                if (throwable != null) throwable.printStackTrace();
                }


                }
