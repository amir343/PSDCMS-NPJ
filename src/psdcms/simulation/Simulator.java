package psdcms.simulation;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.api.control.LifeCycleController;
import org.objectweb.fractal.util.Fractal;
import org.objectweb.jasmine.jade.service.nicheOS.OverlayAccess;
import org.objectweb.jasmine.jade.util.FractalUtil;
import org.objectweb.jasmine.jade.util.NoSuchComponentException;

import dks.niche.ids.ComponentId;
import dks.niche.ids.GroupId;
import dks.niche.interfaces.NicheAsynchronousInterface;
import dks.niche.interfaces.NicheComponentSupportInterface;

import psdcms.interfaces.*;

/**
 * The simulator class for running specific tasks given by the Usertask.
 * 
 * @author Amir Moulavi (amir.moulavi@gmail.com)
 * @author Uwe Dauernheim (uwe@dauernheim.net)
 * @version 1.0
 */
public class Simulator implements BindingController, LifeCycleController,
		UTPublishTask {

	/**
	 * The component handle
	 */
	private Component myself;
	/**
	 * The component's status
	 */
	private boolean status;
	/**
	 * The component's Id
	 */
	ComponentId myGlobalId;
	/**
	 * The component's network location address
	 */
	String myLocation;
	/**
	 * The component's group Id
	 */
	GroupId currentFileGroup;
	/**
	 * The class holding the announced results
	 */
	private ResultAnnounce resultAnnounce;
	/**
	 * The simulator helper thread
	 */
	private SimulatorHelperThread simThread;

	/**
	 * The Niche component support interface
	 */
	NicheComponentSupportInterface nicheOSSupport;
	/**
	 * The Niche asynchronous interface
	 */
	NicheAsynchronousInterface logger;

	/**
	 * The constructor initializing the status, the simulation helper thread and
	 * running the simulation helper thread.
	 */
	public Simulator() {
		System.out.println("Simulator is created!");
		status = false;
		simThread = new SimulatorHelperThread(this);
		simThread.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.objectweb.fractal.api.control.BindingController#bindFc(java.lang.
	 * String, java.lang.Object)
	 */
	public void bindFc(String itfName, Object itfValue)
			throws NoSuchInterfaceException, IllegalBindingException,
			IllegalLifeCycleException {
		if (itfName.equals("component"))
			myself = (Component) itfValue;
		else if (itfName.equals("resultAnnounce"))
			resultAnnounce = (ResultAnnounce) itfValue;
		else
			throw new NoSuchInterfaceException(itfName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.fractal.api.control.BindingController#listFc()
	 */
	public String[] listFc() {
		return new String[] { "component", "resultAnnounce" };

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.objectweb.fractal.api.control.BindingController#lookupFc(java.lang
	 * .String)
	 */
	public Object lookupFc(String itfName) throws NoSuchInterfaceException {
		if (itfName.equals("component"))
			return myself;
		else if (itfName.equals("resultAnnounce"))
			return resultAnnounce;
		else
			throw new NoSuchInterfaceException(itfName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.objectweb.fractal.api.control.BindingController#unbindFc(java.lang
	 * .String)
	 */
	public void unbindFc(String itfName) throws NoSuchInterfaceException,
			IllegalBindingException, IllegalLifeCycleException {
		if (itfName.equals("component"))
			myself = null;
		else if (itfName.equals("resultAnnounce"))
			resultAnnounce = null;
		else
			throw new NoSuchInterfaceException(itfName);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.fractal.api.control.LifeCycleController#getFcState()
	 */
	public String getFcState() {
		return status ? "STARTED" : "STOPPED";
	}

	/*
	 * (non-Javadoc)
	 * 
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
		myLocation = "" + myGlobalId.getResourceRef().getDKSRef().getId();

		nicheOSSupport.setOwner(myGlobalId);

		System.err.println("Simulator Started component = "
				+ myGlobalId.getId().getLocation() + " at " + myLocation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.objectweb.fractal.api.control.LifeCycleController#stopFc()
	 */
	public void stopFc() throws IllegalLifeCycleException {
		status = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see psdcms.interfaces.UTPublishTask#utPublishTask(int[], int[], int,
	 * int)
	 */
	public synchronized void utPublishTask(int[] row, int[] col, int i, int j) {
		simThread.add(row, col, i, j);
	}

	/**
	 * Announcing (sending) the results to the interface component.
	 * 
	 * @param result
	 *            The result containing the matrix element's value
	 * @param row
	 *            The matrix element's row index
	 * @param col
	 *            The matrix element's column index
	 */
	public void sendResult(int result, int row, int col) {
		resultAnnounce.resultAnnounce(result, row, col);

	}
}
