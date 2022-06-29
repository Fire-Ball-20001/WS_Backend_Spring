package org.backend.spring.actions;

public interface BinaryAction <T,V,R>{
    R execute(T object, V second);
}
