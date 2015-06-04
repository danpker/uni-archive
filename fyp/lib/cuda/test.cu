/* Daniel Parker
 * University of Reading
 * 215 Parallel algorithms for Bioinformatics
 *
 * test.cu - test bed
 */

#include <stdio.h>
#include <stdlib.h>

#include "neighbour_joining.h"
#include "random.h"

int main() {

	char *taxa[SIZE];
	int i;

	for (i = 0; i < SIZE; i++) {
		taxa[i] = (char *) calloc(LEN, sizeof(char));
		generate(taxa[i], LEN);
	}

	//char *taxa[] = {"AACC", "AACC", "AGGC", "ACGT", "ACCT", "GTGT", "GTCT", "CCCC"};

	neighbour_joining(taxa, SIZE, LEN);
	
	return 0;
}

