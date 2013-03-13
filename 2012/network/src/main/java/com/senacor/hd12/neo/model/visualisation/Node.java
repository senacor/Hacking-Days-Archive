package com.senacor.hd12.neo.model.visualisation;

public class Node {
    private final String color;
    private final String shape;
    private final String label;

    public Node(String color, String shape, String label) {
        this.color = color;
        this.shape = shape;
        this.label = label;
    }

    public String getColor() {
        return color;
    }

    public String getShape() {
        return shape;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (color != null ? !color.equals(node.color) : node.color != null) return false;
        if (label != null ? !label.equals(node.label) : node.label != null) return false;
        if (shape != null ? !shape.equals(node.shape) : node.shape != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = color != null ? color.hashCode() : 0;
        result = 31 * result + (shape != null ? shape.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        return result;
    }
}
