package com.github.llyb120.json;

import java.util.*;
import java.util.stream.Collectors;

import static com.github.llyb120.json.Json.*;


public class Arr implements List<Object> {

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
    public Object set(int index, Object element) {
        return list().set(index, element);
    }

    @Override
    public void add(int index, Object element) {
        list().add(index, element);
    }

    @Override
    public Object remove(int index) {
        return (Object) list().remove(index);
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
    public ListIterator<Object> listIterator() {
        return list().listIterator();
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
        return list().listIterator(index);
    }

    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
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
    public Iterator<Object> iterator() {
        return list().iterator();
    }

    @Override
    public Object[] toArray() {
        return list().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) list().toArray(a);
    }

    @Override
    public boolean add(Object o) {
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
    public boolean addAll(Collection<? extends Object> c) {
        return list().addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Object> c) {
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
    public Object get(int index ) {
        return (Object) list().get(index);
    }

    public Obj o(int index){
        return cast(list.get(index), Obj.class);
    }

    public <T> T toBson() {
        return castBson(this);
    }

    public static Arr fromBson(Object object) {
        return (Arr) Json.fromBson(object);
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

    public <T> T find(Class<T> clz, ArrFilterFunc<T> func){
        Arr items = filter( clz, func);
        if(items.isEmpty()){
            return null;
        }
        return (T) items.get(0);
    }

    public Obj find(ArrFilterFunc<Obj> func){
        return find(Obj.class, func);
    }

    public Arr filter(ArrFilterFunc<Obj> function){
        Arr ret = a();
        for (Object t : this) {
            if(function.call(ooo(t))){
                ret.add(t);
            }
        }
        return ret;
    }

    public <T> Arr filter(Class<T> clz, ArrFilterFunc<T> function){
        Arr ret = a();
        for (Object t : this) {
            if(function.call((T) t)){
                ret.add(t);
            }
        }
        return ret;
    }

    public List<Obj> oa(){
        Arr arr = a();
        for (Object o : this) {
            arr.add(ooo(o));
        }
        return (List)arr;
    }

//    public Arr add(Object ...objects){
//        for (Object object : objects) {
//           add(object);
//        }
//        return this;
//    }

    public Arr addAll(Object ...objects){
        addAll(Arrays.asList(objects));
        return this;
    }

    public <U> Obj group(ArrGroupFunc<U> function){
        Obj cache = Json.o();
        for (Object t : this) {
            try {
                String key = (String) function.call((U)t);
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

    @Override
    public String toString() {
        return Json.stringify(this);
    }

    //    public List<? extends Bson> toBsonArray(){
//        List list = new ArrayList();
//        for (Object o : this) {
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
