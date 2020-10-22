
import org.chocosolver.solver.Model;
import org.kohsuke.args4j.Option;

public class Fortnite extends AbstractProblem {

	@Option(name = "-i", aliases = "--instance", usage = "Instance ID.", required = true)
	Data data = Data.inst1;

	int nb_sessions, nb_days, nb_players_per_session, min_sessions, max_sessions;

	@Override
	public void buildModel() {

		nb_sessions = data.param(0);

		nb_days = data.param(1);

		nb_players_per_session = data.param(2);

		min_sessions = data.param(3);

		max_sessions = data.param(4);

		model = new Model();
		
		//TODO

	}

	@Override
	public void configureSearch() {
		//TODO
	}

	@Override
	public void solve() {
		
		model.getSolver().solve();

		//print solution
		

		model.getSolver().printStatistics();
	}

	public static void main(String[] args) {
		new Fortnite().execute(args);
	}

	/////////////////////////////////// DATA ////////////////////////////
	/////////////////////////////////////////////////////////////////////

	enum Data {
		inst1(new int[] { 2, 3, 2, 1, 1 }),

		inst2(new int[] { 4, 5, 4, 1, 1 }),

		inst3(new int[] { 5, 8, 2, 0, 1 }),

		inst4(new int[] { 5, 6, 3, 0, 1 }),

		inst5(new int[] { 5, 6, 5, 1, 1 }),

		inst6(new int[] { 6, 3, 6, 0, 2 }),

		inst7(new int[] { 7, 7, 7, 0, 7 }),

		inst8(new int[] { 8, 5, 4, 0, 1 }),

		inst9(new int[] { 8, 9, 8, 0, 1 }),

		inst10(new int[] { 10, 7, 10, 0, 7 }),

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
