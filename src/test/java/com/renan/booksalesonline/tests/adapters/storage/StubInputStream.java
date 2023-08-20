package com.renan.booksalesonline.tests.adapters.storage;

import java.io.IOException;
import java.io.InputStream;

public class StubInputStream extends InputStream {
    private final int desiredSize;
    private final byte[] seed;
    private int actualSize = 0;

    public StubInputStream(int desiredSize, String seed) {
        this.desiredSize = desiredSize;
        this.seed = seed.getBytes();
    }

    @Override
    public int read() throws IOException {
        return 0;
    }
}