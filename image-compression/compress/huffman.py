#!/usr/bin/env python

import cv
import heapq

def freq_list(image_mat):
    """takes in a cv image and outputs a freq list [(freq, symbol),]"""
    totals = {}
    total = 0
    for x in range(image_mat.cols):
        for y in range(image_mat.rows):
            if image_mat[x,y] not in totals:
                totals[image_mat[x,y]] = 1.0
            else:
                totals[image_mat[x,y]] += 1.0
            total += 1
            
    tuplelist = []
    for i in totals:
        totals[i] = totals[i] / total
        
    for k in totals.keys():
        tuplelist.append((totals[k], k))
        
    return tuplelist

def build_keys(tree, code, prefix=''):
    """builds the key dictionary"""
    if len(tree) == 2:
        code[tree[1]] = prefix
    else:
        build_keys(tree[1], code, prefix+'0')
        build_keys(tree[2], code,prefix+'1')

    
def build_huffman_tree(freq_list):
    """takes in a freq list and outputs the huffman tree"""
    trees = list(freq_list)
    heapq.heapify(trees)
    
    while len(trees) > 1:
        right, left = heapq.heappop(trees), heapq.heappop(trees)
        node = (left[0] + right[0], left, right)
        heapq.heappush(trees, node)
        
    return trees[0]
    
def get_keys(image):
    """return the huffman keyset for image"""
    code = {}
    freq = freq_list(image)
    tree = build_huffman_tree(freq)
    build_keys(tree, code)
    return code
    
if __name__ == '__main__':
    
    image = cv.Load("test.xml")
    mat = cv.CreateMat(5, 5, cv.CV_32FC1)
    cv.Set(mat, 1.0)
    mat[0,1] = 2
    mat[1,0] = 2
    mat[1,4] = 4
    code = {}
    freq = freq_list(image)
    tree = build_huffman_tree(freq)
    build_keys(tree, code)
    print code
    