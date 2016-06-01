package com.poomoo.parttimejob.event;

/**
 * @author thanatos
 * @create 2016-01-05
 **/
public class Events<T> {

    public enum EventEnum {
        DELIVER_AVATAR, DELIVER_CITY, SET_INTENTION
    }

    public EventEnum what;
    public T message;

    public static <O> Events<O> just(O t) {
        Events<O> events = new Events<>();
        events.message = t;
        return events;
    }

    public <T> T getMessage() {
        return (T) message;
    }

}
