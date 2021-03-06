/*
 * Copyright (c) 2020.
 * Copyright by Tim and Felix
 */

package de.ft.interitus.events.rightclick;


import de.ft.interitus.UI.UIElements.UIElements.Button;

import java.util.EventObject;

public class RightClickButtonSelectEvent extends EventObject {
    Button button;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */

    public RightClickButtonSelectEvent(Object source, Button button) {
        super(source);
        this.button = button;
    }

    public Button getButton() {
        return button;
    }
}
