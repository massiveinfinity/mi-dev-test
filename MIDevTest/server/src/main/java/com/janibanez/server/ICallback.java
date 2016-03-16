package com.janibanez.server;

import java.io.IOException;

/**
 * Created by jwgibanez on 21/01/2016.
 */
public interface ICallback<T> {

    void onFailure(Throwable throwable);
    void onResponse(T response) throws IOException;

}
