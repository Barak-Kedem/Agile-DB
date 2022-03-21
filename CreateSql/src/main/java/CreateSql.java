import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@Mojo(name = "CreateSql", defaultPhase = LifecyclePhase.COMPILE)
public class CreateSql extends AbstractMojo {

    private static final String DB_SQLS = "/db/migration/";
    private static final String SRC_RESOURCE_PATH = "/src/main/resources/application.properties";
    @Value("${user}")
    String user;

    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("*****************************************");
        System.out.println("******** STARTING SQL EXECUTION *********");
        System.out.println("*****************************************");
        final String dir = System.getProperty("user.dir");

        final Properties prop = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(dir + SRC_RESOURCE_PATH);
            prop.load(inputStream);
            final String developerName = prop.getProperty("developer.name");

            final String migrationPath = dir + DB_SQLS;
            final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
            final LocalDateTime now = LocalDateTime.now();

            final String migrationSqlName = getMigrationSqlName(developerName, dtf, now);
            final File sql = new File(migrationPath, migrationSqlName);
            sql.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getMigrationSqlName(String developerName, DateTimeFormatter dtf, LocalDateTime now) {
        return "V" + dtf.format(now) + "__" + developerName + ".sql";
    }
}
