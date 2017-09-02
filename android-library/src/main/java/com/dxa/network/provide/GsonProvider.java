package com.dxa.network.provide;

import com.google.gson.Gson;

class GsonProvider {

    private GsonProvider() {
    }

    public static Gson provideGson() {
        return GsonHolder.gson;
    }

    private static class GsonHolder {
        private static volatile Gson gson;

        static {
            gson = new Gson();
        }
    }

}
