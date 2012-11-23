package ds02.server.model;

import java.math.BigDecimal;
import java.util.Calendar;

public class Auction implements Cloneable{
    private final int id;
    private final String description;
    private final String user;
    private final Calendar endTimestamp;
    private BigDecimal bidValue = BigDecimal.ZERO;
    private String bidUser;

    public Auction(int id, String description, String user, Calendar endTimestamp) {
        this.id = id;
        this.description = description;
        this.user = user;
        this.endTimestamp = endTimestamp;
    }

    public int getId() {
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

    @Override
    public Auction clone() {
        try {
            synchronized(this){
                return (Auction) super.clone();
            }
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Auction other = (Auction) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
