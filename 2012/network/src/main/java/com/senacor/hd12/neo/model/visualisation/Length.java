package com.senacor.hd12.neo.model.visualisation;

public class Length {
    private final int length;

    public Length(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Length length1 = (Length) o;

        if (length != length1.length) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return length;
    }
}
