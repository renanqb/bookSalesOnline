package com.renan.booksalesonline.tests.domain.enums;

import com.renan.booksalesonline.domain.enums.ImageExtension;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageExtensionTest {

    @Test
    public void should_validate_image_extension_instance_methods() {

        var imageExtension = ImageExtension.GIF;
        assertThat(imageExtension.getText()).isEqualTo("gif");
        assertThat(imageExtension.toString()).isEqualTo("gif");
        assertThat(imageExtension.getContentType()).isEqualTo("image/gif");
    }

    @Test
    public void should_validate_image_extension_static_methods() {

        var expected = "bmp, gif, jpg, jpeg, img, png and tiff";
        assertThat(ImageExtension.getExtensionAcceptedList()).isEqualTo(expected);
    }
}
