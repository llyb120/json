package com.github.llyb120.json.selector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SelectorNode {
    public LinkedList<SelectorKey> keys = new LinkedList<>();
    public SelectorNode next = null;
    public SelectorNode prev = null;

    public void setNext(SelectorNode next) {
        this.next = next;
        next.prev = this;
    }
}
