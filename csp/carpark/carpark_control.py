# CSP Coursework
# Daniel Parker 2011
#
# carpark_control.py - carpark controller process

from pycsp import *

@process
def run(arrive_in, depart_in):
    
    carpark = CarPark(50)
    while True:
        g, msg = AltSelect(
            InputGuard(depart_in),
            InputGuard(arrive_in)
        )
        if msg == 'depart':
            carpark.remove_car()
        elif msg == 'arrive':
            carpark.add_car()


class CarPark:
    """Car park holding cars"""
    def __init__(self, spaces):
        self.max_spaces = spaces
        self.spaces = spaces
        self.cars = []

    def add_car(self):
        """add a car to the carpark"""
        if len(self.cars) < self.max_spaces:
            self.cars.append(1)
            self.update()
            print "Car arrived. There are {0} spaces left.".format(
                self.spaces)
        else:
            print "Cant arrive, no spaces"
            
    def remove_car(self):
        """remove a car from carpark"""
        if len(self.cars) > 0:
            self.cars.pop(0)
            self.update()
            print "Car departed. There are {0} spaces left.".format(
                self.spaces)
        else:
            print "Can't depart, no cars"
                
    def update(self):
       """update the number of spaces left"""
       self.spaces = self.max_spaces - len(self.cars)
    
            

