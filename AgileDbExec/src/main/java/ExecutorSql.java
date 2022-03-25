import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import java.io.IOException;
import java.util.Properties;

@Mojo(name = "ExecutorSql", defaultPhase = LifecyclePhase.COMPILE)
public class ExecutorSql extends AbstractMojo {

    private static final String JDBC_URL = "jdbcUrl";
    private static final String PASSWORD = "password";
    private static final String USER = "user";
    private static final String DB_SQLS = "/db/migration/";
    private static final String SRC_RESOURCE_PATH = "/src/main/resources/application.properties";

    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("*****************************************");
        System.out.println("******** STARTING SQL EXECUTION *********");
        System.out.println("*****************************************");
        final Properties prop;
        try {
            Class.forName("org.postgresql.Driver");
            prop = PropertyReader.getProperties(System.getProperty("user.dir"));

            final ClassicConfiguration configuration = buildConfiguration(prop);
            final Flyway flyway = new Flyway(configuration);
            flyway.repair();
            flyway.migrate();
        } catch (IOException | ClassNotFoundException e) {
           throw new RuntimeException("Failed running Sql: "+ e.getMessage(), e);
        }
    }

    private ClassicConfiguration buildConfiguration(Properties prop) {
        final String user = prop.getProperty(USER);
        final String jdbcUrl = prop.getProperty(JDBC_URL);
        final String password = prop.getProperty(PASSWORD);
        final ClassicConfiguration configuration = new ClassicConfiguration();
        configuration.setDataSource(jdbcUrl, user, password);
        final String dir = System.getProperty("user.dir");
        final String migrationPath = "filesystem:" + dir + DB_SQLS;
        configuration.setLocations(new Location(migrationPath));//dir+ DB_SQLS));
        return configuration;
    }

    public static void main(String args[]) throws MojoExecutionException, MojoFailureException {
        ExecutorSql executorSql = new ExecutorSql();
        executorSql.execute();
    }
}
