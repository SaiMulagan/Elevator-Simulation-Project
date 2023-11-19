import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class PropertyManager {
    private Properties properties;

    public PropertyManager(String propertiesFilePath) {
        properties = new Properties();
        // Set default values
        setDefaultValues();

        if (propertiesFilePath != null) {
            try (FileInputStream fis = new FileInputStream(propertiesFilePath)) {
                properties.load(fis);
            } catch (IOException e) {
                System.out.println("Unable to read properties file, using default values.");
            }
        }
    }

    private void setDefaultValues() {
        properties.setProperty("structures", "linked");
        properties.setProperty("floors", "12");
        properties.setProperty("passengers", "0.03");
        properties.setProperty("elevators", "1");
        properties.setProperty("elevatorCapacity", "10");
        properties.setProperty("duration", "500");
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
