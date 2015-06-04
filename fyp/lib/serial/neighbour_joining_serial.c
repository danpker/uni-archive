/* Daniel Parker
 * University of Reading
 * 215 Parallel algorithms for Bioinformatics
 *
 * neighbour-joining.c - neibour-joining implementation
 */

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "neighbour_joining_serial.h"
#include "tree.h"

/* Calculate the distance matrix for a list of taxa
 * _size_ long and each one containing no more than 
 * _len_ characters */
void createDistanceMatrix(char *taxa[], int size, int len, NJDMAT *matrix) {

	int i, j, k;

	/* allocate memory and set values for the distance matrix structure */

	matrix->size = size;
	matrix->orisize = size;
	matrix->len = len;

	matrix->labels = (char **) calloc(size, sizeof(char *));
	if (matrix->labels == NULL) {
		fprintf(stderr, "Error: allocating memory for labels\n");
	}

	matrix->distances = (float *) calloc(size * size, sizeof(float));

	/* copy labels into place */
	for (i = 0; i < size; i++) {
		matrix->labels[i] = (char *) calloc(len, sizeof(char));
		if (matrix->labels[i] == NULL) {
			fprintf(stderr, "Error: allocating memory for a label\n");
		} else {
			strcpy(matrix->labels[i], taxa[i]);
		}
	}

	/* create the distance matrix */
	for (i = 0; i < size; i++) {
		for (j = 0; j < size; j++) {
			matrix->distances[i * size + j] = 0;
			for (k = 0; k < len; k++) {
				if (taxa[i][k] != taxa[j][k]) {
					matrix->distances[i * size + j] += 1;
				}
			}
		}
	}
}

void netDiv(float netdiv[], int size, NJDMAT *dmat) {
	int i, j;

	// calcuate net divergence
	for (i = 0; i < size; i++) {
		netdiv[i] = 0;
		for (j = 0; j < size; j++) {
			netdiv[i] += dmat->distances[i * size + j];
		}
	}
}

/* Calculate matrix Q for the distance matrix
 * _size_ * _size_ large */
void qMatrix(NJDMAT *dmat, int size, float netDiv[], float qmatrix[]) {

	int i, j, k;

	for (i = 0; i < size; i++) {
		for (j = 0; j < size; j++) {
			int loc = i * size + j;
			qmatrix[loc] = dmat->distances[loc]
					- ((netDiv[i] + netDiv[j]) / (size - 2));
		}
	}
}

/* return the _i_ and _j_ values for the smallest entry in the qMatrix*/
void smallest(int *x, int *y, float qmatrix[], int size) {
	float min = 90000; //TODO: Change to int max
	int lx, ly;
	int i, j;

	for (i = 0; i < size; i++) {
		for (j = 0; j < size; j++) {
			if (qmatrix[i * size + j] < min && i != j) {
				min = qmatrix[i * size + j];
				lx = i;
				ly = j;
			}
		}
	}
	*x = lx;
	*y = ly;
}

/* return the two branch lengths from U to F and G */
void pairNewDist(NJDMAT *dmat, int size, float netdiv[], int f, int g,
		float *fl, float *gl) {
	int lfdist, lgdist;

	int dist = dmat->distances[f * size + g];
	int devdist = netdiv[f] - netdiv[g];
	float left = dist / 2.0;
	float right = devdist / (2 * (size - 2));
	*fl = left + right;
	*gl = dist - *fl;
}

