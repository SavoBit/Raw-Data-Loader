import com.datastax.driver.mapping.annotations.Table;
import pt.ptinovacao.selfnet.aggregation.models.Counter;

/**
 * Created by Force on 01/03/2017.
 */
public class TableAnnotationRuntimeModificationsTest {
	public static void main(String... args) {

		Table t = (Table) Counter.class.getAnnotations()[0];

		System.out.println(t.name());

		Counter.setTable("other");

		t = (Table) Counter.class.getAnnotations()[0];

		System.out.println(t.name());
	}
}
