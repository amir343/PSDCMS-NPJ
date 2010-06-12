package psdcms.interfaces;

/**
 * The ResultAnnounce interface.
 * 
 * @author Amir Moulavi (amir.moulavi@gmail.com)
 * @author Uwe Dauernheim (uwe@dauernheim.net)
 * @version 1.0
 */
public interface ResultAnnounce {
	/**
	 * Announcing the result to the user interface.
	 * 
	 * @param result
	 *            The matrix element's value
	 * @param row
	 *            The row index of the matrix element
	 * @param col
	 *            The column index of the matrix element
	 */
	public void resultAnnounce(int result, int row, int col);
}
