#!/usr/bin/env python

import sys
import cv
from compress import coder
from compress import gray_level

if __name__ == '__main__':
    
    error = "Error, usage: ia.py operation filename outputname levels"
    
    if len(sys.argv) >= 4:
        prog = sys.argv[1]
        filename = sys.argv[2]
        outname = sys.argv[3]
        levels = 0
    else:
        print error
        exit()
        
    if len(sys.argv) >= 5:
        levels = int(sys.argv[4])
    
    #get type
    if prog == "comp":
        print "Compressing"
        # open image
        image = cv.Load(filename)
        # reduce gray levels
        if levels > 0:
            image = gray_level.reduce_levels(image)
        #encode
        coder.encode(image, outname)
    elif prog == "decomp":
        print "Depcompressing"
        decomped = coder.decode(filename)
        cv.Save(outname, decomped)
    else:
        #error
        print error