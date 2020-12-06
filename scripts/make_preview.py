import sys
import numpy as np
from PIL import Image

if len(sys.argv) == 3:
    image = np.array(Image.open(sys.argv[1].rstrip()))
    min_shape = (int(image.shape[1] / (image.shape[0] / 200)), 200)
    image = np.dstack([np.array(Image.fromarray(image[:, :, i].astype(np.uint8)).resize(min_shape, Image.ANTIALIAS))
                       for i in range(3)])

    image = Image.fromarray(image.astype(np.uint8))
    image.save(sys.argv[-1])
