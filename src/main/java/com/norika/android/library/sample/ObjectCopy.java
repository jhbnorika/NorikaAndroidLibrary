
package com.norika.android.library.sample;

public class ObjectCopy implements Cloneable {
    private Person p;
    private int code;

    public Person getP() {
        return p;
    }

    public void setP(Person p) {
        this.p = p;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    protected ObjectCopy clone() throws CloneNotSupportedException {
        ObjectCopy copy = (ObjectCopy) super.clone();
        copy.setP(copy.getP().clone());
        return copy;
    }

}
