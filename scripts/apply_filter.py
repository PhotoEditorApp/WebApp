import sys
import numpy as np
import cv2


def good_gaus_kernel(l=5, sig=1.):
    ax = np.linspace(-(l - 1) / 2., (l - 1) / 2., l)
    xx, yy = np.meshgrid(ax, ax)

    kernel = np.exp(-0.5 * (np.square(xx) + np.square(yy)) / np.square(sig))

    return kernel / np.sum(kernel)


def sharpen_img(image, kernel_size=(5, 5), sigma=1.0, amount=2.0, threshold=0):
    blurred = cv2.GaussianBlur(image, kernel_size, sigma)
    sharpened = float(amount + 1) * image - float(amount) * blurred
    sharpened = np.maximum(sharpened, np.zeros(sharpened.shape))
    sharpened = np.minimum(sharpened, 255 * np.ones(sharpened.shape))
    sharpened = sharpened.round().astype(np.uint8)
    if threshold > 0:
        low_contrast_mask = np.absolute(image - blurred) < threshold
        np.copyto(sharpened, image, where=low_contrast_mask)
    return sharpened


if len(sys.argv) == 4:
    image = cv2.imread(sys.argv[2].rstrip())

    if sys.argv[1] == '-wb':
        image = np.dstack((image[:, :, 0], image[:, :, 0], image[:, :, 0]))
    elif sys.argv[1] == '-blur':
        image = cv2.filter2D(image, -1, good_gaus_kernel())
    elif sys.argv[1] == '-sharp':
        image = sharpen_img(image)
    else:
        raise IOError('Invalid argument: {}'.format(sys.argv[1]))

    cv2.imwrite(sys.argv[-1], image)
