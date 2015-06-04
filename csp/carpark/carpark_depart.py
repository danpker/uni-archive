# CSP Coursework
# Daniel Parker 2011
#
# carpark_depart.py - carpark depart process

import sys
import random

from pycsp import *

@process
def run(depart_out, button_in):
    while True:
        t = button_in() #wait for someone to press the button
        depart_out('depart')
