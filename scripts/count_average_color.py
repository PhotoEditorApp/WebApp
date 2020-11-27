import sys
import numpy as np
from functools import reduce
import cv2

if len(sys.argv) == 2:
    image = cv2.imread(sys.argv[1].rstrip())
    avg_color = list(map(int, [image[:, :, i].sum() // (image.size // 3) for i in range(image.shape[-1])]))

    const = 0x00FF0000
    shifts = [16, 8, 0]
    avg_color = [(color << sh1) & (const >> sh2) for sh1, sh2, color in zip(shifts, shifts[::-1], avg_color)]

    print((const << 8) | reduce(lambda color1, color2: color1 | color2, avg_color))
