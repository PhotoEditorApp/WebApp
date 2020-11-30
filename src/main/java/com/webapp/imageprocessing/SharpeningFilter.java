package com.webapp.imageprocessing;

import com.webapp.domain.UserImage;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SharpeningFilter extends Filter{
    public SharpeningFilter(Path rootLocation, UserImage userImage) {
        super(rootLocation, userImage);
    }

    @Override
    List<String> prepareArguments() {
        return new ArrayList<>(
            List.of(
                    "-sharp",
                    rootLocation.resolve(userImage.getName()).normalize().toAbsolutePath().toString()
            )
        );
    }
}
