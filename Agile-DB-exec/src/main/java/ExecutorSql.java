import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.flywaydb.core.api.configuration.Configuration;
import org.springframework.beans.factory.annotation.Value;

    @Mojo(name = "Executor-sql", defaultPhase = LifecyclePhase.COMPILE)
public class ExecutorSql extends AbstractMojo {

    @Value("${jdbcUrl}")
    String jdbcUrl;

    @Value("${user}")
    String user;

    @Value("${password}")
    String password;


    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("*****************************************");
        System.out.println("******** STARTING SQL EXECUTION *********");
        System.out.println("*****************************************");
        System.out.println("user "+ user);

        final ClassicConfiguration configuration = new ClassicConfiguration();
        configuration.setDataSource(jdbcUrl, user, password);
        final Flyway flyway = new Flyway(configuration);
        flyway.migrate();
    }
}
