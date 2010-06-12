package psdcms.frontend;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.api.control.LifeCycleController;
import org.objectweb.fractal.util.Fractal;
import org.objectweb.jasmine.jade.service.nicheOS.OverlayAccess;
import org.objectweb.jasmine.jade.util.FractalUtil;
import org.objectweb.jasmine.jade.util.NoSuchComponentException;

import psdcms.interfaces.FEPublishTask;
import psdcms.interfaces.ResultAnnounce;

import dks.niche.ids.ComponentId;
import dks.niche.interfaces.IdentifierInterface;
import dks.niche.interfaces.NicheAsynchronousInterface;
import dks.niche.interfaces.NicheComponentSupportInterface;

/**
 * This class is responsible for FrontEnd component. It implements
 * ResultAnnounce interface as its server interface and also it has
 * FEPublishTask member as its client interface
 * 
 * @author M. Amir Moulavi (moulavi@kth.se)
 * @author Uwe Dauernheim (uwe@dauernheim.net)
 * @version 1.0
 * 
 */

public class FrontendImpl implements ResultAnnounce, Runnable,
		BindingController, LifeCycleController {

	/**
	 * Component reference refers to itself
	 */
	private Component myself;
	/**
	 * Status ofthis component which is either STARTED or STOPPED
	 */
	private boolean status;
	/**
	 * Client interface
	 */
	private FEPublishTask fePublishTask;
	/**
	 * Global ID of this component in the system.
	 */
	private ComponentId myGlobalId;
	/**
	 * Used for handling the user interface
	 */
	private UserInterfaceThread uit;
	/**
	 * @see psdcms.frontend.UserInterface
	 */
	private UserInterface myUI;

	/**
	 * Niche Component Support Interface used for getting GlobalID
	 */
	NicheComponentSupportInterface nicheOSSupport;
	/**
	 * Niche Asynchronous Interface used for logging.
	 */
	NicheAsynchronousInterface logger;

	
	/**
	 * It generates the Graphical User Interface within a thread and put the component in STOPPED status 
	 */
	public FrontendImpl() {
		status = false;
		uit = new UserInterfaceThread(this);
		new Thread(uit).start();
	}

	/**
	 * For implementing the server interface functionality
	 * 
	 * @param a Matrix A
	 * @param b Matrix B
	 */
	public synchronized void PublishTask(int[][] a, int[][] b) {
		int[][] matrixA = a;
		int[][] matrixB = b;

		for (int i = 0; i < matrixA.length; ++i) {
			for (int j = 0; j < matrixB[0].length; ++j) {
				int[] rowB = new int[matrixB.length];

				for (int k = 0; k < matrixB.length; ++k) {
					rowB[k] = matrixB[k][j];
				}

				fePublishTask.fePublishTask(matrixA[i], rowB, i, j);
				// System.out.println("Sending row " + i + " col " + j);
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.objectweb.fractal.api.control.BindingController#listFc()
	 */
	public String[] listFc() {

		return new String[] { "component", "fePublishTask" };
	}

	/* (non-Javadoc)
	 * @see org.objectweb.fractal.api.control.BindingController#lookupFc(java.lang.String)
	 */
	public Object lookupFc(final String itfName)
			throws NoSuchInterfaceException {

		if (itfName.equals("fePublishTask"))
			return fePublishTask;
		else if (itfName.equals("component"))
			return myself;
		else
			throw new NoSuchInterfaceException(itfName);

	}

	/* (non-Javadoc)
	 * @see org.objectweb.fractal.api.control.BindingController#bindFc(java.lang.String, java.lang.Object)
	 */
	public void bindFc(final String itfName, final Object itfValue)
			throws NoSuchInterfaceException {
		if (itfName.equals("fePublishTask"))
			fePublishTask = (FEPublishTask) itfValue;
		else if (itfName.equals("component"))
			myself = (Component) itfValue;
		else
			throw new NoSuchInterfaceException(itfName);
	}

	/* (non-Javadoc)
	 * @see org.objectweb.fractal.api.control.BindingController#unbindFc(java.lang.String)
	 */
	public void unbindFc(final String itfName) throws NoSuchInterfaceException {
		if (itfName.equals("fePublishTask"))
			fePublishTask = null;
		else if (itfName.equals("component"))
			myself = null;
		else
			throw new NoSuchInterfaceException(itfName);
	}

	/* (non-Javadoc)
	 * @see org.objectweb.fractal.api.control.LifeCycleController#getFcState()
	 */
	public String getFcState() {
		return status ? "STARTED" : "STOPPED";
	}

	/* (non-Javadoc)
	 * @see org.objectweb.fractal.api.control.LifeCycleController#startFc()
	 */
	public void startFc() throws IllegalLifeCycleException {
		Component jadeNode = null;
		Component niche = null;
		OverlayAccess overlayAccess = null;

		Component comps[] = null;
		try {
			comps = Fractal.getSuperController(myself).getFcSuperComponents();
		} catch (NoSuchInterfaceException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < comps.length; i++) {
			try {
				if (Fractal.getNameController(comps[i]).getFcName().equals(
						"managed_resources")) {
					jadeNode = comps[i];
					break;
				}
			} catch (NoSuchInterfaceException e) {
				e.printStackTrace();
			}
			;
		}

		try {
			niche = FractalUtil.getFirstFoundSubComponentByName(jadeNode,
					"nicheOS");
		} catch (NoSuchComponentException e1) {
			e1.printStackTrace();
		}

		try {
			overlayAccess = (OverlayAccess) niche
					.getFcInterface("overlayAccess");
		} catch (NoSuchInterfaceException e) {
			e.printStackTrace();
		}

		nicheOSSupport = overlayAccess.getOverlay().getComponentSupport();
		logger = overlayAccess.getOverlay().getNicheAsynchronousSupport();

		status = true;

		myGlobalId = nicheOSSupport.getResourceManager().getComponentId(myself);
		nicheOSSupport.setOwner((IdentifierInterface) myGlobalId);

		System.err.println("FrontEnd Started. GlobalId = " + myGlobalId
				+ " at " + myGlobalId.getResourceRef().getDKSRef().getId());

	}

	/* (non-Javadoc)
	 * @see org.objectweb.fractal.api.control.LifeCycleController#stopFc()
	 */
	public void stopFc() throws IllegalLifeCycleException {
		status = false;

	}

	public void run() {

	}

	/**
	 * 
	 * This class is responsible for creating the user interface
	 * 
	 * @author M. Amir Moulavi (moulavi@kth.se)
	 * @author Uwe Dauernheim (uwe@dauernheim.net)
	 * @version 1.0
	 */
	class UserInterfaceThread implements Runnable {

		FrontendImpl connection;

		UserInterfaceThread(FrontendImpl connection) {
			this.connection = connection;

			myUI = new UserInterface();
			myUI.setVisible(true);
		}

		public void run() {

			myUI.run(connection);

		}
	}

	/* (non-Javadoc)
	 * @see psdcms.interfaces.ResultAnnounce#resultAnnounce(int, int, int)
	 */
	public synchronized void resultAnnounce(int result, int row, int col) {
		// System.out.println("FE: Received result " + result + " for row " +
		// row + " col " + col);
		myUI.updateTable(result, row, col);

	}

}
