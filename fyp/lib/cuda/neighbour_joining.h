/* Daniel Parker
 * University of Reading
 * 215 Parallel algorithms for Bioinformatics
 *
 * neighbour_joining_serial.h - neighbour_joining.h
 */

#ifndef NEI
#define NEI

#define SIZE 1280
#define LEN 256

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

void neighbour_joining(char *taxa[], int size, int len);

#endif