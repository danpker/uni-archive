# CSP Coursework
# Daniel Parker 2011
#
# carpark_arrive.py - carpark arrive process

import sys
import random

from pycsp import *

@process
def run(arrive_out, button_in):
	while True:
	    t = button_in() #wait for someone to press the button
	    arrive_out('arrive')

