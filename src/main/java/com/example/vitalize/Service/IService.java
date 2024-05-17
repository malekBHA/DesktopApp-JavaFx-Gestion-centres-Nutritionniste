package com.example.vitalize.Service;

import java.util.List;

public interface IService <T>{
    //crud
    void add(T t);
    void update(T t);
    void delete(T t);
     List<T> getAll();
     T getOne( int id );
}
