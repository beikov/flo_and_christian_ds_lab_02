package ds02.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Bill implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Queue<BillLine> billLines = new ConcurrentLinkedQueue<BillLine>();

	public void add(BillLine billLine) {
		billLines.add(billLine);
	}

	public Collection<BillLine> getBillLines() {
		return new ArrayList<BillLine>(billLines);
	}
}
