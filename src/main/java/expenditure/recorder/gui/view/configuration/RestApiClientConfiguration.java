package expenditure.recorder.gui.view.configuration;

import java.util.Objects;
import java.util.Properties;

public class RestApiClientConfiguration {
    private final String host;
    private final int port;
    private final String username;
    private final String password;

    private RestApiClientConfiguration(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static RestApiClientConfiguration from(Properties properties) {
        String host = properties.getProperty("host");
        Integer port = Integer.parseInt(properties.getProperty("port"));
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        Objects.requireNonNull(host);
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        return new RestApiClientConfiguration(host, port, username, password);
    }
}
