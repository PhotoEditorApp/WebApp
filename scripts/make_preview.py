import sys
import numpy as np
from PIL import Image


def fix_wrong_format(image):
    if len(image.shape) == 2:
        image = np.dstack((image, np.zeros(image.shape), np.zeros(image.shape)))
    elif image.shape[-1] == 2:
        image = np.dstack((image, np.zeros(image.shape)))
    elif image.shape[-1] > 3:
        image = image[:, :, :3]

    return image


if len(sys.argv) == 3:
    image = np.array(Image.open(sys.argv[1].rstrip()))
    image = fix_wrong_format(image)

    min_shape = (int(image.shape[1] / (image.shape[0] / 200)), 200)
    image = np.dstack([np.array(Image.fromarray(image[:, :, i].astype(np.uint8)).resize(min_shape, Image.ANTIALIAS))
                       for i in range(3)])

    image = Image.fromarray(image.astype(np.uint8))
    image.save(sys.argv[-1])
