
package com.norika.android.library.sample;

public class Person implements Cloneable {
    private String name;
    private int age;

    @Override
    public Person clone() throws CloneNotSupportedException {
        return (Person) super.clone();
    }

}
