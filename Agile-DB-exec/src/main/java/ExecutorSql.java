import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.flywaydb.core.api.configuration.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Mojo(name = "Executor-sql", defaultPhase = LifecyclePhase.COMPILE)
public class ExecutorSql extends AbstractMojo {

    public static final String JDBC_URL = "jdbcUrl";
    public static final String PASSWORD = "password";
    public static final String USER = "user";
    public static final String DB_SQLS = "/db/sqls/";
    public static final String SRC_RESOURCE_PATH = "/src/main/resources/application.properties";
    public static final String APPLICATION_PROPERTIES = "application.properties";
    @Value("${user}")
    String user;

    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("*****************************************");
        System.out.println("******** STARTING SQL EXECUTION *********");
        System.out.println("*****************************************");
        final String dir = System.getProperty("user.dir");
        System.out.println("current dir = " + dir);

        Properties prop = new Properties();
        String propFileName = APPLICATION_PROPERTIES;

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(dir+ SRC_RESOURCE_PATH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final String user = prop.getProperty(USER);
        System.out.println("user "+ user);
        final String jdbcUrl = prop.getProperty(JDBC_URL);
        final String password = prop.getProperty(PASSWORD);
        final ClassicConfiguration configuration = new ClassicConfiguration();
        configuration.setDataSource(jdbcUrl, user, password);
        configuration.setLocations(new Location(dir+ DB_SQLS));
        final Flyway flyway = new Flyway(configuration);
        flyway.migrate();
    }

    public static void main (String args[]) throws MojoExecutionException, MojoFailureException {
        ExecutorSql executorSql = new ExecutorSql();
        executorSql.execute();
    }
}
