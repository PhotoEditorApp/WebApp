package com.webapp.imageprocessing;

import com.webapp.domain.UserImage;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SepiaFilter extends Filter{
    public SepiaFilter(Path rootLocation, UserImage userImage) {
        super(rootLocation, userImage);
    }

    @Override
    List<String> prepareArguments() {
        return new ArrayList<>(
            List.of(
                "-sepia",
                rootLocation.resolve(userImage.getName()).normalize().toAbsolutePath().toString()
            )
        );
    }
}
