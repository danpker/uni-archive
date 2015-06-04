#!/usr/bin/env python

import cv, cv2

def mapper(image):
    """converts a spatial image using DCT"""
    pass

def imapper(code):
    """runs the inverse of mapper"""
    pass
if __name__ == '__main__':
    image = cv2.imread("kessel.png", cv2.CV_LOAD_IMAGE_GRAYSCALE)
    cv.ShowImage('', image)