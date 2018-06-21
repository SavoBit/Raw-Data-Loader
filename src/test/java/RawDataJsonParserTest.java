import pt.ptinovacao.selfnet.aggregation.util.Parser;
import pt.ptinovacao.selfnet.aggregation.constants.ModelClasses;
import pt.ptinovacao.selfnet.aggregation.models.BaseModel;
import pt.ptinovacao.selfnet.aggregation.models.Counter;
import pt.ptinovacao.selfnet.aggregation.models.Event;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author rui-d-pedro
 */
public class RawDataJsonParserTest {
	public static void main(String... args) {
		String jsonFMAString = RawDataGeneratorTest.genFMA();

		String jsonSNORTString = RawDataGeneratorTest.genSNORT();

		ConcurrentSkipListSet<BaseModel> models = new ConcurrentSkipListSet<>();

		Parser.parse(jsonFMAString).forEach(m -> models.add(m));
		Parser.parse(jsonSNORTString).forEach(m -> models.add(m));

		models.stream().sequential().forEach(model -> {
			System.out.printf("model: %s\n", model.getClass().getCanonicalName());
			switch(ModelClasses.valueOfEnum(model.getClass())){
				case COUNTER_CLASS:
					Counter r = (Counter) model;
					System.out.println(r.getClass());
					break;
				case EVENT_CLASS:
					Event e = (Event) model;
					System.out.println(e.getClass());
					break;
				case DEFAULT:
					System.out.println("DEFAULT");
					break;
			}
		});
	}
}
