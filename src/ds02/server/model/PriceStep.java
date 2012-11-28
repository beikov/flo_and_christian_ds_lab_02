package ds02.server.model;

import java.io.Serializable;

public class PriceStep implements Serializable, Comparable<PriceStep> {

	private static final long serialVersionUID = 1L;

	private final double startPrice;
	private final double endPrice;
	private final double fixedPrice;
	private final double variablePricePercent;

	public PriceStep(double startPrice, double endPrice, double fixedPrice,
			double variablePricePercent) {
		super();
		this.startPrice = startPrice;
		this.endPrice = endPrice;
		this.fixedPrice = fixedPrice;
		this.variablePricePercent = variablePricePercent;
	}

	public double getStartPrice() {
		return startPrice;
	}

	public double getEndPrice() {
		return endPrice;
	}

	public double getFixedPrice() {
		return fixedPrice;
	}

	public double getVariablePricePercent() {
		return variablePricePercent;
	}

	@Override
	public int compareTo(PriceStep priceStep) {
		return Double.valueOf(this.startPrice).compareTo(priceStep.startPrice);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(endPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(startPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PriceStep other = (PriceStep) obj;
		if (Double.doubleToLongBits(endPrice) != Double
				.doubleToLongBits(other.endPrice))
			return false;
		if (Double.doubleToLongBits(startPrice) != Double
				.doubleToLongBits(other.startPrice))
			return false;
		return true;
	}

}
