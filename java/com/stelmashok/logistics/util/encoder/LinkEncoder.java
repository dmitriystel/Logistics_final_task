package com.stelmashok.logistics.util.encoder;

import java.util.Optional;

public interface LinkEncoder {
    Optional<String> encodeURL(String value);
    Optional<String> decodeURL(String value);
}
