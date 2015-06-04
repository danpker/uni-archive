import cv
import sys

def show(image):
    """show the image"""
    cv.ShowImage("", image)
    cv.WaitKey(0)
    
if __name__ == '__main__':
    image = cv.Load(sys.argv[1])
    show(image)
