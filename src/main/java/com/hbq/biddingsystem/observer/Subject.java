package com.hbq.biddingsystem.observer;

import com.google.common.collect.Lists;

import java.util.List;

public class Subject {

    private List<Observer> observers = Lists.newArrayList();
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        notifyAllObservers();
    }

    private void notifyAllObservers() {
        observers.forEach(Observer::update);
    }
}
