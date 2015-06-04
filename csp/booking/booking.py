# CSP Coursework
# Daniel Parker 2011
#
# booking.py - run the booking system

import socket

from pycsp import *

HOST = ''
PORT = 13375

def booking():
    """runs the booking system"""
    Parallel(connection())

@process
def connection():
    """await a connection and spawn a new booking session"""
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind((HOST, PORT))
    s.listen(1)
    while True:
        conn, addr = s.accept()
        Spawn(booking_session(conn, addr))
        
@process
def booking_session(connection, address):
    """run the booking session with the user"""
    print "Connection!"
    while True:
        pass
        
if __name__ == '__main__':
    booking()
