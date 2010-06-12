package psdcms.app;

import java.util.ArrayList;

import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.NoSuchInterfaceException;
import org.objectweb.fractal.api.control.BindingController;
import org.objectweb.fractal.api.control.IllegalBindingException;
import org.objectweb.fractal.api.control.IllegalLifeCycleException;
import org.objectweb.fractal.api.control.LifeCycleController;
import org.objectweb.jasmine.jade.service.componentdeployment.NicheIdRegistry;
import org.objectweb.jasmine.jade.service.nicheOS.OverlayAccess;

import dks.niche.fractal.FractalInterfaceNames;
import dks.niche.ids.ComponentId;
import dks.niche.ids.GroupId;
import dks.niche.ids.NicheId;
import dks.niche.interfaces.JadeBindInterface;
import dks.niche.interfaces.NicheActuatorInterface;

/**
 * This is one of the main classes in the project. First, it gets the overlay
 * access network and all UserTask, Simulator and FrontEnd Components. It then
 * generates two group namely BagOfTasks and SimulatorGroup.
 * 
 * StartApp does the following bindings between the mentioned components:
 * FrontEnd --(One-to-Many) --> BagOfTasksGroup -- (One-to-Many) -->
 * SimulatorGroup -- (One-to-One) --> FrontEnd
 * 
 * @author M. Amir Moulavi (moulavi@kth.se)
 * @author Uwe Dauernheim (uwe@dauernheim.net)
 * @version 1.0
 * 
 * 
 * 
 */

public class StartApp implements BindingController, LifeCycleController {

	/**
	 * Prefix of Application's name
	 */
	public final static String APPLICATION_PREFIX = "PSDCMS-NPJ_0/";
	/**
	 * Name of UserTask component
	 */
	public final static String USER_COMPONENT = "UserTask";
	/**
	 * Name of Simulator component
	 */
	public final static String SIMULATOR_COMPONENT = "Simulator";
	/**
	 * Name of FrontEnd component
	 */
	public final static String FRONTEND_COMPONENT = "frontEnd";
	/**
	 * DCMS Overlay Access object
	 */
	private OverlayAccess dcmService;
	/**
	 * Niche ID Registry for looking up UserTask, FrontEnd and Simulator components
	 */
	private NicheIdRegistry idRegistry;
	/**
	 * Component reference which refers to the current component
	 */
	private Component mySelf;
	/**
	 * Niche Actuator Interface used for creating groups and doing the bindings.
	 */
	private NicheActuatorInterface myActuatorInterface;

	/**
	 * Status of this component which is either STARTED or STOPPED
	 */
	private boolean status;

