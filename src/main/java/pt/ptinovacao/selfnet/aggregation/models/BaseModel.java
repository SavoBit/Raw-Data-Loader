package pt.ptinovacao.selfnet.aggregation.models;

import com.datastax.driver.mapping.annotations.Transient;

import java.util.UUID;

/**
 * @author rui-d-pedro
 */
abstract public class BaseModel {
	@Transient
	protected UUID uuid;
}
