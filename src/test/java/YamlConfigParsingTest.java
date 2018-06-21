import org.yaml.snakeyaml.Yaml;
import pt.ptinovacao.selfnet.aggregation.configuration.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Force on 02/03/2017.
 */
public class YamlConfigParsingTest {

	public static void main(String... args) {
		String filepath = "./src/rdl/src/main/resources/rdl-config.yaml";

		Yaml conf = new Yaml();

		try {
			InputStream in = Files.newInputStream(Paths.get(filepath));
			Configuration config = conf.loadAs(in, Configuration.class);
			System.out.println(config.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
