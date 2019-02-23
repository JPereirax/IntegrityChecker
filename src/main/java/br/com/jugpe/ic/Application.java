package br.com.jugpe.ic;

import br.com.jugpe.ic.properties.PropertiesManager;
import br.com.jugpe.ic.storage.FileStorage;
import br.com.jugpe.ic.thread.IntegrityThread;
import lombok.Getter;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class Application {

    @Getter
    private static final FileStorage fileStorage = new FileStorage();

    @Getter
    private static final Logger logger = Logger.getLogger("IntegrityChecker");

    public static void main(String[] args) throws IOException {
        Properties properties = new PropertiesManager().getProperties();

        new IntegrityThread(properties).start();
    }

}