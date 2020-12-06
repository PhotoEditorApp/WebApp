import sys
import numpy as np
from PIL import Image

if len(sys.argv) == 4:
    image = np.array(Image.open(sys.argv[1].rstrip()))
    frame = np.array(Image.open(sys.argv[2].rstrip()))

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
    frame = np.dstack([np.array(Image.fromarray(frame[:, :, i].astype(np.uint8)).resize(image_shape, Image.ANTIALIAS))
                       for i in range(3)])
    frame[frame_size_y:-frame_size_y, frame_size_x:-frame_size_x, :] = image

    frame = Image.fromarray(frame.astype(np.uint8))
    frame.save(sys.argv[-1])
