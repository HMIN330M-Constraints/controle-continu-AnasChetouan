
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;
import org.kohsuke.args4j.Option;

public class NurseRostering extends AbstractProblem {

	@Option(name = "-i", aliases = "--instance", usage = "Instance ID.", required = false)
	private Data data = Data.inst1;

	private int nb_nurses, nb_days, day_shift, lb_nightshift, ub_nightshift;
	private IntVar[][] x;



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
		
		model = new Model();

	//Variables
		
		// Constraint1: In each four day period a nurse must have at least one day off

		//TODO

		// Constraint2: no nurse can be scheduled for 3 night shifts in a row

		//TODO

		// Constraint3: no nurse can be scheduled for a day shift after a night shift

		//TODO

		// Constraint4: day shift size and min/max night shift size

		//TODO

	}

	@Override
	public void configureSearch() {
		// TODO
	}

	@Override
	public void solve() {

		model.getSolver().solve();

		//printSolution

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
