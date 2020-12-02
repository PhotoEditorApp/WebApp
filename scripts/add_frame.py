import sys
import numpy as np
import cv2

if len(sys.argv) == 4:
    image = cv2.imread(sys.argv[1].rstrip())
    frame = cv2.imread(sys.argv[2].rstrip())

    frame[frame > 240] = 0

    off_x, off_y = frame.shape[1] // 2, frame.shape[0] // 2
    while frame[off_y, off_x, :].sum() == 0:
        off_x -= 1
    frame_size_x = off_x

    off_x = frame.shape[1] // 2
    while frame[off_y, off_x, :].sum() == 0:
        off_y -= 1
    frame_size_y = off_y

    image_shape = (image.shape[1] + 2 * frame_size_x, image.shape[0] + 2 * frame_size_y)
    frame = np.dstack([cv2.resize(frame[:, :, i], image_shape) for i in range(3)])
    frame[frame_size_y:-frame_size_y, frame_size_x:-frame_size_x, :] = image

    cv2.imwrite(sys.argv[-1], frame)
