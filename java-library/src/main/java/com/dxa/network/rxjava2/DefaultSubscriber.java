package com.dxa.network.rxjava2;


import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * 默认的Subscriber
 */
public abstract class DefaultSubscriber<T> implements Subscriber<T> {

    protected final String tag;
    private Subscription subscription;

    public DefaultSubscriber() {
        this("Subscriber");
    }

    public DefaultSubscriber(String tag) {
        this.tag = tag;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public abstract void onNext(T t);

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {

    }
}
