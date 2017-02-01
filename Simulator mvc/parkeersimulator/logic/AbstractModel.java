package parkeersimulator.logic;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import parkeersimulator.view.*;

/**
 * A AbstractModel
 */
public abstract class AbstractModel {

    private List<AbstractView> views;

    /**
     * This constructor creates an new empty list for views.
     */
    public AbstractModel() {
        views = new ArrayList<>();
    }

    /**
     * This adds a new view to the list.
     *
     * @param view The view that has to be added to the list.
     */
    public void addView(AbstractView view) {
        views.add(view);
        System.out.println(views.size());
    }

    /**
     * This updates all the view that are in the list.
     */
    public void notifyViews() {
        for (AbstractView v : views) {
            v.updateView();

        }
    }
}
