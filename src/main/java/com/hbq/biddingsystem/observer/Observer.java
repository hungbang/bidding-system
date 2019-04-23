package com.hbq.biddingsystem.observer;

public abstract class Observer {
    protected Subject subject;
    public abstract void update();
}
