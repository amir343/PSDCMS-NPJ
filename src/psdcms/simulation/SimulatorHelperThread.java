package psdcms.simulation;

import java.util.Vector;

/**
 * This class contains of a queue of tasks for a certain simulation thread.
 * After computation it will feedback the result by updating the user interface.
 * 
 * @author Amir Moulavi (amir.moulavi@gmail.com)
 * @author Uwe Dauernheim (uwe@dauernheim.net)
 * @version 1.0
 */
public class SimulatorHelperThread extends Thread {

	/**
	 * A queue that holds all posted tasks until they are proceeded.
	 */
	private Vector<Calculations> queue;
	/**
	 * The simulator class
	 */
	private Simulator Sim;

	/**
	 * The constructor taking the simulator and initializing the queue.
	 * 
	 * @param sim
	 *            The simulator
	 */
	public SimulatorHelperThread(Simulator sim) {
		Sim = sim;
		queue = new Vector<Calculations>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while (true) {
			if (queue != null && queue.size() > 0) {
				Calculations obj = (Calculations) queue.get(0);
				queue.remove(0);
				generateResult(obj);
			}
		}
	}

	/**
	 * The actual computation algorithm. The result is send back to the
	 * interface component immediately.
	 * 
	 * @param cal
	 *            The calculation job description
	 */
	private void generateResult(Calculations cal) {
		int sum = 0;

		for (int i = 0; i < cal.row.length; ++i) {
			sum += cal.row[i] * cal.col[i];
		}

		Sim.sendResult(sum, cal.RowIndex, cal.ColIndex);
		// System.out.println("Simulator: row " + cal.RowIndex + ", col " +
		// cal.ColIndex + " result: " + sum);

	}

	/**
	 * Adds a task to the task queue.
	 * 
	 * @param row
	 *            The row of the matrix for the task
	 * @param col
	 *            The column of the matrix for the task
	 * @param i
	 *            The row index of the matrix element for the task
	 * @param j
	 *            The column index of the matrix element for the task
	 */
	protected void add(int[] row, int[] col, int i, int j) {
		queue.add(new Calculations(row, col, i, j));
	}

	/**
	 * This class contains the task specific entities.
	 * 
	 * @author Amir Moulavi <amir.moulavi@gmail.com>
	 * @author Uwe Dauernheim <uwe@dauernheim.net>
	 * @version 1.0
	 */
	private class Calculations {
		/**
		 * The row of the matrix for the task
		 */
		public int[] row;
		/**
		 * The column of the matrix for the task
		 */
		public int[] col;
		/**
		 * The row index of the matrix element for the task
		 */
		public int RowIndex;
		/**
		 * The column index of the matrix element for the task
		 */
		public int ColIndex;

		/**
		 * The constructor setting the matrix row, column and element.
		 * 
		 * @param row
		 *            The row of the matrix for the task
		 * @param col
		 *            The column of the matrix for the task
		 * @param i
		 *            The row index of the matrix element for the task
		 * @param j
		 *            The column index of the matrix element for the task
		 */
		public Calculations(int[] row, int[] col, int i, int j) {
			this.row = row;
			this.col = col;
			this.RowIndex = i;
			this.ColIndex = j;
		}
	}
}
