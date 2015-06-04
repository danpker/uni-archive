# Daniel Parker
# University of Reading
# 215 Parallel algorithms for Bioinformatics
#
# nj.py - neibour-joining implementation

from distance_matrix import *
from operator import add

def distance_matrix(taxa):
    """produce and return a distance matrix"""

    r = len(taxa)
    matrix = DistanceMatrix(taxa)
    i = 0
    for x in taxa:
        j = 0
        for y in taxa:
            matrix.matrix[i,j] = (distance(x,y))
            j+=1
        i+=1

    return matrix
    
def net_divergence():
    """calculate the net divergence for each cell"""
    

def q_matrix(matrix):
    """produce and return a q-matrix"""

    r = len(matrix.matrix[0])
    q_matrix = zeros((r,r))
    for i in range(r):
        for j in range(r):
            ii = sum(matrix[i,range(r)])
            jj = sum(matrix[j,range(r)])
            q_matrix[i,j] = (r-2)*matrix[i,j] - ii - jj
            if i == j:
                q_matrix[i,j] = 0

    return q_matrix

def distance(seq1, seq2):
    """calculate the distance between 2 sequences"""
    return reduce(add, map(is_same, seq1, seq2))

def is_same(a, b):
    """if a = b return 0, else 1"""
    if a == b:
        return 0
    else:
        return 1

if __name__ == "__main__":
    taxa = ["--------------", "BBBBBBB-------", "CCCCCCCCCCC---",
            "DDDDDDDDDDDDDD"]

    print distance_matrix(taxa)
    print q_matrix(distance_matrix(taxa))
