package pt.ptinovacao.selfnet.aggregation.constants;

import pt.ptinovacao.selfnet.aggregation.models.Counter;
import pt.ptinovacao.selfnet.aggregation.models.Event;

import java.util.Arrays;

/**
 * @author rui-d-pedro
 */
public enum ModelClasses {
	COUNTER_CLASS(Counter.class),
	EVENT_CLASS(Event.class),
	DEFAULT(Object.class);


	private Class aclass;

	ModelClasses(Class aclass) {
		this.setAclass(aclass);
	}

	public Class getAclass() {
		return aclass;
	}

	private void setAclass(Class aclass) {
		this.aclass = aclass;
	}

	/**
	 * A custom valueOf method, this one uses the custom string names used on the enum.
	 * If not found it returns the DEFAULT enum member.
	 *
	 * @param name a String that hopefully represents a ModelClasses enum member.
	 * @return the corresponding enum member or the DEFAULT(Object class) if not found.
	 */
	public static ModelClasses valueOfEnum(String name) {
		return Arrays.stream(ModelClasses.values())
					 .parallel()
					 .filter(e -> e.toString().equals(name))
					 .distinct()
					 .findFirst()
					 .orElseGet(() -> DEFAULT);
	}

	/**
	 * A custom valueOf method, that given a Class object tries to find its corresponding enum member.
	 * If not found it returns the DEFAULT enum member.
	 *
	 * @param aclass a Class that hopefully represents a ModelClasses enum member
	 * @return the corresponding enum member or the DEFAULT(Object class) if not found
	 */
	public static ModelClasses valueOfEnum(Class aclass) {
		return Arrays.stream(ModelClasses.values())
					 .parallel()
					 .filter(e -> e.getAclass().equals(aclass))
					 .distinct()
					 .findFirst()
					 .orElseGet(() -> DEFAULT);
	}
}
