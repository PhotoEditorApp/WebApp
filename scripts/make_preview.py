import sys
import numpy as np
import cv2

if len(sys.argv) == 3:
    image = cv2.imread(sys.argv[1].rstrip())
    min_shape = (200, 200)
    image = np.dstack([cv2.resize(image[:, :, i], min_shape) for i in range(3)])

    cv2.imwrite(sys.argv[-1], image)
