package com.github.llyb120.json.selector;

public class PropertyOperator {
    public PropertyOperatorType op;
    public String value;

    public PropertyOperator( PropertyOperatorType op, String value) {
        this.op = op;
        this.value = value;
    }
}
