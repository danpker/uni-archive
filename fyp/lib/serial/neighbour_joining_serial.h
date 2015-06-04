/* Daniel Parker
 * University of Reading
 * 215 Parallel algorithms for Bioinformatics
 *
 * neighbour_joining_serial.h - test bed
 */

#ifndef NEI_SERIAL
#define NEI_SERIAL

/* 
 * NJDMAT
 *
 * size		: number of taxa in the matrix
 * orisize	: original size of the matrix
 * len		: largest length of the taxa string
 * labels	: current taxa in the string, ordered
 * distances: actual distance matrix
 */
typedef struct {
	int size;
	int orisize;
	int len;
	char **labels;
	float *distances;
} NJDMAT;

void createDistanceMatrix(char *taxa[], int size, int len, NJDMAT *mat);
void qMatrix(NJDMAT *dmat, int size, float netDiv[], float qmatrix[]);
void smallest(int *x, int *y, float qmatrix[], int size);
void pairNewDist(NJDMAT *dmat, int size, float netdiv[], int f, int g,
		float *fl, float *gl);
void iteration(NJDMAT *dmat, int size, float *flp, float *glp, int *floc,
		int *gloc);
void neighbour_joining(char *taxa[], int size, int len);

#endif

