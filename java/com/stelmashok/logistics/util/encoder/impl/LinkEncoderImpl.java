package com.stelmashok.logistics.util.encoder.impl;

import com.stelmashok.logistics.util.encoder.LinkEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class LinkEncoderImpl implements LinkEncoder {
    private static final Logger logger = LogManager.getLogger();
    private static LinkEncoderImpl instance;

    private LinkEncoderImpl() {
    }

    public static LinkEncoderImpl getInstance() {
        if (instance == null) {
            instance = new LinkEncoderImpl();
        }
        return instance;
    }

    @Override
    public Optional<String> encodeURL(String value) {
        try {
            return Optional.of(URLEncoder.encode(value, StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException e) {
            logger.error("failed to encode url", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> decodeURL(String value) {
        try {
            return Optional.of(URLDecoder.decode(value, StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException e) {
            logger.error("failed to decode url", e);
            return Optional.empty();
        }
    }
}
