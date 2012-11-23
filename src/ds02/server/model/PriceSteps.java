package ds02.server.model;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

public class PriceSteps implements Serializable {

	private static final long serialVersionUID = 1L;

	private Set<PriceStep> priceSteps = new TreeSet<PriceStep>();

	public Set<PriceStep> getPriceSteps() {
		return priceSteps;
	}

	public void setPriceSteps(Set<PriceStep> priceSteps) {
		this.priceSteps = priceSteps;
	}
}
