/* Daniel Parker
 * University of Reading
 * 215 Parallel algorithms for Bioinformatics
 *
 * tree.h - collection of functions for the phylo tree
 */

 /*
 * NJTREE
 *
 * name		: name of node, if a leaf
 * rlen		: length of branch to right child
 * llen		: length of branch to left child
 * left		: pointer to left child
 * right	: pointer to right child
 */
typedef struct _NJTREE_ {
	char *name;
	float rlen;
	float llen;
	struct _NJTREE_ *left;
	struct _NJTREE_ *right;
} NJTREE;

char * printTree(NJTREE *tree, int size, int lev);