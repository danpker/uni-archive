from numpy  import *

class DistanceMatrix():
    """A distance matrix with labels"""
    def __init__(self, taxa):
        self.orisize = len(taxa)
        self.size = self.orisize
        self.matrix = zeros((self.size, self.size))

        