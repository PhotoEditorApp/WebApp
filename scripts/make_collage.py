import sys
import numpy as np
from PIL import Image


def fix_wrong_format(images):
    new_images = []
    for i in range(len(images)):
        if len(images[i].shape) == 2:
            new_images.append(np.dstack((images[i], np.zeros(images[i].shape), np.zeros(images[i].shape))))
        elif images[i].shape[-1] == 2:
            new_images.append(np.dstack((images[i], np.zeros(images[i].shape))))
        elif images[i].shape[-1] > 3:
            new_images.append(images[i][:, :, :3])
        else:
            new_images.append(images[i])
    return new_images


if len(sys.argv) > 1:
    files = [f for f in sys.argv][1:-1]
    images = [np.array(Image.open(img_path.rstrip())) for img_path in files]

    images = fix_wrong_format(images)
    min_shape = min([im[:, :, 0].shape for im in images])
    arr_of_k = [[int(a / (im[:, :, 0].shape[0] / min_shape[0])) if i != 0 else b for a, b, i in
                 zip(im[:, :, 0].shape, min_shape, range(len(min_shape)))] for im in images]
    images = [np.dstack([
        np.array(Image.fromarray(s_img[:, :, i].astype(np.uint8))
                 .resize(tuple(reversed(size_of_img)), Image.ANTIALIAS)) for i in range(s_img.shape[2])
    ]) for s_img, size_of_img in zip(images, arr_of_k)
    ]
    collage = np.hstack(images)

    collage = Image.fromarray(collage.astype(np.uint8))
    collage.save(sys.argv[-1])
