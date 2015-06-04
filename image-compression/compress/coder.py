import huffman
import cv
import bitstring
import pickle
import StringIO
import uu
from xml.dom.minidom import Document
from xml.etree.ElementTree import ElementTree

def encode(image_mat, filename="out.xml"):
    """encode and image_mat"""
    
    # generate huffman keys
    keys = huffman.get_keys(image_mat)
    
    # pack data using huffman keys
    d = StringIO.StringIO()
    data = pack_data(image_mat, keys)
    di = StringIO.StringIO(data)
    #convert to plain ascii
    uu.encode(di, d)
    
    #serialize keys
    s = StringIO.StringIO()
    pickle.dump(keys,s)
    
    doc = Document()
    im_tag = doc.createElement("image")
    doc.appendChild(im_tag)
    
    #rows
    rows_tag = doc.createElement("rows")
    im_tag.appendChild(rows_tag)
    rows_node = doc.createTextNode(str(image_mat.rows))
    rows_tag.appendChild(rows_node)
    
    #cols
    cols_tag = doc.createElement("cols")
    im_tag.appendChild(cols_tag)
    cols_node = doc.createTextNode(str(image_mat.cols))
    cols_tag.appendChild(cols_node)
    
    #data
    data_tag = doc.createElement("data")
    im_tag.appendChild(data_tag)
    data_node = doc.createTextNode(d.getvalue())
    data_tag.appendChild(data_node)
    
    #tree
    tree_tag = doc.createElement("tree")
    im_tag.appendChild(tree_tag)
    tree_node = doc.createTextNode(s.getvalue())
    tree_tag.appendChild(tree_node)
    
    file_object = open(filename, "w")
    doc.writexml(file_object, encoding="utf-8")
    file_object.close()
    
def decode(filename):
    """decode an xml file to image_mat"""
    #open xml
    doc = ElementTree()
    doc.parse(filename)
    
    # get rows
    rows = doc.find("rows")
    rows = int(rows.text)
    
    # get cols
    cols = doc.find("cols")
    cols = int(cols.text)
    
    # get data
    data = doc.find("data")
    d = StringIO.StringIO(data.text)
    o = StringIO.StringIO()
    uu.decode(d,o)
    data = o.getvalue()
    
    # get keys
    k = doc.find("tree")
    s = StringIO.StringIO(k.text)
    keys_temp = pickle.load(s)
    keys = {}
    # flip keys
    for i in keys_temp.keys():
        keys[keys_temp[i]] = i
        
    imlist = unpack_data(data, keys)
    
    mat = cv.CreateMat(rows, cols, cv.CV_8UC1)
    i = 0
    for x in range(cols):
        for y in range(rows):
            mat[x,y] = imlist[i]
            i += 1
            
    return mat

def pack_data(image_mat, keys):
    """packs the data into a group of bytes"""
    
    out = ""
    
    # replace each value with it's respective bit symbol
    for x in range(image_mat.cols):
        for y in range(image_mat.rows):
            out += keys[image_mat[x,y]]

    b = bitstring.BitArray(bin=out)
    return b.tobytes()

    
def unpack_data(string, keys):
    b = bitstring.BitArray(bytes=string)
    values = []
    temp = ''

    for i in b.bin:
        if temp not in keys:
            temp+=i
        else:
            values.append(keys[temp])
            temp = i
    if temp in keys:
        values.append(keys[temp])
 
    return values
    
if __name__ == '__main__':
    im = cv.LoadImageM("kessel.tif", cv.CV_LOAD_IMAGE_GRAYSCALE)
    mat = cv.CreateMat(4, 4, cv.CV_8UC1)
    cv.Set(mat, 1.0)
    mat[0,0] = 0
    mat[0,1] = 1
    mat[0,2] = 2
    mat[0,3] = 3
    mat[1,0] = 10
    mat[1,1] = 11
    mat[1,2] = 12
    mat[1,3] = 13
    mat[2,0] = 20
    mat[2,1] = 21
    mat[2,2] = 22
    mat[2,3] = 23
    mat[3,0] = 30
    mat[3,1] = 31
    mat[3,2] = 32
    mat[3,3] = 33
    encode(im)
    decode("out.xml")







