package me.corruptionhades.customcosmetics.ui.comp;

public interface Callback {

    interface ClickCallback {
        void onClick();
    }

    interface ValueChangeCallback<Type> {
        void onChange(Type oldVal, Type newVal);
    }
}
