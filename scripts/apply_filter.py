import sys
import numpy as np
from PIL import Image, ImageFilter


def sharpen_img(image, kernel_size=(5, 5), sigma=1.0, amount=2.0, threshold=0):
    blurred = Image.fromarray(image.astype(np.uint8))
    blurred.filter(ImageFilter.GaussianBlur(radius=kernel_size[0]))
    blurred = np.array(blurred)
    sharpened = float(amount + 1) * image - float(amount) * blurred
    sharpened = np.maximum(sharpened, np.zeros(sharpened.shape))
    sharpened = np.minimum(sharpened, 255 * np.ones(sharpened.shape))
    sharpened = sharpened.round().astype(np.uint8)
    if threshold > 0:
        low_contrast_mask = np.absolute(image - blurred) < threshold
        np.copyto(sharpened, image, where=low_contrast_mask)
    return sharpened


if len(sys.argv) == 4:
    image = np.array(Image.open(sys.argv[2].rstrip()))

    if sys.argv[1] == '-wb':
        image = np.dstack((image[:, :, 0], image[:, :, 0], image[:, :, 0]))
    elif sys.argv[1] == '-blur':
        image = np.array(Image.fromarray(image.astype(np.uint8))
                         .filter(ImageFilter.GaussianBlur(radius=3)))
    elif sys.argv[1] == '-sharp':
        image = sharpen_img(image)
    else:
        raise IOError('Invalid argument: {}'.format(sys.argv[1]))

    image = Image.fromarray(image.astype(np.uint8))
    image.save(sys.argv[-1])
