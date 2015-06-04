# CSP Coursework
# Daniel Parker 2011
#
# user.py - modelled user using system

import socket

HOST = '127.0.0.1'
PORT = 13375

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((HOST, PORT))
s.close()