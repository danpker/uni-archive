# CSP Coursework
# Daniel Parker 2011
#
# app.py - main application wrapper

from pycsp import *

from carpark import carpark
from e_ticket import e_ticket
from booking import booking

Parallel(
	carpark.run(),
	e_ticket.run(),
	booking.run()
)
