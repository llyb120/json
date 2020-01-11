package com.github.llyb120.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Generator implements Iterable{

    private Object source;
    private GeneratorFunc func;

    public Generator(Object source, GeneratorFunc func) {
        this.source = source;
        this.func = func;
    }

    private List call(){
        List ret = new ArrayList<>();
        if(source instanceof Iterable){
            ((Iterable) source).forEach(e -> {
                try {
                    ret.add(func.call(e));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }
        return ret;
    }

    @Override
    public Iterator iterator() {
        return this.call().iterator();
    }
}
