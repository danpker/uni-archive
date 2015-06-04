# CSP Coursework
# Daniel Parker 2011
#
# ui.py - ui for carpark

from Tkinter import *
from pycsp import *

@process
def run(arrive_but, depart_but):
    root = Tk()
    ui = UI(root)
    root.mainloop()
	
class UI:
    """User interface"""
    def __init__(self, master):
        frame = Frame(master)
        frame.pack()
        master.title('Car Park')
        
        self.label = Label(frame, text="Choose")
        self.label.pack()
        self.but_arrive = Button(frame, text="Arrive", command=self.arrive)
    	self.but_arrive.pack(side=LEFT)
    	self.but_depart = Button(frame, text="Depart", command=self.depart)
    	self.but_depart.pack(side=LEFT)
        	
    def arrive(self):
        """send an arrive message"""
        arrive_but(1)
    
    def depart(self):
        """send a depart message"""
        depart_but(1)

if __name__ == '__main__':
    #test the ui on it's own, will crash if a button is pressed
    root = Tk()
    ui = UI(root)
    root.mainloop()