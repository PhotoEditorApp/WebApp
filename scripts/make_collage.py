import sys
import numpy as np
from PIL import Image

if len(sys.argv) > 1:
    files = [f for f in sys.argv][1:-1]
    images = [np.array(Image.open(img_path.rstrip())) for img_path in files]
    min_shape = min([im[:, :, 0].shape for im in images])
    images = [np.dstack([np.array(Image.fromarray(s_img[:, :, i].astype(np.uint8)).resize(min_shape, Image.ANTIALIAS))
                         for i in range(3)]) for s_img in images]
    collage = np.hstack(images)

    collage = Image.fromarray(collage.astype(np.uint8))
    collage.save(sys.argv[-1])
