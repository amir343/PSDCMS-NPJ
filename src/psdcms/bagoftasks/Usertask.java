package psdcms.bagoftasks;

import java.util.Random;

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

import psdcms.interfaces.FEPublishTask;
import psdcms.interfaces.UTPublishTask;

import dks.niche.ids.ComponentId;
import dks.niche.ids.GroupId;
import dks.niche.interfaces.NicheAsynchronousInterface;
import dks.niche.interfaces.NicheComponentSupportInterface;

/**
 * This class is responsible for UserTask component. It implements the
 * FEPublishTask interface as a server interface and also has UTPublishTask
 * interface member as its client interface.
 * 
 * @author M. Amir Moulavi (moulavi@kth.se)
 * @author Uwe Dauernheim (uwe@dauernheim.net)
 * @version 1.0
 * @see psdcms.interfaces.FEPublishTask
 * @see psdcms.interfaces.UTPublishTask
 * 
 * 
 */


public class Usertask implements BindingController, LifeCycleController,
		FEPublishTask {

	/**
	 * Client interface for UserTask component
	 */
	private UTPublishTask utPublishTask;
	/**
	 * Component reference refers to itself
	 */
	private Component myself;
	/**
	 * Status of this component which is either STARTED or STOPPED
	 */
	private boolean status;
	/**
	 * Component Global ID in the system
	 */
	ComponentId myGlobalId;
	/**
	 * Component's location
	 */
	String myLocation;

	/**
	 * Niche Component Support Interface used for getting GlobalID
	 */
	NicheComponentSupportInterface nicheOSSupport;
	/**
	 * Niche Asynchronous Interface used for logging.
	 */
	NicheAsynchronousInterface logger;

	/**
	 * The constructor only puts the current component in the state of STOPPED.
	 */
	public Usertask() {
		System.out.println("UserTask component created!");
		status = false;
	}

	/* (non-Javadoc)
	 * @see org.objectweb.fractal.api.control.BindingController#bindFc(java.lang.String, java.lang.Object)
	 */
	public void bindFc(String itfName, Object itfValue)
			throws NoSuchInterfaceException, IllegalBindingException,
			IllegalLifeCycleException {
		if (itfName.equals("component"))
			myself = (Component) itfValue;
		else if (itfName.equals("utPublishTask"))
			utPublishTask = (UTPublishTask) itfValue;
		else
			throw new NoSuchInterfaceException(itfName);

	}

	/* (non-Javadoc)
	 * @see org.objectweb.fractal.api.control.BindingController#listFc()
	 */
	public String[] listFc() {
		return new String[] { "component", "utPublishTask" };
	}

	/* (non-Javadoc)
	 * @see org.objectweb.fractal.api.control.BindingController#lookupFc(java.lang.String)
	 */
	public Object lookupFc(String itfName) throws NoSuchInterfaceException {
		if (itfName.equals("component"))
			return myself;
		else if (itfName.equals("utPublishTask"))
			return utPublishTask;
		else
			throw new NoSuchInterfaceException(itfName);
	}

	/* (non-Javadoc)
	 * @see org.objectweb.fractal.api.control.BindingController#unbindFc(java.lang.String)
	 */
	public void unbindFc(String itfName) throws NoSuchInterfaceException {
		if (itfName.equals("component"))
			myself = null;
		else if (itfName.equals("utPublishTask"))
			utPublishTask = null;
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
		myLocation = "" + myGlobalId.getResourceRef().getDKSRef().getId();

		nicheOSSupport.setOwner(myGlobalId);

		System.err.println("UserTask Started component = " + myGlobalId.getId()
				+ " at " + myLocation);

	}

	/* (non-Javadoc)
	 * @see org.objectweb.fractal.api.control.LifeCycleController#stopFc()
	 */
	public void stopFc() throws IllegalLifeCycleException {
		status = false;
	}

	/* (non-Javadoc)
	 * @see psdcms.interfaces.FEPublishTask#fePublishTask(int[], int[], int, int)
	 */
	public void fePublishTask(int[] row, int[] col, int i, int j) {
		// System.out.println("UserTask Received row " + i + " col " + j);
		utPublishTask.utPublishTask(row, col, i, j);
	}

}
