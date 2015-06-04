# CSP Coursework
# Daniel Parker 2011
#
# carpark.py - carpark wrapper

import carpark_arrive
import carpark_control
import carpark_depart
import ui

import thread

from pycsp import *

@process
def run():
    """Runs the carpark system in parallel"""
    depart = Channel()
    arrive = Channel()
    arrive_but = Channel()
    depart_but = Channel()
    Parallel(
        ui.run(arrive_but.writer(), depart_but.reader()),
        carpark_arrive.run(arrive.writer(), arrive_but.reader()),
        carpark_control.run(arrive.reader(), depart.reader()),
        carpark_depart.run(depart.writer(), depart_but.reader())
    )

if __name__ == '__main__':
    Spawn(run())