	/**
	 * In this method first it gets the overlay access network and all UserTask, Simulator and
	 * FrontEnd Components. It then generates two group namely BagOfTasks and
	 * SimulatorGroup.
	 * 
	 * StartApp does the following bindings between the mentioned components:
	 * FrontEnd --(One-to-Many) --> BagOfTasksGroup -- (One-to-Many) -->
	 * SimulatorGroup -- (One-to-One) --> FrontEnd
	 * 
	 */
	public void startUpScript() {

		// Getting the overlay access network

		this.myActuatorInterface = dcmService.getOverlay().getNicheActuator(
				new NicheId());

		// Definition for constructing two groups: BagOfTaskGroup and
		// SimulatorGroup

		ArrayList<ComponentId> bagOfTasksComponents = new ArrayList<ComponentId>();
		ArrayList<ComponentId> simulatorComponents = new ArrayList<ComponentId>();
		ComponentId frontEnd;

		frontEnd = (ComponentId) idRegistry.lookup(APPLICATION_PREFIX
				+ FRONTEND_COMPONENT);

		if (frontEnd == null) {
			System.out.println("Couldn't find frontEnd component");

		}

		ComponentId user = (ComponentId) idRegistry.lookup(APPLICATION_PREFIX
				+ USER_COMPONENT + "1");
		ComponentId sim = (ComponentId) idRegistry.lookup(APPLICATION_PREFIX
				+ SIMULATOR_COMPONENT + "1");

		int user_index = 1;
		int sim_index = 1;

		// Collecting all the UserTask components. currently there are 3
		// components (refer to .fractal file)

		while (user != null) {
			bagOfTasksComponents.add(user);
			user_index++;
			user = (ComponentId) idRegistry.lookup(APPLICATION_PREFIX
					+ USER_COMPONENT + user_index);
		}

		System.out.println("Group BagOfTasks is created with "
				+ bagOfTasksComponents.size() + " members");

		// Collecting all the Simulator components. Currently there are 4
		// components (refer to .fractal file)

		while (sim != null) {
			simulatorComponents.add(sim);
			sim_index++;
			sim = (ComponentId) idRegistry.lookup(APPLICATION_PREFIX
					+ SIMULATOR_COMPONENT + sim_index);
		}

		System.out.println("Group Simulator is created with "
				+ simulatorComponents.size() + " members");

		// Creating 2 groups, one for UserTasks and the other for Simulators

		GroupId BagOfTasksGID = myActuatorInterface
				.createGroup(bagOfTasksComponents);
		GroupId SimulatorsGID = myActuatorInterface
				.createGroup(simulatorComponents);

		BagOfTasksGID.activate();
		SimulatorsGID.activate();

		// Binding FrontEnd's client interface to BagOfTask's server interface

		String FE_clientItf = "fePublishTask";
		String BOT_serverItf = "fePublishTask";

		myActuatorInterface.bind(frontEnd, FE_clientItf, BagOfTasksGID,
				BOT_serverItf, JadeBindInterface.ONE_TO_ANY);

		// Binding BagOfTask group's client interface to Simulator group's
		// server interface

		String BOT_clientItf = "utPublishTask";
		String SIM_serverItf = "utPublishTask";

		myActuatorInterface.bind(BagOfTasksGID, BOT_clientItf, SimulatorsGID,
				SIM_serverItf, JadeBindInterface.ONE_TO_ANY);

		// Binding Simulator group's client interface to FrontEnd server
		// interface

		String SIM_clientItf = "resultAnnounce";
		String FE_serverItf = "resultAnnounce";

		myActuatorInterface.bind(SimulatorsGID, SIM_clientItf, frontEnd,
				FE_serverItf, JadeBindInterface.ONE_TO_ONE);

	}

	/* (non-Javadoc)
	 * @see org.objectweb.fractal.api.control.BindingController#bindFc(java.lang.String, java.lang.Object)
	 */
	public void bindFc(String itfName, Object itfValue)
			throws NoSuchInterfaceException, IllegalBindingException,
			IllegalLifeCycleException {
		if (itfName.equals(FractalInterfaceNames.OVERLAY_ACCESS))
			dcmService = (OverlayAccess) itfValue;
		else if (itfName.equals(FractalInterfaceNames.ID_REGISTRY))
			idRegistry = (NicheIdRegistry) itfValue;
		else if (itfName.equals("component"))
			mySelf = (Component) itfValue;
		else
			throw new NoSuchInterfaceException(itfName);
	}

	/* (non-Javadoc)
	 * @see org.objectweb.fractal.api.control.BindingController#listFc()
	 */
	public String[] listFc() {
		return new String[] { FractalInterfaceNames.COMPONENT,
				FractalInterfaceNames.ACTUATOR_CLIENT_INTERFACE,
				FractalInterfaceNames.ID_REGISTRY };
	}

	/* (non-Javadoc)
	 * @see org.objectweb.fractal.api.control.BindingController#lookupFc(java.lang.String)
	 */
	public Object lookupFc(String itfName) throws NoSuchInterfaceException {
		if (itfName.equals(FractalInterfaceNames.OVERLAY_ACCESS))
			return dcmService;
		else if (itfName.equals(FractalInterfaceNames.ID_REGISTRY))
			return idRegistry;
		else if (itfName.equals("component"))
			return mySelf;
		else
			throw new NoSuchInterfaceException(itfName);

	}

	/* (non-Javadoc)
	 * @see org.objectweb.fractal.api.control.BindingController#unbindFc(java.lang.String)
	 */
	public void unbindFc(String itfName) throws NoSuchInterfaceException,
			IllegalBindingException, IllegalLifeCycleException {
		if (itfName.equals(FractalInterfaceNames.OVERLAY_ACCESS))
			dcmService = null;
		else if (itfName.equals(FractalInterfaceNames.ID_REGISTRY))
			idRegistry = null;
		else if (itfName.equals("component"))
			mySelf = null;
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
		status = true;
		startUpScript();
	}

	/* (non-Javadoc)
	 * @see org.objectweb.fractal.api.control.LifeCycleController#stopFc()
	 */
	public void stopFc() throws IllegalLifeCycleException {
		status = false;
	}

}
