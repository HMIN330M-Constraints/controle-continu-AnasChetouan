int: nb_nurses; % number of nurses
set of int: NURSE = 1..nb_nurses;
int: nb_days; % number of days
set of int: DAY = 1..nb_days;
set of int: SHIFT = 1..3;
int: day = 1; int: night = 2; int: dayoff = 3;
array[SHIFT] of string: code = ["d", "n", "-"];
int: day_shift; % number on day shift
int: lb_nightshift; % lower bound for nightshift
int: ub_nightshift; % upper bound for nightshift


array[NURSE,DAY] of var SHIFT: x;

% Constraint1: In each four day period a nurse must have at least one day off

constraint forall(n in NURSE, d in 1..nb_days-4)
                 ( x[n,d] != dayoff /\ x[n,d+1] != dayoff 
                 /\ x[n,d+2] != dayoff /\ x[n,d+3] != dayoff
                   -> x[n,d+4] = dayoff);

% Constraint2: no nurse can be scheduled for 3 night shifts in a row

constraint forall(n in NURSE, d in 1..nb_days-2)
                 ( x[n,d] = night /\ x[n,d+1] = night 
                   -> x[n,d+2] = dayoff);

% Constraint3: no nurse can be scheduled for a day shift after a night shift

constraint forall(n in NURSE, d in 1..nb_days-1)
                 (x[n,d] = night -> x[n,d+1] != day);

% Constraint4: day shift size and min/max night shift size

include "global_cardinality_low_up.mzn";
constraint forall(d in DAY)
                 (global_cardinality_low_up([x[n,d] | n in NURSE ],                                               
                 [ day, night ], [ day_shift, lb_nightshift ], [day_shift, ub_nightshift])); 

solve satisfy;

output [code[fix(x[n,d])] 
         ++ if d = nb_days then "\n" else " " endif
       | n in NURSE, d in DAY];