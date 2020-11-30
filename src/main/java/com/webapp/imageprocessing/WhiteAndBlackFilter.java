package com.webapp.imageprocessing;

import com.webapp.domain.UserImage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WhiteAndBlackFilter extends Filter{
    public WhiteAndBlackFilter(Path rootLocation, UserImage userImage) {
        super(rootLocation, userImage);
    }

    @Override
    List<String> prepareArguments() {
        return new ArrayList<>(
            List.of(
                "-wb",
                rootLocation.resolve(userImage.getName()).normalize().toAbsolutePath().toString()
            )
        );
    }
}
