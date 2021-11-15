package com.warh.damlab3.dao;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BasicAuthInterceptor implements Interceptor {

    private String credenciales;

    public BasicAuthInterceptor(String usuario, String contrasena){
        this.credenciales = Credentials.basic(usuario, contrasena);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder().header("Authorization", credenciales).build();
        return chain.proceed(authenticatedRequest);
    }
}
