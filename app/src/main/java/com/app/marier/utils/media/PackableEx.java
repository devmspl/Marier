package com.app.marier.utils.media;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}
