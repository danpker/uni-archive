/* Daniel Parker
 * University of Reading
 * 215 Parallel algorithms for Bioinformatics
 *
 * random.cu - generate some random strings for testing
 */

#include <stdlib.h>
#include <time.h>

void generate(char string[], int len) {

	int i;

	for (i = 0; i < len; i++) {
		string[i] = 'a' + (rand() % 4 + 1);
	}
}

