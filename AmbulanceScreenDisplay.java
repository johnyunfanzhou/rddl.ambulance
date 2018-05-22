/**
 * RDDL: A simple text display for the SysAdmin domain state. 
 * 
 * @author Scott Sanner (ssanner@gmail.com)
 * @version 10/10/10
 *
 **/

package rddl.viz;

import java.util.ArrayList;

import rddl.EvalException;
import rddl.State;
import rddl.RDDL.LCONST;
import rddl.RDDL.PVARIABLE_DEF;
import rddl.RDDL.PVAR_NAME;
import rddl.RDDL.TYPE_NAME;

public class AmbulanceScreenDisplay extends StateViz {
        public int _numCols = 3; 
        
	public AmbulanceScreenDisplay() {
		_bSuppressNonFluents = true;
	}
	
	public AmbulanceScreenDisplay(boolean suppress_nonfluents) {
		_bSuppressNonFluents = suppress_nonfluents;
	}
	
	public boolean _bSuppressNonFluents = false;
	
	public void display(State s, int time) {
		try {
			System.out.println("TIME = " + time + ": " + getStateDescription(s));
		} catch (EvalException e) {
			System.out.println("\n\nError during visualization:\n" + e);
			e.printStackTrace();
			System.exit(1);
		}
	}

	//////////////////////////////////////////////////////////////////////

	public String getStateDescription(State s) throws EvalException {
		StringBuilder sb = new StringBuilder();
		
		TYPE_NAME ambulance = new TYPE_NAME("ambulance");
		ArrayList<LCONST> ambulances = s._hmObject2Consts.get(ambulance);

		TYPE_NAME node = new TYPE_NAME("node"); 
		ArrayList<LCONST> nodes = s._hmObject2Consts.get(node);

		PVAR_NAME ambulanceAt = new PVAR_NAME("ambulance-at-node");
		//PVARIABLE_DEF location = s._hmPVariables.get(ambulanceAt);
		
		PVAR_NAME personWaiting = new PVAR_NAME("person-waiting-service");
		//PVARIABLE_DEF calls = s._hmPVariables.get(personWaiting);
	
		PVAR_NAME inAmbulance = new PVAR_NAME("person-in-ambulance");
		//PVARIABLE_DEF loaded = s._hmPVariables.get(inAmbulance);

		PVAR_NAME hospital = new PVAR_NAME("HOSPITAL");
		//PVARIABLE_DEF isHospital = s._hmPVariables.get(hospital);

		// Set up an arity-2 parameter list for ambulance, node
		ArrayList<LCONST> paramsAN = new ArrayList<LCONST>(2);
		paramsAN.add(null);
		paramsAN.add(null);

		// Set up an arity-1 parameter list for ambulance
		ArrayList<LCONST> paramsA = new ArrayList<LCONST>(1);
		paramsA.add(null);
		
		// Set up an arity-1 parameter list for node
		ArrayList<LCONST> paramsN = new ArrayList<LCONST>(1);
		paramsN.add(null);

		
		sb.append("\n\n");
		
                int iter = 0;
		// Dipict the status of each node 
		for (LCONST n : nodes) {
                    paramsN.set(0, n); 
                    if((Boolean)s.getPVariableAssign(hospital, paramsN)) {
                        sb.append("H"); 
                    }
                    else if((Boolean)s.getPVariableAssign(personWaiting, paramsN)){
                        sb.append("C"); 
                    }
                    else{
                        sb.append("."); 
                    }
                    
                    for (LCONST a : ambulances) {
                        paramsA.set(0,a); 
                        paramsAN.set(0,a);
                        paramsAN.set(1,n); 
                            
                        if(((Boolean)s.getPVariableAssign(ambulanceAt, paramsAN)) && ((Boolean)s.getPVariableAssign(inAmbulance, paramsA))){
                            sb.append("F");
                        }
                        
                        else if((Boolean)s.getPVariableAssign(ambulanceAt, paramsAN)){
                            sb.append("E");
                        }
                            
                        else{
                            sb.append(" "); 
                        }
                    }
                         
                    iter++; 
                    if(iter%_numCols == 0) {
                        sb.append("\n\n"); 
                    }
                    else {
                        sb.append(" ");
                    }
		}					
		return sb.toString();
	}
}

