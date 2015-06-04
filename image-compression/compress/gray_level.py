import cv

def reduce_levels(image, levels=16):
    """reduce the number of levels in an image"""
    
    #find the number of bytes per pixel
    step = image.step
    rows = image.rows
    bytes_per_pixel = 2**((step / rows)*8)
    
    steps = 256/levels
    #build thresholds
    thresholds = {}
    for i in range(bytes_per_pixel):
        thresholds[i] = (i/steps)*steps
        
    #threshold image
    for x in range(image.cols):
        for y in range(image.rows):
            image[x,y] = thresholds[image[x,y]]
            
    return image
    
def test():
    """test"""
    levels = 16
    image = cv.LoadImageM("../images/kessel.tif", cv.CV_LOAD_IMAGE_GRAYSCALE)
    cv.SaveImage("test.tif", reduce_levels(image, levels))
    
if __name__ == '__main__':
    test()
    
    