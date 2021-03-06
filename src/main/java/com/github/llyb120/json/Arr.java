package com.github.llyb120.json;

import com.github.llyb120.json.lambda.ArrFilterFunc;
import com.github.llyb120.json.lambda.ArrGroupFunc;
import com.github.llyb120.json.lambda.GeneratorFunc;
import com.github.llyb120.json.selector.JsonPicker;
import org.bson.Document;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.llyb120.json.Json.*;


public class Arr<X> implements List<X>, Serializable {

    private List list;

    public Arr(){
        list = new ArrayList();
    }

    public List list(){
        return list;
    }

    public Arr list(List list){
        this.list = list;
        return this;
    }

    @Override
    public X set(int index, X element) {
        return (X) list().set(index, element);
    }

    @Override
    public void add(int index, X element) {
        list().add(index, element);
    }

    @Override
    public X remove(int index) {
        return (X) list().remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list().lastIndexOf(o);
    }

    @Override
    public ListIterator<X> listIterator() {
        return list().listIterator();
    }

    @Override
    public ListIterator<X> listIterator(int index) {
        return list().listIterator(index);
    }

    @Override
    public List<X> subList(int fromIndex, int toIndex) {
        return list().subList(fromIndex, toIndex);
    }

    @Override
    public int size() {
        return list().size();
    }

    @Override
    public boolean isEmpty() {
        return list().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list().contains(o);
    }

    @Override
    public Iterator<X> iterator() {
        return list().iterator();
    }

    @Override
    public X[] toArray() {
        return (X[]) list().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) list().toArray(a);
    }

    @Override
    public boolean add(X o) {
        return list().add(o);
    }


    @Override
    public boolean remove(Object o) {
        return list().remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return list().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends X> c) {
        return list().addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends X> c) {
        return list().addAll(c);
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        return list().removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return list().retainAll(c);
    }

    @Override
    public void clear() {

    }

    @Override
    public X get(int index ) {
        return (X) list().get(index);
    }

    public Obj o(int index){
        return cast(list.get(index), Obj.class);
    }

    public String join(){
        return join(",");
    }
    public String join(String ch){
        return join(ch, "");
    }
    public String join(String ch,String wrap){
        return this.stream()
                .filter(e -> e != null)
                .map(e -> String.valueOf(e))
                .map(e -> wrap + e + wrap)
                .collect(Collectors.joining(ch));
    }

//    public <T> T find(Class<T> clz, ArrFilterFunc<T> func){
//        Arr items = filter( clz, func);
//        if(items.isEmpty()){
//            return null;
//        }
//        return (T) items.get(0);
//    }

    public Obj find(ArrFilterFunc<X> func){
        Arr items = filter(func);
        if(items.isEmpty()){
            return null;
        }
        return (Obj) items.get(0);
    }

    public Arr filter(ArrFilterFunc<X> function){
        Arr ret = a();
        for (X t : this) {
            if(function.call((t))){
                ret.add(t);
            }
        }
        return ret;
    }

//    public <T> Arr filter(Class<T> clz, ArrFilterFunc<T> function){
//        Arr ret = a();
//        for (X t : this) {
//            if(function.call((T) t)){
//                ret.add(t);
//            }
//        }
//        return ret;
//    }

    /**
     *
     */
    @Deprecated
    public Arr<Obj> oa(){
        Arr<?> arr = this;
        return aaa(arr.stream()
                .map(Json::ro)
                .toArray());
    }

    public Stream<Obj> os(){
        Arr<?> arr = this;
        return arr.stream()
            .map(Json::ro);
    }

    public void ofor(Consumer<? super Obj> action){
        Arr<?> arr = this;
        for (Object o : arr) {
            Obj obj = ro(o);
            action.accept(obj);
        }
    }


    public String s(int i){
        if(i >= size()){
            return null;
        }
        X item = get(i);
        if (item == null) {
            return null;
        }
        return item.toString();
    }

//    public Arr add(X ...objects){
//        for (X object : objects) {
//           add(object);
//        }
//        return this;
//    }

    public Arr addAll(X ...objects){
        addAll(Arrays.asList(objects));
        return this;
    }

    public Obj group(ArrGroupFunc<X> function){
        Obj cache = Json.o();
        for (X t : this) {
            try {
                String key = (String) function.call((X)t);
                Arr arr = cache.aa(key);
                if (arr == null) {
                    cache.put(key, arr = a());
                }
                arr.add(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cache;
    }

//    public Arr spread(){
//        spread = true;
//        return this;
//    }


    public Object or(Object obj){
        if(isEmpty()){
            return obj;
        }
        return this;
    }

    public <T> Arr<T> pick(String path, Class<T> clz){
        return JsonPicker.pick(path, this, clz);
    }
    public Arr pick(String path){
        return JsonPicker.pick(path, this, Object.class);
    }

    public Generator yield(GeneratorFunc<X> func){
        return Json.yield(this, func);
    }

    @Override
    public String toString() {
        return Json.stringify(this);
    }

    @Deprecated
    public List<Document> toBson(){
        return ba($expand, this);
    }

    @Deprecated
    public <T> T to(Class<T> clz){
        return Json.cast(this, clz);
    }

    //    public List<? extends Bson> toBsonArray(){
//        List list = new ArrayList();
//        for (X o : this) {
//            if(o instanceof Obj){
//                list.add(((Obj) o).toBson());
//            } else if(o instanceof Arr){
//                list.add(((Arr) o).toBsonArray());
//            } else {
//                list.add(o);
//            }
//        }
//        return list;
//    }

}
