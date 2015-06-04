#include <stdio.h>
#include <omp.h>

#include "lib.h"

#define COLUMNS 50000
#define ROWS	2000
#define STEPS	1000

int main (int argc, char *argv[]) {
	
	int i, j;
	double start, end;
	float **prev = makegrid(COLUMNS, ROWS);
	float **cur = makegrid(COLUMNS, ROWS);

	/* Provides the constant cell */
	prev[0][0] = 5;

	int step = 0;

	/* Run the program 
		Each iteration of the loop is a step
		Each loop for rows and cols is ran
			in parallel using openmp */

	start = omp_get_wtime();
	while(step != STEPS) {
		#pragma omp parallel for	
		for (i = 0; i < ROWS; i++) {
			#pragma omp parallel for
			for (j = 0; j < COLUMNS; j++) {
				cur[i][j] = calc(prev, ROWS, COLUMNS, i, j);
			}
		}

		/* Swap the pointers */
		float **temp = cur;
		cur = prev;
		prev = temp;

		/* Increase the step */
		step++;
		printf("Step %d\n", step);

		/* Reset the constant cell */
		prev[0][0] = 5;
	}
	end = omp_get_wtime();

	printf("Total time: %f seconds\n", end-start);

	printGrid(cur, 10, 10);

	return 0;


}

