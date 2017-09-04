package com.dxa.network.rxjava;

/**
 * 携带对象
 */

public abstract class TransportSubscriber<T, Transport> extends DefaultSubscriber<T> {

    private Transport transport;

    public TransportSubscriber(Transport transport) {
        this.transport = transport;
    }

    public TransportSubscriber(String tag, Transport transport) {
        super(tag);
        this.transport = transport;
    }

    public Transport getTransport() {
        return transport;
    }
}
