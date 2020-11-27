import sys
import numpy as np
import cv2

if len(sys.argv) == 2:
    image = cv2.imread(sys.argv[1].rstrip())
    avg_color = int(image.sum() // image.size)
    print(avg_color)
