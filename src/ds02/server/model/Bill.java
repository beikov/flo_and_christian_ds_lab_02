package ds02.server.model;

import java.io.Serializable;

public class Bill implements Serializable {

	private static final long serialVersionUID = 1L;
	private final long auctionId;
	private final double strikePrice;
	private final double feeFixed;
	private final double feeVariable;
	private final double feeTotal;

	public Bill(long auctionId, double strikePrice, double feeFixed,
			double feeVariable, double feeTotal) {
		super();
		this.auctionId = auctionId;
		this.strikePrice = strikePrice;
		this.feeFixed = feeFixed;
		this.feeVariable = feeVariable;
		this.feeTotal = feeTotal;
	}

	public long getAuctionId() {
		return auctionId;
	}

	public double getStrikePrice() {
		return strikePrice;
	}

	public double getFeeFixed() {
		return feeFixed;
	}

	public double getFeeVariable() {
		return feeVariable;
	}

	public double getFeeTotal() {
		return feeTotal;
	}

}
