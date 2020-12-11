import sys
import numpy as np
from functools import reduce
from PIL import Image

if len(sys.argv) == 4:
    image = np.array(Image.open(sys.argv[1].rstrip()))
    frame = np.array(Image.open(sys.argv[2].rstrip()))

    if len(frame.shape) == 2:
        frame = np.dstack((frame, np.zeros(frame.shape), np.zeros(frame.shape)))

    image_shape = (image.shape[1], image.shape[0])
    frame = np.dstack([np.array(Image.fromarray(frame[:, :, i].astype(np.uint8)).resize(image_shape, Image.ANTIALIAS))
                       for i in range(3)])

    frame_deriv_x = reduce(lambda x, y: x + y, [frame[:, :, i] for i in range(frame.shape[2])]) // 3
    frame_deriv_y = reduce(lambda x, y: x + y, [frame[:, :, i] for i in range(frame.shape[2])]) // 3

    for i in range(frame_deriv_x.shape[0]):
        frame_deriv_x[i, :] = np.convolve(np.array([1, 0, -1]), frame_deriv_x[i, :], mode="same") // 3

    for j in range(frame_deriv_y.shape[1]):
        frame_deriv_y[:, j] = np.convolve(np.array([1, 0, -1]), frame_deriv_y[:, j], mode="same") // 3

    frame_deriv = np.round(np.sqrt(frame_deriv_x ** 2 + frame_deriv_y ** 2)).astype(np.uint8)
    off_x, off_y = frame_deriv.shape[1] // 2, frame_deriv.shape[0] // 2
    threshold = frame_deriv.max()

    while frame_deriv[off_y, off_x] == 0:
        off_x -= 1
    frame_size_x = off_x

    off_x = frame_deriv.shape[1] // 2
    while frame_deriv[off_y, off_x] == 0:
        off_y -= 1
    frame_size_y = off_y

    image_shape = (image.shape[1] + 2 * frame_size_x, image.shape[0] + 2 * frame_size_y)
    frame = np.dstack([np.array(Image.fromarray(frame[:, :, i].astype(np.uint8)).resize(image_shape, Image.ANTIALIAS))
                       for i in range(3)])

    frame[frame_size_y:-frame_size_y, frame_size_x:-frame_size_x, :] = image

    frame = Image.fromarray(frame.astype(np.uint8))
    frame.save(sys.argv[-1])
