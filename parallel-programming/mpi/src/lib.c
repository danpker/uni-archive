#include <stdlib.h>
#include <stdio.h>

#include "lib.h"

/* Calculate the average for the number of cells */
float average (float array[], int size) {
	
	int i;
	/* This was originally a loop, but it was optimised */
	double tot = array[0] + array[1] + array[2] + array[3] +
	array[4];
	
	if (tot == 0) {
		return 0;
	} else {
		return tot / size;
	};
}

/* Makes the grid of the specified size*/
float **makegrid (int col, int row) {

	float **grid = getgrid(col, row);
	initGrid(grid, col, row);

	return grid;
}
		
/* Returns a pointer to a grid that is
	created using malloc */
float **getgrid (int col, int row) {

	float	**grid;
	int		i, j;

	/* Create an array of pointers to pointers */
	grid = malloc(row * sizeof(float *));

	for (i = 0; i != row; i++) {
		
		/* for each pointer 'row' create an array 'columns' */
		grid[i]  = malloc(col * sizeof(float));

	}
	
	return grid;
}

/* Sets all of the cells in the grid to 0 */
void initGrid(float **grid, int col, int row) {

	int		i, j;

	for (i = 0; i != row; i++) {
		for (j = 0; j != col; j++) {

			grid[i][j] = 0;
		}
	}
}

/* Build the array and return the new val */
float calc(float **grid, 
	       	int row, 
			int col, 
			int curRow, 
			int curCol
		   ) {

	int size = 5;

	/* Middle Cell */

	float mid = grid[curRow][curCol];

	/* Left Cell */
	
	float lef;
	if (curCol == 0) {
		lef = 0;
		size--;
	} else {
		lef = grid[curRow][curCol-1];
	}

	/* Check the right, left, top and bottom cells
	   with checks to their edge cases */
	/* Right Cell */

	float rig;
	if (curCol == (col - 1)) {
		rig = 0;
		size--;
	} else {
		rig = grid[curRow][curCol+1];
	}

	/* Top Cell */

	float top;
	if (curRow == 0) {
		top = 0;
		size--;
	} else {
		top = grid[curRow-1][curCol];
	}

	/* Bottom Cell */

	float bot;
	if (curRow == row -1) {
		bot = 0;
		size--;
	} else {
		bot = grid[curRow+1][curCol];
	}

	/* create array for average function */
	float array[] = {mid, lef, rig, top, bot}; 
	
	return average(array, size);
}

/* Print the specified grid */
void printGrid(float **grid, int row, int col) {
	
	int i, j;

	for (i = 0; i != row; i++) {
		for (j = 0; j != col; j++) {
			printf("%3.2f ", grid[i][j]);
		}
		printf("\n");
	}

	printf("\n");

	return;
}


