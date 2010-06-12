package psdcms.interfaces;

/**
 * The UserTask Publishing interface.
 * 
 * @author Amir Moulavi (amir.moulavi@gmail.com)
 * @author Uwe Dauernheim (uwe@dauernheim.net)
 * @version 1.0
 */
public interface UTPublishTask {
	/**
	 * Publishing the user task to simulator.
	 * 
	 * @param row
	 *            The matrix's row for the task
	 * @param col
	 *            The matrix's column for the task
	 * @param i
	 *            The row index of the matrix element for the task
	 * @param j
	 *            The column index of the matrix element for the task
	 */
	public void utPublishTask(int[] row, int[] col, int i, int j);
}
