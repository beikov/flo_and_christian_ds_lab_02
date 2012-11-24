package ds02.server.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

public class Auction implements Cloneable, Serializable {

	private static final long serialVersionUID = 1L;
	private final long id;
	private final String description;
	private final String user;
	private final Calendar endTimestamp;
	private final Calendar beginTimestamp;

	private BigDecimal bidValue = BigDecimal.ZERO;
	private String bidUser;

	public Auction(long id, String description, String user,
			Calendar endTimestamp) {
		this.beginTimestamp = Calendar.getInstance();
		this.id = id;
		this.description = description;
		this.user = user;
		this.endTimestamp = endTimestamp;
	}

	public long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public String getUser() {
		return user;
	}

	public Calendar getEndTimestamp() {
		return endTimestamp;
	}

	public BigDecimal getBidValue() {
		return bidValue;
	}

	public void setBidValue(BigDecimal bidValue) {
		this.bidValue = bidValue;
	}

	public String getBidUser() {
		return bidUser;
	}

	public void setBidUser(String bidUser) {
		this.bidUser = bidUser;
	}

	public Calendar getBeginTimestamp() {
		return beginTimestamp;
	}

	@Override
	public Auction clone() {
		try {
			synchronized (this) {
				return (Auction) super.clone();
			}
		} catch (CloneNotSupportedException ex) {
			return null;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Auction other = (Auction) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
