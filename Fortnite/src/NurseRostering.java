
import org.chocosolver.sat.SatFactory;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;
import org.kohsuke.args4j.Option;

public class NurseRostering extends AbstractProblem {

	@Option(name = "-i", aliases = "--instance", usage = "Instance ID.", required = false)
	private Data data = Data.inst1;

	private int nb_nurses, nb_days, day_shift, lb_nightshift, ub_nightshift;
	private IntVar[][] x;



	@SuppressWarnings("null")
	@Override
	public void buildModel() {

		nb_nurses = data.param(0);

		nb_days = data.param(1);

		day_shift = data.param(2);

		lb_nightshift = data.param(3);

		ub_nightshift = data.param(4);

		int day = 1;
		int night = 2;
		int dayoff = 3;
		x = new IntVar[nb_nurses][nb_days];
		model = new Model("NurseRostering");
		
		int[] domaine = {day,night,dayoff};
		
		
		
		for(int n = 0; n < nb_nurses; n++){
			for(int d = 0; d < nb_days; d++){
				x[n][d] = model.intVar("N"+n+"D"+d,domaine);
			}
    	}
		
		
	//Variables

		// Constraint1: In each four day period a n must have at least one day off
		for(int n = 0; n < nb_nurses; n++) {
			for(int d = 0; d < nb_days-3; d++) {
				model.ifThen(model.and(
						model.arithm(x[n][d], "<", 3), 
						model.arithm(x[n][d+1], "<", 3), 
						model.arithm(x[n][d+2], "<", 3)) , 
						model.arithm(x[n][d+3], "=", 3)  );
			}
		}
		
		// Constraint2: no nurse can be scheduled for 3 night shifts in a row
		for(int n = 0; n < nb_nurses; n++) {
			for(int d = 0; d < nb_days-2; d++) {
				model.ifThen(model.and(
						model.arithm(x[n][d], "=", 2),
						model.arithm(x[n][d+1], "=", 2)), 
						model.arithm(x[n][d+2],"!=",2));
			}
		}

		// Constraint3: no nurse can be scheduled for a day shift after a night shift	
		for(int n = 0; n < nb_nurses; n++) {
			for(int d = 0; d < nb_days-1; d++) {
				model.ifThen(
						model.arithm(x[n][d], "=", 2), 
						model.arithm(x[n][d+1],"!=",1));
			}
		}

		
		
		// Constraint4: day shift size and min/max night shift size
		IntVar[] nsT = new IntVar[nb_nurses];
		for(int d = 0; d < nb_days; d++) {
			for(int n = 0; n < nb_nurses; n++) {
				nsT[n] = x[n][d];
			}
			if(day_shift > 0 && lb_nightshift > 0 && ub_nightshift > 0) {
				model.count(1, nsT, model.intVar(day_shift)).post();
				model.not(model.count(1, nsT, model.intVar(day_shift+1))).post();
				
				model.count(2, nsT, model.intVar(lb_nightshift)).post();
				model.not(model.count(2, nsT, model.intVar(ub_nightshift))).post();
			}
		}
		

	}

	@Override
	public void configureSearch() {
		// TODO
	}

	@Override
	public void solve() {

		model.getSolver().solve();

		// Affichage
		for(int n = 0; n < nb_nurses; n++){
			for(int d = 0; d < nb_days; d++){
				switch(x[n][d].getValue()) {
					case 1:
						System.out.print("d" +" | ");
						break;
					case 2:
						System.out.print("n" +" | ");
						break;
					case 3:
						System.out.print("o" +" | ");
						break;
				}
			}
			
			System.out.println(" ");
    	}
		

		model.getSolver().printStatistics();
	}

	public static void main(String[] args) {
		new NurseRostering().execute(args);
	}

	/////////////////////////////////// DATA ////////////////////////////
	/////////////////////////////////////////////////////////////////////

	enum Data {
		inst1(new int[] { 6, 7, 2, 2, 3 }),

		;
		final int[] param;

		Data(int[] param) {
			this.param = param;
		}

		int param(int i) {
			return param[i];
		}
	}
}
