package com.github.llyb120.json;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Generator implements Iterable{

    private Iterable arrSource;
    private Map mapSource;
    private GeneratorFunc func;

    public Generator(Iterable source, GeneratorFunc func) {
        this.arrSource = source;
        this.func = func;
    }



    private List call(){
        List ret = new ArrayList<>();
        if (arrSource != null) {
            for (Object o : arrSource) {
                try {
                    ret.add(func.call(o));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else if(mapSource != null){

        }
        return ret;
    }

    @Override
    public Iterator iterator() {
        return this.call().iterator();
    }
}
