package ds02.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class PriceSteps implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Collection<PriceStep> priceSteps;

	public PriceSteps(Collection<PriceStep> priceSteps) {
		this.priceSteps = Collections
				.unmodifiableList(new ArrayList<PriceStep>(priceSteps));
	}

	public Collection<PriceStep> getPriceSteps() {
		return priceSteps;
	}
}