/* calculate the new dmatrix, with merging */
void newDistanceMatrix(NJDMAT *dmat, int size, int floc, int gloc) {

	int i, j, k, l;

	/* new size of distances */
	int nsize = size - 1;

	/* map for new locs */
	int old[nsize];

	/* f and g dist */
	float fgdist = dmat->distances[floc * size + gloc] / 2;

	/* new distance matrix */
	float *newdist = (float *) calloc(nsize * nsize, sizeof(float));
	/* create mapping for new values */
	for (i = 0, j = 0; i < size; i++) {
		if (i != floc || i != gloc) {
			old[j++] = i;
		}
	}

	for (i = 0; i < nsize; i++) {
		for (j = 0; j < nsize; j++) {
			if (i == 0 || j == 0) {
				//d(CU) = d(AC) + d(BC) - d(AB) / 2 = 3
				/* new, merged node */
				if (i == 0) {
					k = old[j];
					newdist[i * nsize + j] = dmat->distances[floc * size + k]
							+ dmat->distances[gloc * size + k] - fgdist;
				}
				if (j == 0) {
					k = old[i];
					newdist[i * nsize + j] = dmat->distances[floc * size + k]
							+ dmat->distances[gloc * size + k] - fgdist;
				}
				if (j == 0 && i == 0) {
					newdist[0] = 0;
				}
			} else {
				k = old[i];
				l = old[j];
				newdist[i * nsize + j] = dmat->distances[k * size + l];
			}
		}
	}

	/* adjust size */
	dmat->size--;
	/* adjust labels */
	char **newlabels = (char **) calloc(nsize, sizeof(char *));
	if (newlabels == NULL) {
		fprintf(stderr, "Error allocating new label memory\n");
	}
	/* first element */
	newlabels[0] = (char *) malloc(sizeof("NEW NODE"));
	strcpy(newlabels[0], "NEW NODE");
	for (i = 1; i < nsize; i++) {
		newlabels[i] = (char *) malloc(dmat->len * sizeof(char));
		strcpy(newlabels[i], dmat->labels[old[i]]);
	}

	float *olddist = dmat->distances;
	char **oldlabels = dmat->labels;
	dmat->distances = newdist;
	dmat->labels = newlabels;

	//TODO free old label memory
	//TODO free old dist matrix memory
	free(olddist);
	for (i = 0; i < size; i++) {
		free(oldlabels[i]);
	}
	free(oldlabels);
}

/* Neighbour-join, 1 iteration
 * TODO actual comment */
void iteration(NJDMAT *dmat, int size, float *flp, float *glp, int *floc,
		int *gloc) {

	float netdiv[size];
	float qmatrix[size * size];
	int f, g;
	float fl, gl;
	int i, j;

	// net divergence
	netDiv(netdiv, size, dmat);

	// calculate new (Q) d matrix
	qMatrix(dmat, size, netdiv, qmatrix);

	// get smallest dist
	smallest(&f, &g, qmatrix, size);

	// get lens
	pairNewDist(dmat, size, netdiv, f, g, &fl, &gl);

	*flp = fl;
	*glp = gl;
	*floc = f;
	*gloc = g;
}

/* Run the full codes */
void neighbour_joining(char *taxa[], int size, int len) {

	int i, j, gen;
	float flp, glp;
	int floc, gloc;
	NJTREE **tree;
	NJDMAT *dmat;

	/* create the distance matrix */
	dmat = (NJDMAT *) malloc(sizeof(NJDMAT));
	createDistanceMatrix(taxa, size, len, dmat);

	// /* build the original tree */
	// tree = (NJTREE **) calloc(size, sizeof(NJTREE *));
	// for (i = 0; i < size; i++) {
	// 	NJTREE *cur;
	// 	cur = (NJTREE *) malloc(sizeof(NJTREE));
	// 	cur->name = (char *) calloc(len, sizeof(char));
	// 	strcpy(cur->name, taxa[i]);
	// 	tree[i] = cur;
	// }

	// gen = size;
	// while(gen > 2) {

	// 	/* run an iteration */
	// 	iteration(dmat, gen, &flp, &glp, &floc, &gloc);

	// 	/* add new branch, then remove leafs from list */
	// 	NJTREE *tmp = (NJTREE *) calloc(1, sizeof(NJTREE));
	// 	tmp->llen = flp;
	// 	tmp->rlen = glp;
	// 	tmp->left = tree[floc];
	// 	tmp->right = tree[gloc];

	// 	/* remove the two duplicates, and put the new node at the front */
	// 	NJTREE **newtree = (NJTREE **) calloc(gen-1, sizeof(NJTREE *));

	// 	newtree[0] = tmp;
	// 	for (i = 0, j = 1; i < gen; i++) {
	// 		if(i != floc && i != gloc) {
	// 			newtree[j++] = tree[i];
	// 		}
	// 	}
	// 	tree = (NJTREE **) calloc(gen-1, sizeof(NJTREE *));
	// 	for (i = 0; i < gen-1; i++) {

	// 		tree[i] = newtree[i];
	// 	}

	// 	newDistanceMatrix(dmat, gen, floc, gloc);
	// 	gen--;
	// }

	// /* add new branch, then remove leafs from list */
	// NJTREE *fin = (NJTREE *) malloc(sizeof(NJTREE));
	// fin->llen = 0; //TODO FIX
	// fin->rlen = 0;
	// fin->left = tree[0];
	// fin->right = tree[1];

	// printTree(fin, size, 0);
}

