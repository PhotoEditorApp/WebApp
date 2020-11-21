import sys
import numpy as np
import cv2

if len(sys.argv) > 1:
    files = [f for f in sys.argv][1:-1]
    images = [cv2.imread(img_path.rstrip()) for img_path in files]
    min_shape = min([im[:, :, 0].shape for im in images])
    images = [np.dstack([cv2.resize(s_img[:, :, i], min_shape) for i in range(3)])
              for s_img in images]
    collage = np.hstack(images)

    cv2.imwrite(sys.argv[-1], collage)
