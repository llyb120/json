package com.github.llyb120.json;

import com.github.llyb120.json.lambda.GeneratorFunc;
import com.github.llyb120.json.lambda.MapGeneratorFunc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Generator implements Iterable{

    private Iterable arrSource;
    private Map mapSource;
    private GeneratorFunc func;
    private MapGeneratorFunc mapFunc;

    public Generator(Iterable source, GeneratorFunc func) {
        this.arrSource = source;
        this.func = func;
    }

    public Generator(Map mapSource, MapGeneratorFunc func){
        this.mapSource = mapSource;
        this.mapFunc = func;
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
            for (Object o : mapSource.entrySet()) {
                try{
                    ret.add(mapFunc.call(((Map.Entry) o).getKey(), ((Map.Entry) o).getValue()));
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        }
        return ret;
    }

    @Override
    public Iterator iterator() {
        return this.call().iterator();
    }
}
